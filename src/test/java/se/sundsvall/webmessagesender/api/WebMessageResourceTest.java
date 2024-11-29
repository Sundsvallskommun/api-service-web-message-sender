package se.sundsvall.webmessagesender.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.List;
import java.util.Map;
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
import se.sundsvall.webmessagesender.api.model.WebMessage;
import se.sundsvall.webmessagesender.service.WebMessageService;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class WebMessageResourceTest {

	@MockBean
	private WebMessageService webMessageService;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void createWebMessage() {

		// Mock
		final var id = UUID.randomUUID().toString();
		when(webMessageService.create(any(), any())).thenReturn(WebMessage.create().withId(id));

		// Parameter values
		final var municipalityId = "2281";
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withMessage("Test message")
			.withOepInstance("internal")
			.withExternalReferences(List.of(ExternalReference.create().withKey("key").withValue("value")))
			.withAttachments(List.of(Attachment.create().withFileName("fileName").withBase64Data("base64Data")))
			.withPartyId(UUID.randomUUID().toString());

		webTestClient.post()
			.uri(builder -> builder.path("/{municipalityId}/webmessages").build(Map.of("municipalityId", municipalityId)))
			.contentType(APPLICATION_JSON)
			.bodyValue(createWebMessageRequest)
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().location("/" + municipalityId + "/webmessages/" + id)
			.expectHeader().contentType(ALL_VALUE)
			.expectBody().isEmpty();

		// Verification
		verify(webMessageService).create(municipalityId, createWebMessageRequest);
	}

	@Test
	void createWebMessageWithoutPartyId() {

		// Parameter values
		final var municipalityId = "2281";
		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withMessage("Test message")
			.withOepInstance("external")
			.withExternalReferences(List.of(ExternalReference.create().withKey("key").withValue("value")))
			.withPartyId(null);

		// Mock
		final var id = UUID.randomUUID().toString();
		when(webMessageService.create(any(), any())).thenReturn(WebMessage.create().withId(id));

		webTestClient.post()
			.uri(builder -> builder.path("/{municipalityId}/webmessages").build(Map.of("municipalityId", municipalityId)))
			.contentType(APPLICATION_JSON)
			.bodyValue(createWebMessageRequest)
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().location("/" + municipalityId + "/webmessages/" + id)
			.expectHeader().contentType(ALL_VALUE)
			.expectBody().isEmpty();

		// Verification
		verify(webMessageService).create(municipalityId, createWebMessageRequest);
	}

	@Test
	void deleteWebMessageById() {

		// Parameter values
		final var municipalityId = "2281";
		final var id = "81471222-5798-11e9-ae24-57fa13b361e1";

		webTestClient.delete()
			.uri(builder -> builder.path("/{municipalityId}/webmessages/{id}").build(Map.of("municipalityId", municipalityId, "id", id)))
			.exchange()
			.expectStatus().isNoContent()
			.expectHeader().doesNotExist(CONTENT_TYPE)
			.expectBody().isEmpty();

		// Verification
		verify(webMessageService).deleteByMunicipalityIdAndId(municipalityId, id);
	}

	@Test
	void getWebMessageById() {

		// Mock
		when(webMessageService.getByMunicipalityIdAndId(any(), any())).thenReturn(WebMessage.create());

		// Parameter values
		final var municipalityId = "2281";
		final var id = UUID.randomUUID().toString();

		webTestClient.get()
			.uri(builder -> builder.path("/{municipalityId}/webmessages/{id}").build(Map.of("municipalityId", municipalityId, "id", id)))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody().jsonPath("$").isMap();

		// Verification
		verify(webMessageService).getByMunicipalityIdAndId(municipalityId, id);
	}

	@Test
	void getWebMessagesByPartyId() {

		// Mock
		when(webMessageService.getByMunicipalityIdAndPartyId(any(), any())).thenReturn(List.of(WebMessage.create()));

		// Parameter values
		final var municipalityId = "2281";
		final var partyId = UUID.randomUUID().toString();

		webTestClient.get()
			.uri(builder -> builder.path("/{municipalityId}/webmessages/recipients/{partyId}").build(Map.of("municipalityId", municipalityId, "partyId", partyId)))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBodyList(WebMessage.class).hasSize(1);

		// Verification
		verify(webMessageService).getByMunicipalityIdAndPartyId(municipalityId, partyId);
	}

	@Test
	void getWebMessagesByExternalReference() {

		// Mock
		when(webMessageService.getByMunicipalityIdAndExternalReference(any(), any(), any())).thenReturn(List.of(WebMessage.create()));

		// Parameter values
		final var municipalityId = "2281";
		final var key = "key";
		final var value = "value";

		webTestClient.get()
			.uri(builder -> builder.path("/{municipalityId}/webmessages/external-references/{key}/{value}").build(Map.of("municipalityId", municipalityId, "key", key, "value", value)))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBodyList(WebMessage.class).hasSize(1);

		// Verification
		verify(webMessageService).getByMunicipalityIdAndExternalReference(municipalityId, key, value);
	}
}
