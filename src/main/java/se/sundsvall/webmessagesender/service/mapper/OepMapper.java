package se.sundsvall.webmessagesender.service.mapper;

import static java.util.Collections.emptyList;
import static javax.xml.datatype.DatatypeFactory.newInstance;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import javax.xml.datatype.DatatypeConfigurationException;
import se.sundsvall.webmessagesender.generatedsources.oep.AddMessage;
import se.sundsvall.webmessagesender.generatedsources.oep.Attachment;
import se.sundsvall.webmessagesender.generatedsources.oep.IntegrationMessage;

public final class OepMapper {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private OepMapper() {}

	public static AddMessage toAddMessage(String message, int flowInstanceId, List<se.sundsvall.webmessagesender.api.model.Attachment> attachments) throws DatatypeConfigurationException {
		final IntegrationMessage integrationMessage = new IntegrationMessage()
			.withAdded(newInstance().newXMLGregorianCalendar(new GregorianCalendar()))
			.withAttachments(toAttachments(attachments))
			.withMessage(message);

		return new AddMessage()
			.withFlowInstanceID(flowInstanceId)
			.withMessage(integrationMessage);
	}

	private static List<Attachment> toAttachments(List<se.sundsvall.webmessagesender.api.model.Attachment> attachments) {
		return Optional.ofNullable(attachments).orElse(emptyList()).stream()
			.map(OepMapper::toAttachment)
			.toList();
	}

	private static Attachment toAttachment(se.sundsvall.webmessagesender.api.model.Attachment attachment) {
		final byte[] encodedData = MAPPER.convertValue(attachment.getBase64Data(), byte[].class);

		return new Attachment()
			.withEncodedData(encodedData)
			.withFilename(attachment.getFileName())
			.withSize(encodedData.length);
	}
}
