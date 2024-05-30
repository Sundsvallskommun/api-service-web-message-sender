package se.sundsvall.webmessagesender.service;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.webmessagesender.service.ServiceConstants.ERROR_WEB_MESSAGE_NOT_FOUND;
import static se.sundsvall.webmessagesender.service.mapper.OepMapper.toAddMessage;
import static se.sundsvall.webmessagesender.service.mapper.WebMessageMapper.toWebMessageEntity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.xml.datatype.DatatypeConfigurationException;

import jakarta.xml.soap.Detail;
import jakarta.xml.soap.SOAPFault;
import jakarta.xml.ws.soap.SOAPFaultException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;

import se.sundsvall.webmessagesender.api.model.CreateWebMessageRequest;
import se.sundsvall.webmessagesender.api.model.ExternalReference;
import se.sundsvall.webmessagesender.generatedsources.oep.AddMessage;
import se.sundsvall.webmessagesender.generatedsources.oep.AddMessageResponse;
import se.sundsvall.webmessagesender.integration.db.WebMessageRepository;
import se.sundsvall.webmessagesender.integration.db.model.ExternalReferenceEntity;
import se.sundsvall.webmessagesender.integration.db.model.WebMessageEntity;
import se.sundsvall.webmessagesender.integration.oep.OepIntegration;
import se.sundsvall.webmessagesender.service.mapper.OepMapper;

@ExtendWith(MockitoExtension.class)
class WebMessageServiceTest {

	@Mock
	private WebMessageRepository webMessageRepository;

	@Mock
	private AddMessageResponse addMessageResponse;

	@Mock
	private AddMessage addMessageMock;

	@Mock
	private OepIntegration oepIntegration;

	@Mock
	private SOAPFaultException soapFaultException;

	@Mock
	private SOAPFault soapFault;

	@Mock
	private Detail detail;

	@InjectMocks
	private WebMessageService webMessageService;

	@Captor
	private ArgumentCaptor<AddMessage> addMessageCaptor;

	@Captor
	private ArgumentCaptor<WebMessageEntity> webMessageEntityCaptor;

	@Test
	void createWebMessageWithoutFlowInstanceId() {

		// Setup
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withExternalReferences(List.of(ExternalReference.create().withKey("key").withValue("value")))
			.withMessage("Message")
			.withPartyId(UUID.randomUUID().toString());

		// Mock
		final var webMessageEntity = toWebMessageEntity(createWebMessageRequest, null);
		when(webMessageRepository.save(any())).thenReturn(webMessageEntity);

		// Call
		final var result = webMessageService.createWebMessage(createWebMessageRequest);

		// Verification
		verify(webMessageRepository).save(webMessageEntity);
		verifyNoMoreInteractions(webMessageRepository);

		assertThat(result).isNotNull();
	}

