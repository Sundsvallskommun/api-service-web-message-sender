package se.sundsvall.webmessagesender.service.mapper;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64;
import static se.sundsvall.webmessagesender.service.mapper.MimeTypeUtility.detectMimeType;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import se.sundsvall.webmessagesender.api.model.Attachment;
import se.sundsvall.webmessagesender.api.model.CreateWebMessageRequest;
import se.sundsvall.webmessagesender.api.model.ExternalReference;
import se.sundsvall.webmessagesender.api.model.WebMessage;
import se.sundsvall.webmessagesender.integration.db.model.AttachmentEntity;
import se.sundsvall.webmessagesender.integration.db.model.ExternalReferenceEntity;
import se.sundsvall.webmessagesender.integration.db.model.WebMessageEntity;

public class WebMessageMapper {

	private WebMessageMapper() {}

	public static WebMessageEntity toWebMessageEntity(final CreateWebMessageRequest createWebMessageRequest, final Integer oepMessageId) {
		if (isNull(createWebMessageRequest)) {
			return null;
		}
		return WebMessageEntity.create()
			.withAttachments(toAttachmentEntities(createWebMessageRequest.getAttachments()))
			.withExternalReferences(toExternalReferenceEntities(createWebMessageRequest.getExternalReferences()))
			.withMessage(createWebMessageRequest.getMessage())
			.withOepMessageId(oepMessageId)
			.withPartyId(createWebMessageRequest.getPartyId());
	}

	public static WebMessage toWebMessage(final WebMessageEntity webMessageEntity) {
		if (isNull(webMessageEntity)) {
			return null;
		}
		return WebMessage.create()
			.withAttachments(toAttachments(webMessageEntity.getAttachments()))
			.withCreated(webMessageEntity.getCreated())
			.withExternalReferences(toExternalReferences(webMessageEntity.getExternalReferences()))
			.withId(webMessageEntity.getId())
			.withMessage(webMessageEntity.getMessage())
			.withPartyId(webMessageEntity.getPartyId());
	}

	public static List<WebMessage> toWebMessages(final List<WebMessageEntity> webMessageEntities) {
		return Optional.ofNullable(webMessageEntities).orElse(emptyList()).stream()
			.map(WebMessageMapper::toWebMessage)
			.toList();
	}

	private static List<AttachmentEntity> toAttachmentEntities(List<Attachment> attachments) {
		return Optional.ofNullable(attachments).orElse(emptyList()).stream()
			.map(WebMessageMapper::toAttachmentEntity)
			.toList();
	}

	private static AttachmentEntity toAttachmentEntity(Attachment attachment) {
		byte[] byteArray = decodeBase64(attachment.getBase64Data());
		
		return AttachmentEntity.create()
			.withFile(byteArray)
			.withFileName(attachment.getFileName())
			.withMimeType(detectMimeType(attachment.getFileName(), byteArray));
	}
	
	private static List<ExternalReferenceEntity> toExternalReferenceEntities(List<ExternalReference> externalReferences) {
		return Optional.ofNullable(externalReferences).orElse(emptyList()).stream()
			.distinct() // Remove duplicates
			.map(WebMessageMapper::toExternalReferenceEntity)
			.toList();
	}

	private static ExternalReferenceEntity toExternalReferenceEntity(ExternalReference externalReference) {
		return ExternalReferenceEntity.create()
			.withKey(externalReference.getKey())
			.withValue(externalReference.getValue());
	}

	private static List<Attachment> toAttachments(List<AttachmentEntity> attachmentEntities) {
		return Optional.ofNullable(attachmentEntities).orElse(emptyList()).stream()
			.map(WebMessageMapper::toAttachment)
			.toList();
	}

	private static Attachment toAttachment(AttachmentEntity attachmentEntity) {
		return Attachment.create()
			.withBase64Data(new String(encodeBase64(attachmentEntity.getFile()), StandardCharsets.UTF_8))
			.withFileName(attachmentEntity.getFileName())
			.withMimeType(attachmentEntity.getMimeType());
	}

	private static List<ExternalReference> toExternalReferences(List<ExternalReferenceEntity> externalReferenceEntities) {
		return Optional.ofNullable(externalReferenceEntities).orElse(emptyList()).stream()
			.map(WebMessageMapper::toExternalReference)
			.toList();
	}

	private static ExternalReference toExternalReference(ExternalReferenceEntity externalReferenceEntity) {
		return ExternalReference.create()
			.withKey(externalReferenceEntity.getKey())
			.withValue(externalReferenceEntity.getValue());
	}
}
