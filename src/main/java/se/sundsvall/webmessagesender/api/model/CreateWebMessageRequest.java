package se.sundsvall.webmessagesender.api.model;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.webmessagesender.api.validation.ValidExternalReferences;
import se.sundsvall.webmessagesender.api.validation.ValidInstance;

@Schema(description = "CreateWebMessageRequest model")
public class CreateWebMessageRequest {

	@Schema(description = "Party ID (e.g. a personId or an organizationId)", examples = "81471222-5798-11e9-ae24-57fa13b361e1", requiredMode = NOT_REQUIRED)
	@ValidUuid(nullable = true)
	private String partyId;

	@Schema(description = "The sender", requiredMode = NOT_REQUIRED)
	private Sender sender;

	@Schema(description = "The message", examples = "This is a message")
	private String message;

	@Schema(description = "Determines if the message should be added to the internal or external OeP instance", allowableValues = {
		"internal", "external"
	}, examples = "internal")
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

	public Sender getSender() {
		return sender;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	public CreateWebMessageRequest withSender(Sender sender) {
		this.sender = sender;
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
	public int hashCode() {
		return Objects.hash(attachments, externalReferences, message, oepInstance, partyId, sender);
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
		CreateWebMessageRequest other = (CreateWebMessageRequest) obj;
		return Objects.equals(attachments, other.attachments) && Objects.equals(externalReferences, other.externalReferences) && Objects.equals(message, other.message) && Objects.equals(oepInstance, other.oepInstance) && Objects.equals(partyId,
			other.partyId) && Objects.equals(sender, other.sender);
	}

	@Override
	public String toString() {
		return "CreateWebMessageRequest [partyId=" + partyId + ", sender=" + sender + ", message=" + message + ", oepInstance=" + oepInstance + ", externalReferences=" + externalReferences + ", attachments=" + attachments + "]";
	}
}