	@ParameterizedTest
	@ValueSource(strings = {"flowInstanceId", "flowinstanceid", "FLOWINSTANCEID"})
	void createWebMessageWithFlowInstanceId(String key) {

		// Setup
		final var value = "123456";
		final var partyId = UUID.randomUUID().toString();
		final var message = "Message";
		final var oepMessageId = Integer.MAX_VALUE;

		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withOepInstance("internal")
			.withExternalReferences(List.of(ExternalReference.create().withKey(key).withValue(value)))
			.withMessage(message)
			.withPartyId(partyId);


		final var webMessageEntity = toWebMessageEntity(createWebMessageRequest, oepMessageId);

		// Mock
		when(oepIntegration.addMessage(eq("internal"), any(AddMessage.class))).thenReturn(addMessageResponse);
		when(addMessageResponse.getMessageID()).thenReturn(oepMessageId);
		when(webMessageRepository.save(any())).thenReturn(webMessageEntity);

		// Call
		final var result = webMessageService.createWebMessage(createWebMessageRequest);

		// Verification
		verify(oepIntegration).addMessage(eq("internal"), addMessageCaptor.capture());
		verify(webMessageRepository).save(webMessageEntityCaptor.capture());
		verifyNoMoreInteractions(oepIntegration, webMessageRepository);

		assertThat(result).isNotNull();
		assertThat(addMessageCaptor.getValue().getFlowInstanceID()).isEqualTo(Integer.parseInt(value));
		assertThat(addMessageCaptor.getValue().getMessage().getMessage()).isEqualTo(message);
		assertThat(addMessageCaptor.getValue().getMessage().getAdded().toGregorianCalendar().toZonedDateTime().toLocalDateTime()).isCloseTo(LocalDateTime.now(), within(2, ChronoUnit.SECONDS));
		assertThat(addMessageCaptor.getValue().getExternalID()).isNull();
		assertThat(addMessageCaptor.getValue().getPrincipal()).isNull();
		assertThat(addMessageCaptor.getValue().getMessage().getAttachments()).isNullOrEmpty();
		assertThat(addMessageCaptor.getValue().getMessage().getUserID()).isNull();

		assertThat(webMessageEntityCaptor.getValue().getCreated()).isNull();
		assertThat(webMessageEntityCaptor.getValue().getId()).isNull();
		assertThat(webMessageEntityCaptor.getValue().getMessage()).isEqualTo(message);
		assertThat(webMessageEntityCaptor.getValue().getOepMessageId()).isEqualTo(oepMessageId);
		assertThat(webMessageEntityCaptor.getValue().getPartyId()).isEqualTo(partyId);
		assertThat(webMessageEntityCaptor.getValue().getExternalReferences())
			.hasSize(1)
			.extracting(ExternalReferenceEntity::getId, ExternalReferenceEntity::getKey, ExternalReferenceEntity::getValue)
			.containsExactly(tuple(0L, key, value));
	}

