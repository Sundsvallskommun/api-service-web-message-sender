package se.sundsvall.webmessagesender.api.model;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Schema(description = "WebMessage model", accessMode = READ_ONLY)
public class WebMessage {

	@Schema(description = "Web Message ID", examples = "f0882f1d-06bc-47fd-b017-1d8307f5ce95")
	private String id;

	@Schema(description = "Municipality ID", examples = "2281")
	private String municipalityId;

	@Schema(description = "The sender")
	private Sender sender;

	@Schema(description = "Party ID (e.g. a personId or an organizationId)", examples = "81471222-5798-11e9-ae24-57fa13b361e1")
	private String partyId;

	@Schema(description = "The message", examples = "This is a message")
	private String message;

	@Schema(description = "The oep instance", examples = "internal")
	private String oepInstance;

	@ArraySchema(schema = @Schema(implementation = ExternalReference.class))
	private List<ExternalReference> externalReferences;

	@Schema(description = "Created timestamp")
	@DateTimeFormat(iso = DATE_TIME)
	private OffsetDateTime created;

	@ArraySchema(schema = @Schema(implementation = Attachment.class))
	private List<Attachment> attachments;

	public static WebMessage create() {
		return new WebMessage();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public WebMessage withId(String id) {
		this.id = id;
		return this;
	}

	public String getMunicipalityId() {
		return municipalityId;
	}

	public void setMunicipalityId(String municipalityId) {
		this.municipalityId = municipalityId;
	}

	public WebMessage withMunicipalityId(String municipalityId) {
		this.municipalityId = municipalityId;
		return this;
	}

	public Sender getSender() {
		return sender;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	public WebMessage withSender(Sender sender) {
		this.sender = sender;
		return this;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public WebMessage withPartyId(String partyId) {
		this.partyId = partyId;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public WebMessage withMessage(String message) {
		this.message = message;
		return this;
	}

	public String getOepInstance() {
		return oepInstance;
	}

	public void setOepInstance(final String oepInstance) {
		this.oepInstance = oepInstance;
	}

	public WebMessage withOepInstance(final String oepInstance) {
		this.oepInstance = oepInstance;
		return this;
	}

	public List<ExternalReference> getExternalReferences() {
		return externalReferences;
	}

	public void setExternalReferences(List<ExternalReference> externalReferences) {
		this.externalReferences = externalReferences;
	}

	public WebMessage withExternalReferences(List<ExternalReference> externalReferences) {
		this.externalReferences = externalReferences;
		return this;
	}

	public OffsetDateTime getCreated() {
		return created;
	}

	public void setCreated(OffsetDateTime created) {
		this.created = created;
	}

	public WebMessage withCreated(OffsetDateTime created) {
		this.created = created;
		return this;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public WebMessage withAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(attachments, created, externalReferences, id, message, municipalityId, oepInstance, partyId, sender);
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
		WebMessage other = (WebMessage) obj;
		return Objects.equals(attachments, other.attachments) && Objects.equals(created, other.created) && Objects.equals(externalReferences, other.externalReferences) && Objects.equals(id, other.id) && Objects.equals(message, other.message) && Objects
			.equals(municipalityId, other.municipalityId) && Objects.equals(oepInstance, other.oepInstance) && Objects.equals(partyId, other.partyId) && Objects.equals(sender, other.sender);
	}

	@Override
	public String toString() {
		return "WebMessage [id=" + id + ", municipalityId=" + municipalityId + ", sender=" + sender + ", partyId=" + partyId + ", message=" + message + ", oepInstance=" + oepInstance + ", externalReferences=" + externalReferences + ", created=" + created
			+ ", attachments=" + attachments + "]";
	}
}
