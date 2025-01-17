package se.sundsvall.webmessagesender.service;

import static java.lang.String.format;
import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;
import static org.zalando.problem.Status.NOT_FOUND;
import static se.sundsvall.webmessagesender.integration.oep.mapper.OepSoapFaultMapper.convertToThrowableProblem;
import static se.sundsvall.webmessagesender.service.ServiceConstants.ERROR_WEB_MESSAGE_NOT_FOUND;
import static se.sundsvall.webmessagesender.service.ServiceConstants.REFERENCE_FLOW_INSTANCE_ID;
import static se.sundsvall.webmessagesender.service.mapper.OepMapper.toAddMessage;
import static se.sundsvall.webmessagesender.service.mapper.WebMessageMapper.toWebMessage;
import static se.sundsvall.webmessagesender.service.mapper.WebMessageMapper.toWebMessageEntity;
import static se.sundsvall.webmessagesender.service.mapper.WebMessageMapper.toWebMessages;

import feign.codec.DecodeException;
import jakarta.xml.ws.soap.SOAPFaultException;
import java.util.List;
import java.util.Optional;
import javax.xml.datatype.DatatypeConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import se.sundsvall.webmessagesender.api.model.CreateWebMessageRequest;
import se.sundsvall.webmessagesender.api.model.ExternalReference;
import se.sundsvall.webmessagesender.api.model.WebMessage;
import se.sundsvall.webmessagesender.integration.db.WebMessageRepository;
import se.sundsvall.webmessagesender.integration.oep.OepIntegration;

@Service
public class WebMessageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebMessageService.class);
	private final WebMessageRepository webMessageRepository;
	private final OepIntegration oepIntegration;

	public WebMessageService(WebMessageRepository webMessageRepository, OepIntegration oepIntegration) {
		this.webMessageRepository = webMessageRepository;
		this.oepIntegration = oepIntegration;
	}

	public WebMessage create(final String municipalityId, final CreateWebMessageRequest createWebMessageRequest) {
		final var oepMessageId = addOepMessage(createWebMessageRequest);
		return toWebMessage(webMessageRepository.save(toWebMessageEntity(municipalityId, createWebMessageRequest, oepMessageId)));
	}

	private Integer addOepMessage(CreateWebMessageRequest createWebMessageRequest) {
		try {
			final var flowInstanceid = retrieveFlowInstanceId(createWebMessageRequest);
			if (flowInstanceid.isPresent()) {
				return oepIntegration.addMessage(createWebMessageRequest.getOepInstance(), toAddMessage(createWebMessageRequest.getMessage(), flowInstanceid.get(), createWebMessageRequest.getAttachments()))
					.getMessageID();
			}
			return null;
		} catch (final DecodeException e) {
			if (e.getCause() instanceof final SOAPFaultException soapFaultException) {
				throw convertToThrowableProblem(soapFaultException);
			}
			throw e;
		} catch (final DatatypeConfigurationException e) {
			LOGGER.error("Could not obtain an instance of DatatypeFactory", e);
			throw Problem.valueOf(INTERNAL_SERVER_ERROR, "Configuration error in service");
		} catch (final SOAPFaultException soapFaultException) {
			throw convertToThrowableProblem(soapFaultException);
		}
	}

	private Optional<Integer> retrieveFlowInstanceId(CreateWebMessageRequest createWebMessageRequest) {
		return createWebMessageRequest.getExternalReferences().stream()
			.filter(reference -> REFERENCE_FLOW_INSTANCE_ID.equalsIgnoreCase(reference.getKey()))
			.map(ExternalReference::getValue)
			.map(Integer::parseInt)
			.findFirst();
	}

	public WebMessage getByMunicipalityIdAndId(final String municipalityId, final String id) {
		return toWebMessage(webMessageRepository.findByMunicipalityIdAndId(municipalityId, id).orElseThrow(() -> Problem.valueOf(NOT_FOUND, format(ERROR_WEB_MESSAGE_NOT_FOUND, id))));
	}

	public List<WebMessage> getByMunicipalityIdAndPartyId(final String municipalityId, final String partyId) {
		return toWebMessages(webMessageRepository.findByMunicipalityIdAndPartyIdOrderByCreated(municipalityId, partyId));
	}

	public List<WebMessage> getByMunicipalityIdAndExternalReference(final String municipalityId, final String key, final String value) {
		return toWebMessages(webMessageRepository.findByMunicipalityIdAndExternalReferencesKeyAndExternalReferencesValueOrderByCreated(municipalityId, key, value));
	}

	public void deleteByMunicipalityIdAndId(final String municipalityId, final String id) {

		final var entity = webMessageRepository.findByMunicipalityIdAndId(municipalityId, id)
			.orElseThrow(() -> Problem.valueOf(NOT_FOUND, format(ERROR_WEB_MESSAGE_NOT_FOUND, id)));

		webMessageRepository.delete(entity);
	}
}
