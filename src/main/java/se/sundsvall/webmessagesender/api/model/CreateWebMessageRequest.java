package se.sundsvall.webmessagesender.api.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.webmessagesender.api.validation.ValidExternalReferences;

@JsonInclude(NON_NULL)
@Schema(description = "CreateWebMessageRequest model")
public class CreateWebMessageRequest {

	@Schema(description = "Party ID (e.g. a personId or an organizationId)", example = "81471222-5798-11e9-ae24-57fa13b361e1")
	@ValidUuid
	private String partyId;

	@Schema(description = "The message", example = "This is a message")
	private String message;

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
	public int hashCode() {
		return Objects.hash(externalReferences, message, partyId, attachments);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final CreateWebMessageRequest other = (CreateWebMessageRequest) obj;
		return Objects.equals(externalReferences, other.externalReferences)
			&& Objects.equals(message, other.message)
			&& Objects.equals(partyId, other.partyId)
			&& Objects.equals(attachments, other.attachments);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CreateWebMessageRequest [partyId=").append(partyId)
			.append(", message=").append(message)
			.append(", externalReferences=").append(externalReferences)
			.append(", attachments=").append(attachments)
			.append("]");
		return builder.toString();
	}
}
