package se.sundsvall.webmessagesender.service.mapper;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import se.sundsvall.webmessagesender.api.model.Attachment;
import se.sundsvall.webmessagesender.api.model.CreateWebMessageRequest;
import se.sundsvall.webmessagesender.api.model.ExternalReference;
import se.sundsvall.webmessagesender.api.model.Sender;
import se.sundsvall.webmessagesender.integration.db.model.AttachmentEntity;
import se.sundsvall.webmessagesender.integration.db.model.ExternalReferenceEntity;
import se.sundsvall.webmessagesender.integration.db.model.WebMessageEntity;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64;
import static org.assertj.core.api.Assertions.assertThat;

class WebMessageMapperTest {

	@Test
	void toWebMessageEntityFromCreateWebMessageRequestWithoutOepMessageId() {

		// Setup
		final var municipalityId = "municipalityId";
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withExternalReferences(List.of(
				ExternalReference.create().withKey("key-1").withValue("value-1"),
				ExternalReference.create().withKey("key-2").withValue("value-2"),
				ExternalReference.create().withKey("key-2").withValue("value-2"),
				ExternalReference.create().withKey("key-3").withValue("value-3")))
			.withMessage("A message")
			.withPartyId(UUID.randomUUID().toString())
			.withSender(Sender.create().withUserId("senderUserId"));

		// Call
		final var webMessageEntity = WebMessageMapper.toWebMessageEntity(municipalityId, createWebMessageRequest, null);

		// Verification
		assertThat(webMessageEntity.getExternalReferences()).hasSize(3); // Duplicates removed.
		assertThat(webMessageEntity.getExternalReferences()).extracting(ExternalReferenceEntity::getKey).containsExactly("key-1", "key-2", "key-3");
		assertThat(webMessageEntity.getExternalReferences()).extracting(ExternalReferenceEntity::getValue).containsExactly("value-1", "value-2", "value-3");
		assertThat(webMessageEntity.getMessage()).isEqualTo(createWebMessageRequest.getMessage());
		assertThat(webMessageEntity.getMunicipalityId()).isEqualTo(municipalityId);
		assertThat(webMessageEntity.getPartyId()).isEqualTo(createWebMessageRequest.getPartyId());
		assertThat(webMessageEntity.getSenderUserId()).isEqualTo(createWebMessageRequest.getSender().getUserId());
		assertThat(webMessageEntity.getOepMessageId()).isNull();
	}

	@Test
	void toWebMessageEntityFromCreateWebMessageRequestWithOepMessageId() {

		// Setup
		final var municipalityId = "municipalityId";
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withExternalReferences(List.of(
				ExternalReference.create().withKey("key-1").withValue("value-1"),
				ExternalReference.create().withKey("key-2").withValue("value-2"),
				ExternalReference.create().withKey("key-2").withValue("value-2"),
				ExternalReference.create().withKey("key-3").withValue("value-3")))
			.withMessage("A message")
			.withPartyId(UUID.randomUUID().toString())
			.withSender(Sender.create().withUserId("senderUserId"));

		// Call
		final var webMessageEntity = WebMessageMapper.toWebMessageEntity(municipalityId, createWebMessageRequest, Integer.MAX_VALUE);

		// Verification
		assertThat(webMessageEntity.getExternalReferences()).hasSize(3); // Duplicates removed.
		assertThat(webMessageEntity.getExternalReferences()).extracting(ExternalReferenceEntity::getKey).containsExactly("key-1", "key-2", "key-3");
		assertThat(webMessageEntity.getExternalReferences()).extracting(ExternalReferenceEntity::getValue).containsExactly("value-1", "value-2", "value-3");
		assertThat(webMessageEntity.getMessage()).isEqualTo(createWebMessageRequest.getMessage());
		assertThat(webMessageEntity.getMunicipalityId()).isEqualTo(municipalityId);
		assertThat(webMessageEntity.getPartyId()).isEqualTo(createWebMessageRequest.getPartyId());
		assertThat(webMessageEntity.getSenderUserId()).isEqualTo(createWebMessageRequest.getSender().getUserId());
		assertThat(webMessageEntity.getOepMessageId()).isEqualTo(Integer.MAX_VALUE);
	}

	@Test
	void toWebMessageEntityFromCreateWebMessageRequestWithOepMessageIdAndAttachements() {

		// Setup
		final var data_1 = new String(encodeBase64("bytes-1".getBytes()));
		final var data_2 = new String(encodeBase64("bytes-2".getBytes()));
		final var municipalityId = "municipalityId";
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withAttachments(List.of(
				Attachment.create().withBase64Data(data_1).withFileName("file-1.txt"),
				Attachment.create().withBase64Data(data_2).withFileName("file-2.txt")))
			.withExternalReferences(List.of(
				ExternalReference.create().withKey("key-1").withValue("value-1"),
				ExternalReference.create().withKey("key-2").withValue("value-2"),
				ExternalReference.create().withKey("key-2").withValue("value-2"),
				ExternalReference.create().withKey("key-3").withValue("value-3")))
			.withMessage("A message")
			.withPartyId(UUID.randomUUID().toString())
			.withSender(Sender.create().withUserId("senderUserId"));

		// Call
		final var webMessageEntity = WebMessageMapper.toWebMessageEntity(municipalityId, createWebMessageRequest, Integer.MAX_VALUE);

		// Verification
		assertThat(webMessageEntity.getAttachments()).hasSize(2)
			.extracting(
				AttachmentEntity::getFileName, AttachmentEntity::getFile, AttachmentEntity::getMimeType)
			.containsExactly(
				Tuple.tuple("file-1.txt", decodeBase64(data_1), "text/plain"),
				Tuple.tuple("file-2.txt", decodeBase64(data_2), "text/plain"));
		assertThat(webMessageEntity.getExternalReferences()).hasSize(3); // Duplicates removed.
		assertThat(webMessageEntity.getExternalReferences()).extracting(ExternalReferenceEntity::getKey).containsExactly("key-1", "key-2", "key-3");
		assertThat(webMessageEntity.getExternalReferences()).extracting(ExternalReferenceEntity::getValue).containsExactly("value-1", "value-2", "value-3");
		assertThat(webMessageEntity.getMessage()).isEqualTo(createWebMessageRequest.getMessage());
		assertThat(webMessageEntity.getMunicipalityId()).isEqualTo(municipalityId);
		assertThat(webMessageEntity.getPartyId()).isEqualTo(createWebMessageRequest.getPartyId());
		assertThat(webMessageEntity.getSenderUserId()).isEqualTo(createWebMessageRequest.getSender().getUserId());
		assertThat(webMessageEntity.getOepMessageId()).isEqualTo(Integer.MAX_VALUE);
	}

