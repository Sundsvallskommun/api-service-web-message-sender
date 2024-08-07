package se.sundsvall.webmessagesender.service;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
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

import javax.xml.datatype.DatatypeConfigurationException;

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

import jakarta.xml.soap.Detail;
import jakarta.xml.soap.SOAPFault;
import jakarta.xml.ws.soap.SOAPFaultException;
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
	void createWithoutFlowInstanceId() {

		// Setup
		final var municipalityId = "municipalityId";
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withExternalReferences(List.of(ExternalReference.create().withKey("key").withValue("value")))
			.withMessage("Message")
			.withPartyId(randomUUID().toString());

		// Mock
		final var webMessageEntity = toWebMessageEntity(municipalityId, createWebMessageRequest, null);
		when(webMessageRepository.save(any())).thenReturn(webMessageEntity);

		// Call
		final var result = webMessageService.create(municipalityId, createWebMessageRequest);

		assertThat(result).isNotNull();

		// Verification
		verify(webMessageRepository).save(webMessageEntity);
		verifyNoMoreInteractions(webMessageRepository);
	}

	@ParameterizedTest
	@ValueSource(strings = { "flowInstanceId", "flowinstanceid", "FLOWINSTANCEID" })
	void createWithFlowInstanceId(String key) {

		// Setup
		final var value = "123456";
		final var partyId = randomUUID().toString();
		final var message = "Message";
		final var oepMessageId = Integer.MAX_VALUE;
		final var municipalityId = "municipalityId";

		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withOepInstance("internal")
			.withExternalReferences(List.of(ExternalReference.create().withKey(key).withValue(value)))
			.withMessage(message)
			.withPartyId(partyId);

		final var webMessageEntity = toWebMessageEntity(municipalityId, createWebMessageRequest, oepMessageId);

		// Mock
		when(oepIntegration.addMessage(eq("internal"), any(AddMessage.class))).thenReturn(addMessageResponse);
		when(addMessageResponse.getMessageID()).thenReturn(oepMessageId);
		when(webMessageRepository.save(any())).thenReturn(webMessageEntity);

		// Call
		final var result = webMessageService.create(municipalityId, createWebMessageRequest);

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
		assertThat(webMessageEntityCaptor.getValue().getMunicipalityId()).isEqualTo(municipalityId);
		assertThat(webMessageEntityCaptor.getValue().getExternalReferences())
			.hasSize(1)
			.extracting(ExternalReferenceEntity::getId, ExternalReferenceEntity::getKey, ExternalReferenceEntity::getValue)
			.containsExactly(tuple(0L, key, value));
	}

	@Test
	void createThrowsSOAPFaultException() {

		// Setup
		final var municipalityId = "municipalityId";
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("123456")))
			.withMessage("Message")
			.withPartyId(randomUUID().toString());

		// Mock throw from OEP integration
		when(oepIntegration.addMessage(any(), any())).thenThrow(soapFaultException);
		when(soapFaultException.getFault()).thenReturn(soapFault);
		when(soapFault.getFaultString()).thenReturn("Bogus error");
		when(soapFault.getDetail()).thenReturn(detail);
		when(detail.getDetailEntries()).thenReturn(Collections.emptyIterator());

		// Call
		final var problem = assertThrows(ThrowableProblem.class, () -> webMessageService.create(municipalityId, createWebMessageRequest));

		// Verification
		assertThat(problem).isNotNull();
		assertThat(problem.getTitle()).isEqualTo(Status.INTERNAL_SERVER_ERROR.getReasonPhrase());
		assertThat(problem.getStatus()).isEqualTo(Status.INTERNAL_SERVER_ERROR);
		assertThat(problem.getDetail()).isEqualTo("Bogus error");

		verify(oepIntegration).addMessage(any(), any());
		verifyNoInteractions(webMessageRepository);
	}

	@Test
	void createThrowsDatatypeConfigurationException() {

		// Setup
		final var municipalityId = "municipalityId";
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withExternalReferences(List.of(ExternalReference.create().withKey("flowInstanceId").withValue("123456")))
			.withMessage("Message")
			.withPartyId(randomUUID().toString());

		// Mock throw from static method and test
		try (MockedStatic<OepMapper> mockMapper = mockStatic(OepMapper.class)) {
			mockMapper.when(() -> toAddMessage(any(), anyInt(), any())).thenThrow(new DatatypeConfigurationException());

			// Call
			final var problem = assertThrows(ThrowableProblem.class, () -> webMessageService.create(municipalityId, createWebMessageRequest));

			// Verification
			assertThat(problem).isNotNull();
			assertThat(problem.getTitle()).isEqualTo(Status.INTERNAL_SERVER_ERROR.getReasonPhrase());
			assertThat(problem.getStatus()).isEqualTo(Status.INTERNAL_SERVER_ERROR);
			assertThat(problem.getDetail()).isEqualTo("Configuration error in service");

			verifyNoInteractions(oepIntegration, webMessageRepository);
		}
	}

	@Test
	void deleteByMunicipalityIdAndId() {

		// Setup
		final var municipalityId = "municipalityId";
		final var id = randomUUID().toString();
		final var entity = WebMessageEntity.create();

		// Mock
		when(webMessageRepository.findByMunicipalityIdAndId(municipalityId, id)).thenReturn(Optional.of(entity));

		// Call
		webMessageService.deleteByMunicipalityIdAndId(municipalityId, id);

		// Verification
		verify(webMessageRepository).findByMunicipalityIdAndId(municipalityId, id);
		verify(webMessageRepository).delete(entity);
	}

	@Test
	void deleteByMunicipalityIdAndIdNotFound() {

		// Setup
		final var municipalityId = "municipalityId";
		final var id = randomUUID().toString();

		// Mock
		when(webMessageRepository.findByMunicipalityIdAndId(municipalityId, id)).thenReturn(empty());

		// Call
		final var problem = assertThrows(ThrowableProblem.class, () -> webMessageService.deleteByMunicipalityIdAndId(municipalityId, id));

		// Verification
		assertThat(problem).isNotNull();
		assertThat(problem.getTitle()).isEqualTo(Status.NOT_FOUND.getReasonPhrase());
		assertThat(problem.getStatus()).isEqualTo(Status.NOT_FOUND);
		assertThat(problem.getDetail()).isEqualTo(format(ERROR_WEB_MESSAGE_NOT_FOUND, id));

		verify(webMessageRepository).findByMunicipalityIdAndId(municipalityId, id);
		verifyNoMoreInteractions(webMessageRepository);
	}

	@Test
	void getByMunicipalityIdAndId() {

		// Setup
		final var municipalityId = "municipalityId";
		final var id = randomUUID().toString();

		// Mock
		when(webMessageRepository.findByMunicipalityIdAndId(municipalityId, id)).thenReturn(Optional.of(WebMessageEntity.create().withId(id)));

		// Call
		final var result = webMessageService.getByMunicipalityIdAndId(municipalityId, id);

		// Verification
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(id);

		verify(webMessageRepository).findByMunicipalityIdAndId(municipalityId, id);
	}

	@Test
	void getWebMessageByMunicipalityIdAndIdNotFound() {

		// Setup
		final var municipalityId = "municipalityId";
		final var id = randomUUID().toString();

		// Mock
		when(webMessageRepository.findByMunicipalityIdAndId(municipalityId, id)).thenReturn(Optional.empty());

		// Call
		final var problem = assertThrows(ThrowableProblem.class, () -> webMessageService.getByMunicipalityIdAndId(municipalityId, id));

		// Verification
		assertThat(problem).isNotNull();
		assertThat(problem.getTitle()).isEqualTo(Status.NOT_FOUND.getReasonPhrase());
		assertThat(problem.getStatus()).isEqualTo(Status.NOT_FOUND);
		assertThat(problem.getDetail()).isEqualTo(format(ERROR_WEB_MESSAGE_NOT_FOUND, id));

		verify(webMessageRepository).findByMunicipalityIdAndId(municipalityId, id);
	}

	@Test
	void getWebMessagesByPartyId() {

		// Setup
		final var municipalityId = "municipalityId";
		final var id = randomUUID().toString();
		final var partyId = randomUUID().toString();

		// Mock
		when(webMessageRepository.findByMunicipalityIdAndPartyIdOrderByCreated(municipalityId, partyId)).thenReturn(List.of(WebMessageEntity.create().withId(id).withPartyId(partyId)));

		// Call
		final var result = webMessageService.getByMunicipalityIdAndPartyId(municipalityId, partyId);

		// Verification
		assertThat(result).isNotNull().hasSize(1);
		assertThat(result.get(0).getId()).isEqualTo(id);
		assertThat(result.get(0).getPartyId()).isEqualTo(partyId);

		verify(webMessageRepository).findByMunicipalityIdAndPartyIdOrderByCreated(municipalityId, partyId);
	}

	@Test
	void getWebMessagesByMunicipalityIdAndExternalReference() {

		// Setup
		final var municipalityId = "municipalityId";
		final var id = randomUUID().toString();
		final var partyId = randomUUID().toString();
		final var key = "key";
		final var value = "value";

		// Mock
		when(webMessageRepository.findByMunicipalityIdAndExternalReferencesKeyAndExternalReferencesValueOrderByCreated(municipalityId, key, value))
			.thenReturn(List.of(WebMessageEntity.create().withId(id).withPartyId(partyId)
				.withExternalReferences(List.of(ExternalReferenceEntity.create().withKey(key).withValue(value)))));

		// Call
		final var result = webMessageService.getByMunicipalityIdAndExternalReference(municipalityId, key, value);

		// Verification
		assertThat(result).isNotNull().hasSize(1);
		assertThat(result.get(0).getId()).isEqualTo(id);
		assertThat(result.get(0).getPartyId()).isEqualTo(partyId);
		assertThat(result.get(0).getExternalReferences()).extracting(ExternalReference::getKey).containsExactly(key);
		assertThat(result.get(0).getExternalReferences()).extracting(ExternalReference::getValue).containsExactly(value);

		verify(webMessageRepository).findByMunicipalityIdAndExternalReferencesKeyAndExternalReferencesValueOrderByCreated(municipalityId, key, value);
	}
}
