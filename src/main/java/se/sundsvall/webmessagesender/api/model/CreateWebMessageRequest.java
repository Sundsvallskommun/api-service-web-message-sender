package se.sundsvall.webmessagesender.api.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.util.List;
import java.util.Objects;

import jakarta.validation.Valid;

import com.fasterxml.jackson.annotation.JsonInclude;

import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.webmessagesender.api.validation.ValidExternalReferences;
import se.sundsvall.webmessagesender.api.validation.ValidInstance;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(NON_NULL)
@Schema(description = "CreateWebMessageRequest model")
public class CreateWebMessageRequest {

	@Schema(description = "Party ID (e.g. a personId or an organizationId)", example = "81471222-5798-11e9-ae24-57fa13b361e1")
	@ValidUuid
	private String partyId;

	@Schema(description = "The message", example = "This is a message")
	private String message;

	@Schema(description = "Determines if the message should be added to the internal or external OeP instance", allowableValues = {
		"internal", "external"
	}, example = "internal")
	@ValidInstance
	private String oepInstance;

	@ArraySchema(schema = @Schema(implementation = ExternalReference.class))
	@ValidExternalReferences
	private List<ExternalReference> externalReferences;

	@ArraySchema(schema = @Schema(implementation = Attachment.class), maxItems = 10)
	@Valid
	private List<Attachment> attachments;

	public static CreateWebMessageRequest create() {
		return new CreateWebMessageRequest();
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public CreateWebMessageRequest withPartyId(String partyId) {
		this.partyId = partyId;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public CreateWebMessageRequest withMessage(String message) {
		this.message = message;
		return this;
	}

	public String getOepInstance() {
		return oepInstance;
	}

	public void setOepInstance(final String oepInstance) {
		this.oepInstance = oepInstance;
	}

	public CreateWebMessageRequest withOepInstance(final String oepInstance) {
		this.oepInstance = oepInstance;
		return this;
	}

	public List<ExternalReference> getExternalReferences() {
		return externalReferences;
	}

	public void setExternalReferences(List<ExternalReference> externalReferences) {
		this.externalReferences = externalReferences;
	}

	public CreateWebMessageRequest withExternalReferences(List<ExternalReference> externalReferences) {
		this.externalReferences = externalReferences;
		return this;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public CreateWebMessageRequest withAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
		return this;
	}

	@Override
	public String toString() {
		return "CreateWebMessageRequest{" +
			"partyId='" + partyId + '\'' +
			", message='" + message + '\'' +
			", oepInstance='" + oepInstance + '\'' +
			", externalReferences=" + externalReferences +
			", attachments=" + attachments +
			'}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final CreateWebMessageRequest that = (CreateWebMessageRequest) o;
		return Objects.equals(partyId, that.partyId) && Objects.equals(message, that.message) && Objects.equals(oepInstance, that.oepInstance) && Objects.equals(externalReferences, that.externalReferences) && Objects.equals(attachments, that.attachments);
	}

	@Override
	public int hashCode() {
		return Objects.hash(partyId, message, oepInstance, externalReferences, attachments);
	}
}