	@Test
	void toWebMessageEntityFromNull() {

		// Call
		final var webMessageEntity = WebMessageMapper.toWebMessageEntity(null, null, null);

		// Verification
		assertThat(webMessageEntity).isNull();
	}

	@Test
	void toWebMessageFromWebMessageEntity() {

		// Setup
		final var fileData1 = "bytes-1".getBytes();
		final var fileData2 = "bytes-2".getBytes();
		final var municipalityId = "municipalityId";
		final var webMessageEntity = WebMessageEntity.create()
			.withAttachments(List.of(
				AttachmentEntity.create().withFile(fileData1).withFileName("file-1.txt").withId(1).withMimeType("text/plain"),
				AttachmentEntity.create().withFile(fileData2).withFileName("file-2.txt").withId(2).withMimeType("text/plain")))
			.withCreated(OffsetDateTime.now())
			.withExternalReferences(List.of(
				ExternalReferenceEntity.create().withKey("key-1").withValue("value-1"),
				ExternalReferenceEntity.create().withKey("key-2").withValue("value-2"),
				ExternalReferenceEntity.create().withKey("key-2").withValue("value-2"),
				ExternalReferenceEntity.create().withKey("key-3").withValue("value-3")))
			.withId(UUID.randomUUID().toString())
			.withMessage("A message")
			.withMunicipalityId(municipalityId)
			.withPartyId(UUID.randomUUID().toString())
			.withSenderUserId("senderUserId");

		// Call toWebMessage
		final var webMessage = WebMessageMapper.toWebMessage(webMessageEntity);

		// Verification
		assertThat(webMessage.getAttachments()).hasSize(2)
			.extracting(Attachment::getFileName, Attachment::getBase64Data, Attachment::getMimeType)
			.containsExactly(
				Tuple.tuple("file-1.txt", new String(encodeBase64(fileData1)), "text/plain"),
				Tuple.tuple("file-2.txt", new String(encodeBase64(fileData2)), "text/plain"));
		assertThat(webMessage.getCreated()).isEqualTo(webMessageEntity.getCreated());
		assertThat(webMessage.getExternalReferences()).hasSize(4);
		assertThat(webMessage.getExternalReferences()).extracting(ExternalReference::getKey).containsExactly("key-1", "key-2", "key-2", "key-3");
		assertThat(webMessage.getExternalReferences()).extracting(ExternalReference::getValue).containsExactly("value-1", "value-2", "value-2", "value-3");
		assertThat(webMessage.getId()).isEqualTo(webMessageEntity.getId());
		assertThat(webMessage.getMessage()).isEqualTo(webMessageEntity.getMessage());
		assertThat(webMessage.getMunicipalityId()).isEqualTo(webMessageEntity.getMunicipalityId());
		assertThat(webMessage.getPartyId()).isEqualTo(webMessageEntity.getPartyId());
		assertThat(webMessage.getSender().getUserId()).isEqualTo(webMessageEntity.getSenderUserId());

		// Call toWebMessages (reusing same webMessageEntity used in test of method toWebMessage)for verification of list of
		// messages
		assertThat(WebMessageMapper.toWebMessages(List.of(webMessageEntity)))
			.satisfiesExactly(message -> {
				assertThat(message.getAttachments()).hasSize(2)
					.extracting(Attachment::getFileName, Attachment::getBase64Data, Attachment::getMimeType)
					.containsExactly(
						Tuple.tuple("file-1.txt", new String(encodeBase64(fileData1)), "text/plain"),
						Tuple.tuple("file-2.txt", new String(encodeBase64(fileData2)), "text/plain"));
				assertThat(message.getCreated()).isEqualTo(webMessageEntity.getCreated());
				assertThat(message.getCreated()).isEqualTo(webMessageEntity.getCreated());
				assertThat(message.getExternalReferences()).hasSize(4);
				assertThat(message.getExternalReferences()).extracting(ExternalReference::getKey).containsExactly("key-1", "key-2", "key-2", "key-3");
				assertThat(message.getExternalReferences()).extracting(ExternalReference::getValue).containsExactly("value-1", "value-2", "value-2", "value-3");
				assertThat(message.getId()).isEqualTo(webMessageEntity.getId());
				assertThat(message.getMessage()).isEqualTo(webMessageEntity.getMessage());
				assertThat(message.getMunicipalityId()).isEqualTo(webMessageEntity.getMunicipalityId());
				assertThat(message.getPartyId()).isEqualTo(webMessageEntity.getPartyId());
				assertThat(message.getSender().getUserId()).isEqualTo(webMessageEntity.getSenderUserId());
			});
	}

	@Test
	void toWebMessageFromNull() {

		// Call
		final var webMessageEntity = WebMessageMapper.toWebMessage(null);

		// Verification
		assertThat(webMessageEntity).isNull();
	}
}
