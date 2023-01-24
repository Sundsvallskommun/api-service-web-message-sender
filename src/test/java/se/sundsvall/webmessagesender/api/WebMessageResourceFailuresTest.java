package se.sundsvall.webmessagesender.api;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import se.sundsvall.webmessagesender.Application;
import se.sundsvall.webmessagesender.api.model.Attachment;
import se.sundsvall.webmessagesender.api.model.CreateWebMessageRequest;
import se.sundsvall.webmessagesender.api.model.ExternalReference;
import se.sundsvall.webmessagesender.service.WebMessageService;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class WebMessageResourceFailuresTest {

	@MockBean
	private WebMessageService webMessageService;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void createWebMessageMissingPartyId() {

		// Parameter values
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withMessage("Test message")
			.withExternalReferences(List.of(ExternalReference.create().withKey("key").withValue("value")))
			.withPartyId(null); // Missing partyId

		webTestClient.post().uri("/webmessages/")
			.contentType(APPLICATION_JSON)
			.bodyValue(createWebMessageRequest)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("partyId")
			.jsonPath("$.violations[0].message").isEqualTo("not a valid UUID");

		// Verification
		verifyNoInteractions(webMessageService);
	}

	@Test
	void createWebMessageInvalidPartyId() {

		// Parameter values
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withMessage("Test message")
			.withExternalReferences(List.of(ExternalReference.create().withKey("key").withValue("value")))
			.withPartyId("invalid"); // Invalid partyId

		webTestClient.post().uri("/webmessages/")
			.contentType(APPLICATION_JSON)
			.bodyValue(createWebMessageRequest)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("partyId")
			.jsonPath("$.violations[0].message").isEqualTo("not a valid UUID");

		// Verification
		verifyNoInteractions(webMessageService);
	}

	@Test
	void createWebMessageMissingExternalReferences() {

		// Parameter values
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withMessage("Test message")
			.withExternalReferences(null) // Missing externalReferences
			.withPartyId(UUID.randomUUID().toString());

		webTestClient.post().uri("/webmessages/")
			.contentType(APPLICATION_JSON)
			.bodyValue(createWebMessageRequest)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("externalReferences")
			.jsonPath("$.violations[0].message").isEqualTo("can not be empty or contain elements with empty keys or values");

		// Verification
		verifyNoInteractions(webMessageService);
	}

	@Test
	void createWebMessageInvalidExternalReferences() {

		// Parameter values
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withMessage("Test message")
			.withExternalReferences(List.of(ExternalReference.create().withKey("key").withValue(" "))) // Invalid externalReferences
			.withPartyId(UUID.randomUUID().toString());

		webTestClient.post().uri("/webmessages/")
			.contentType(APPLICATION_JSON)
			.bodyValue(createWebMessageRequest)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("externalReferences")
			.jsonPath("$.violations[0].message").isEqualTo("can not be empty or contain elements with empty keys or values");

		// Verification
		verifyNoInteractions(webMessageService);
	}

	@Test
	void createWebMessageAttachmentMissingFileName() {

		// Parameter values
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withMessage("Test message")
			.withExternalReferences(List.of(ExternalReference.create().withKey("key").withValue("value")))
			.withAttachments(List.of(Attachment.create().withBase64Data("data")))
			.withPartyId(UUID.randomUUID().toString());

		webTestClient.post().uri("/webmessages/")
			.contentType(APPLICATION_JSON)
			.bodyValue(createWebMessageRequest)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("attachments[0].fileName")
			.jsonPath("$.violations[0].message").isEqualTo("must not be blank");

		// Verification
		verifyNoInteractions(webMessageService);
	}

	@Test
	void createWebMessageAttachmentBase64DataMissing() {

		// Parameter values
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withMessage("Test message")
			.withExternalReferences(List.of(ExternalReference.create().withKey("key").withValue("value")))
			.withAttachments(List.of(Attachment.create().withFileName("fileName")))
			.withPartyId(UUID.randomUUID().toString());

		webTestClient.post().uri("/webmessages/")
			.contentType(APPLICATION_JSON)
			.bodyValue(createWebMessageRequest)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("attachments[0].base64Data")
			.jsonPath("$.violations[0].message").isEqualTo("not a valid BASE64-encoded string");

		// Verification
		verifyNoInteractions(webMessageService);
	}
	
	@Test
	void createWebMessageAttachmentBase64DataToLarge() {

		// Parameter values
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withMessage("Test message")
			.withExternalReferences(List.of(ExternalReference.create().withKey("key").withValue("value")))
			.withAttachments(List.of(Attachment.create().withFileName("fileName").withBase64Data("to_large_base64_data")))
			.withPartyId(UUID.randomUUID().toString());

		webTestClient.post().uri("/webmessages/")
			.contentType(APPLICATION_JSON)
			.bodyValue(createWebMessageRequest)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("attachments[0].base64Data")
			.jsonPath("$.violations[0].message").isEqualTo("attachment exceeds the maximum allowed size of 10 bytes");

		// Verification
		verifyNoInteractions(webMessageService);
	}
	
	@Test
	void createWebMessageAttachmentBase64DataNotBase64Encoded() {

		// Parameter values
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withMessage("Test message")
			.withExternalReferences(List.of(ExternalReference.create().withKey("key").withValue("value")))
			.withAttachments(List.of(Attachment.create().withFileName("fileName").withBase64Data("not base64 åäö")))
			.withPartyId(UUID.randomUUID().toString());

		webTestClient.post().uri("/webmessages/")
			.contentType(APPLICATION_JSON)
			.bodyValue(createWebMessageRequest)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("attachments[0].base64Data")
			.jsonPath("$.violations[0].message").isEqualTo("not a valid BASE64-encoded string");

		// Verification
		verifyNoInteractions(webMessageService);
	}

	@Test
	void createWebMessageEmptyJsonBody() {

		// Parameter values
		final var createWebMessageRequest = "{}";

		webTestClient.post().uri("/webmessages/")
			.contentType(APPLICATION_JSON)
			.bodyValue(createWebMessageRequest)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("externalReferences")
			.jsonPath("$.violations[0].message").isEqualTo("can not be empty or contain elements with empty keys or values")
			.jsonPath("$.violations[1].field").isEqualTo("partyId")
			.jsonPath("$.violations[1].message").isEqualTo("not a valid UUID");

		// Verification
		verifyNoInteractions(webMessageService);
	}

	@Test
	void getWebMessageByIdInvalidId() {

		// Parameter values
		final var id = "invalid";

		webTestClient.get().uri("/webmessages/{id}", id)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("getWebMessageById.id")
			.jsonPath("$.violations[0].message").isEqualTo("not a valid UUID");

		// Verification
		verifyNoInteractions(webMessageService);
	}

	@Test
	void getWebMessagesByPartyIdInvalidPartyId() {

		// Parameter values
		final var partyId = "invalid";

		webTestClient.get().uri("/webmessages/recipients/{partyId}", partyId)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("getWebMessagesByPartyId.partyId")
			.jsonPath("$.violations[0].message").isEqualTo("not a valid UUID");

		// Verification
		verifyNoInteractions(webMessageService);
	}

	@Test
	void getWebMessagesByExternalReferenceInvalidKey() {

		// Parameter values
		final var key = "x";
		final var value = "value";

		webTestClient.get().uri("/webmessages/external-references/{key}/{value}", key, value)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("getWebMessagesByExternalReference.key")
			.jsonPath("$.violations[0].message").isEqualTo("size must be between 3 and 128");

		// Verification
		verifyNoInteractions(webMessageService);
	}

	@Test
	void getWebMessagesByExternalReferenceInvalidValue() {

		// Parameter values
		final var key = "flowInstanceId";
		final var value = "x";

		webTestClient.get().uri("/webmessages/external-references/{key}/{value}", key, value)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("getWebMessagesByExternalReference.value")
			.jsonPath("$.violations[0].message").isEqualTo("size must be between 3 and 128");

		// Verification
		verifyNoInteractions(webMessageService);
	}

	@Test
	void deleteWebMessageByIdInvalidId() {

		// Parameter values
		final var id = "Not valid";

		webTestClient.delete().uri("/webmessages/{id}", id)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("deleteWebMessageById.id")
			.jsonPath("$.violations[0].message").isEqualTo("not a valid UUID");

		// Verification
		verifyNoInteractions(webMessageService);
	}

	@Test
	void deleteWebMessageByIdEmptyId() {

		// Parameter values
		final var id = "";

		webTestClient.delete().uri("/webmessages/{id}", id)
			.exchange()
			.expectStatus().isEqualTo(METHOD_NOT_ALLOWED)
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody()
			.jsonPath("$.title").isEqualTo(METHOD_NOT_ALLOWED.getReasonPhrase())
			.jsonPath("$.status").isEqualTo(METHOD_NOT_ALLOWED.value())
			.jsonPath("$.detail").isEqualTo("Request method 'DELETE' not supported");

		// Verification
		verifyNoInteractions(webMessageService);
	}
}
