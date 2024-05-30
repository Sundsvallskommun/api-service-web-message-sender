package se.sundsvall.webmessagesender.api;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;

import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.webmessagesender.api.model.CreateWebMessageRequest;
import se.sundsvall.webmessagesender.api.model.WebMessage;
import se.sundsvall.webmessagesender.service.WebMessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Validated
@Tag(name = "WebMessages", description = "Web messages")
@RequestMapping(
	path = "/webmessages",
	produces = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE}
)
@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = {Problem.class, ConstraintViolationProblem.class})))
@ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
@ApiResponse(responseCode = "502", description = "Bad gateway", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
class WebMessageResource {

	private final WebMessageService webMessageService;

	WebMessageResource(final WebMessageService webMessageService) {
		this.webMessageService = webMessageService;
	}

	@PostMapping(consumes = APPLICATION_JSON_VALUE)
	@Operation(summary = "Create web message")
	@ApiResponse(responseCode = "201", headers = @Header(name = LOCATION, schema = @Schema(type = "string")), description = "Successful operation", useReturnTypeSchema = true)
	public ResponseEntity<Void> createWebMessage(@Valid @RequestBody final CreateWebMessageRequest request) {

		return ResponseEntity
			.created(fromPath("/webmessages/{id}").buildAndExpand(webMessageService.createWebMessage(request).getId()).toUri())
			.header(CONTENT_TYPE, ALL_VALUE)
			.build();
	}

	@GetMapping(path = "/{id}")
	@Operation(summary = "Get web message by ID")
	@ApiResponse(responseCode = "200", description = "Successful operation", useReturnTypeSchema = true)
	public ResponseEntity<WebMessage> getWebMessageById(
		@Parameter(name = "id", description = "Web message ID", example = "81471222-5798-11e9-ae24-57fa13b361e1") @ValidUuid @PathVariable(name = "id", required = true) String id) {

		return ok(webMessageService.getWebMessageById(id));
	}

	@GetMapping(path = "/recipients/{partyId}")
	@Operation(summary = "Get web messages by partyId")
	@ApiResponse(responseCode = "200", description = "Successful operation", useReturnTypeSchema = true)
	public ResponseEntity<List<WebMessage>> getWebMessagesByPartyId(
		@Parameter(name = "partyId", description = "PartyID", example = "81471222-5798-11e9-ae24-57fa13b361e1") @PathVariable(value = "partyId", required = true) @ValidUuid String partyId) {

		return ok(webMessageService.getWebMessagesByPartyId(partyId));
	}

	@GetMapping(path = "/external-references/{key}/{value}")
	@Operation(summary = "Get web messages by externalReference")
	@ApiResponse(responseCode = "200", description = "Successful operation", useReturnTypeSchema = true)
	public ResponseEntity<List<WebMessage>> getWebMessagesByExternalReference(
		@Parameter(name = "key", description = "The external-reference key", example = "flowInstanceId") @PathVariable(value = "key", required = true) @Size(min = 3, max = 128) String key,
		@Parameter(name = "value", description = "The external-reference value", example = "356t4r34f") @PathVariable(value = "value", required = true) @Size(min = 3, max = 128) String value) {

		return ok(webMessageService.getWebMessagesByExternalReference(key, value));
	}

	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Delete web message by ID")
	@ApiResponse(responseCode = "204", description = "Successful operation", useReturnTypeSchema = true)
	public ResponseEntity<Void> deleteWebMessageById(
		@Parameter(name = "id", description = "Web message ID", example = "81471222-5798-11e9-ae24-57fa13b361e1") @ValidUuid @PathVariable String id) {

		webMessageService.deleteWebMessageById(id);
		return ResponseEntity.noContent().build();
	}
}