	@Test
	void createWebMessageThrowsSOAPFaultException() {

		// Setup
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("123456")))
			.withMessage("Message")
			.withPartyId(UUID.randomUUID().toString());

		// Mock throw from OEP integration
		when(oepIntegration.addMessage(any(), any())).thenThrow(soapFaultException);
		when(soapFaultException.getFault()).thenReturn(soapFault);
		when(soapFault.getFaultString()).thenReturn("Bogus error");
		when(soapFault.getDetail()).thenReturn(detail);
		when(detail.getDetailEntries()).thenReturn(Collections.emptyIterator());

		// Call
		final var problem = assertThrows(ThrowableProblem.class, () -> webMessageService.createWebMessage(createWebMessageRequest));

		// Verification
		assertThat(problem).isNotNull();
		assertThat(problem.getTitle()).isEqualTo(Status.INTERNAL_SERVER_ERROR.getReasonPhrase());
		assertThat(problem.getStatus()).isEqualTo(Status.INTERNAL_SERVER_ERROR);
		assertThat(problem.getDetail()).isEqualTo("Bogus error");

		verify(oepIntegration).addMessage(any(), any());
		verifyNoInteractions(webMessageRepository);
	}

	@Test
	void createWebMessageThrowsDatatypeConfigurationException() {

		// Setup
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("123456")))
			.withMessage("Message")
			.withPartyId(UUID.randomUUID().toString());

		// Mock throw from static method and test
		try (MockedStatic<OepMapper> mockMapper = mockStatic(OepMapper.class)) {
			mockMapper.when(() -> toAddMessage(any(), anyInt(), any())).thenThrow(new DatatypeConfigurationException());

			// Call
			final var problem = assertThrows(ThrowableProblem.class, () -> webMessageService.createWebMessage(createWebMessageRequest));

			// Verification
			assertThat(problem).isNotNull();
			assertThat(problem.getTitle()).isEqualTo(Status.INTERNAL_SERVER_ERROR.getReasonPhrase());
			assertThat(problem.getStatus()).isEqualTo(Status.INTERNAL_SERVER_ERROR);
			assertThat(problem.getDetail()).isEqualTo("Configuration error in service");

			verifyNoInteractions(oepIntegration, webMessageRepository);
		}
	}

	@Test
	void deleteWebMessageById() {

		// Setup
		final var id = UUID.randomUUID().toString();

		// Mock
		when(webMessageRepository.existsById(id)).thenReturn(true);

		// Call
		webMessageService.deleteWebMessageById(id);

		// Verification
		verify(webMessageRepository).existsById(id);
		verify(webMessageRepository).deleteById(id);
	}

	@Test
	void deleteWebMessageByIdNotFound() {

		// Setup
		final var id = UUID.randomUUID().toString();

		// Mock
		when(webMessageRepository.existsById(id)).thenReturn(false);

		// Call
		final var problem = assertThrows(ThrowableProblem.class, () -> webMessageService.deleteWebMessageById(id));

		// Verification
		assertThat(problem).isNotNull();
		assertThat(problem.getTitle()).isEqualTo(Status.NOT_FOUND.getReasonPhrase());
		assertThat(problem.getStatus()).isEqualTo(Status.NOT_FOUND);
		assertThat(problem.getDetail()).isEqualTo(format(ERROR_WEB_MESSAGE_NOT_FOUND, id));
		verify(webMessageRepository).existsById(id);
		verify(webMessageRepository, never()).deleteById(any());
	}

	@Test
	void getWebMessageById() {

		// Setup
		final var id = UUID.randomUUID().toString();

		// Mock
		when(webMessageRepository.findById(id)).thenReturn(Optional.of(WebMessageEntity.create().withId(id)));

		// Call
		final var result = webMessageService.getWebMessageById(id);

		// Verification
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(id);
		verify(webMessageRepository).findById(id);
	}

	@Test
	void getWebMessageByIdNotFound() {

		// Setup
		final var id = UUID.randomUUID().toString();

		// Mock
		when(webMessageRepository.findById(id)).thenReturn(Optional.empty());

		// Call
		final var problem = assertThrows(ThrowableProblem.class, () -> webMessageService.getWebMessageById(id));

		// Verification
		assertThat(problem).isNotNull();
		assertThat(problem.getTitle()).isEqualTo(Status.NOT_FOUND.getReasonPhrase());
		assertThat(problem.getStatus()).isEqualTo(Status.NOT_FOUND);
		assertThat(problem.getDetail()).isEqualTo(format(ERROR_WEB_MESSAGE_NOT_FOUND, id));
		verify(webMessageRepository).findById(id);
	}

	@Test
	void getWebMessagesByPartyId() {

		// Setup
		final var id = UUID.randomUUID().toString();
		final var partyId = UUID.randomUUID().toString();

		// Mock
		when(webMessageRepository.findByPartyIdOrderByCreated(partyId)).thenReturn(List.of(WebMessageEntity.create().withId(id).withPartyId(partyId)));

		// Call
		final var result = webMessageService.getWebMessagesByPartyId(partyId);

		// Verification
		assertThat(result).isNotNull().hasSize(1);
		assertThat(result.get(0).getId()).isEqualTo(id);
		assertThat(result.get(0).getPartyId()).isEqualTo(partyId);
		verify(webMessageRepository).findByPartyIdOrderByCreated(partyId);
	}

	@Test
	void getWebMessagesByExternalReference() {

		// Setup
		final var id = UUID.randomUUID().toString();
		final var partyId = UUID.randomUUID().toString();
		final var key = "key";
		final var value = "value";

		// Mock
		when(webMessageRepository.findByExternalReferencesKeyAndExternalReferencesValueOrderByCreated(key, value))
			.thenReturn(List.of(WebMessageEntity.create().withId(id).withPartyId(partyId)
				.withExternalReferences(List.of(ExternalReferenceEntity.create().withKey(key).withValue(value)))));

		// Call
		final var result = webMessageService.getWebMessagesByExternalReference(key, value);

		// Verification
		assertThat(result).isNotNull().hasSize(1);
		assertThat(result.get(0).getId()).isEqualTo(id);
		assertThat(result.get(0).getPartyId()).isEqualTo(partyId);
		assertThat(result.get(0).getExternalReferences()).extracting(ExternalReference::getKey).containsExactly(key);
		assertThat(result.get(0).getExternalReferences()).extracting(ExternalReference::getValue).containsExactly(value);
		verify(webMessageRepository).findByExternalReferencesKeyAndExternalReferencesValueOrderByCreated(key, value);
	}
}
