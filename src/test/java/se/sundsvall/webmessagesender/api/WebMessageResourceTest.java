package se.sundsvall.webmessagesender.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.sundsvall.webmessagesender.Application;
import se.sundsvall.webmessagesender.api.model.Attachment;
import se.sundsvall.webmessagesender.api.model.CreateWebMessageRequest;
import se.sundsvall.webmessagesender.api.model.ExternalReference;
import se.sundsvall.webmessagesender.api.model.WebMessage;
import se.sundsvall.webmessagesender.service.WebMessageService;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class WebMessageResourceTest {

	@MockBean
	private WebMessageService webMessageService;

	@Autowired
	private WebTestClient webTestClient;

	@LocalServerPort
	private int port;

	@Test
	void createWebMessage() {

		// Mock
		final var id = UUID.randomUUID().toString();
		when(webMessageService.createWebMessage(any())).thenReturn(WebMessage.create().withId(id));

		final var expectedLocationURL = "http://localhost:".concat(String.valueOf(port)).concat("/webmessages/").concat(id);

		// Parameter values
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withMessage("Test message")
			.withExternalReferences(List.of(ExternalReference.create().withKey("key").withValue("value")))
			.withAttachments(List.of(Attachment.create().withFileName("fileName").withBase64Data("base64Data")))
			.withPartyId(UUID.randomUUID().toString());

		webTestClient.post().uri("/webmessages")
			.contentType(APPLICATION_JSON)
			.bodyValue(createWebMessageRequest)
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().location(expectedLocationURL)
			.expectHeader().contentType(ALL_VALUE)
			.expectBody().isEmpty();

		// Verification
		verify(webMessageService).createWebMessage(createWebMessageRequest);
	}

	@Test
	void deleteWebMessageById() {

		// Parameter values
		final var id = "81471222-5798-11e9-ae24-57fa13b361e1";

		webTestClient.delete().uri("/webmessages/{id}", id)
			.exchange()
			.expectStatus().isNoContent()
			.expectHeader().doesNotExist(CONTENT_TYPE)
			.expectBody().isEmpty();

		// Verification
		verify(webMessageService).deleteWebMessageById(id);
	}

	@Test
	void getWebMessageById() {

		// Mock
		when(webMessageService.getWebMessageById(any())).thenReturn(WebMessage.create());

		// Parameter values
		final var id = UUID.randomUUID().toString();

		webTestClient.get().uri("/webmessages/{id}", id)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody().jsonPath("$").isMap();

		// Verification
		verify(webMessageService).getWebMessageById(id);
	}

	@Test
	void getWebMessagesByPartyId() {

		// Mock
		when(webMessageService.getWebMessagesByPartyId(any())).thenReturn(List.of(WebMessage.create()));

		// Parameter values
		final var partyId = UUID.randomUUID().toString();

		webTestClient.get().uri("/webmessages/recipients/{partyId}", partyId)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBodyList(WebMessage.class).hasSize(1);

		// Verification
		verify(webMessageService).getWebMessagesByPartyId(partyId);
	}

	@Test
	void getWebMessagesByExternalReference() {

		// Mock
		when(webMessageService.getWebMessagesByExternalReference(any(), any())).thenReturn(List.of(WebMessage.create()));

		// Parameter values
		final var key = "key";
		final var value = "value";

		webTestClient.get().uri("/webmessages/external-references/{key}/{value}", key, value)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBodyList(WebMessage.class).hasSize(1);

		// Verification
		verify(webMessageService).getWebMessagesByExternalReference(key, value);
	}
}
