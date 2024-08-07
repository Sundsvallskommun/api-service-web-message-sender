package se.sundsvall.webmessagesender.api.model;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "WebMessage model")
public class WebMessage {

	@Schema(description = "Web Message ID", example = "f0882f1d-06bc-47fd-b017-1d8307f5ce95")
	private String id;

	@Schema(description = "Municipality ID", example = "2281", accessMode = READ_ONLY)
	private String municipalityId;

	@Schema(description = "Party ID (e.g. a personId or an organizationId)", example = "81471222-5798-11e9-ae24-57fa13b361e1")
	private String partyId;

	@Schema(description = "The message", example = "This is a message")
	private String message;

	@Schema(description = "The oep instance", example = "internal")
	private String oepInstance;

	@ArraySchema(schema = @Schema(implementation = ExternalReference.class))
	private List<ExternalReference> externalReferences;

	@Schema(description = "Created timestamp", accessMode = READ_ONLY)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
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
		return Objects.hash(attachments, created, externalReferences, id, message, municipalityId, oepInstance, partyId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (!(obj instanceof final WebMessage other)) { return false; }
		return Objects.equals(attachments, other.attachments) && Objects.equals(created, other.created) && Objects.equals(externalReferences, other.externalReferences) && Objects.equals(id, other.id) && Objects.equals(message, other.message) && Objects
			.equals(municipalityId, other.municipalityId) && Objects.equals(oepInstance, other.oepInstance) && Objects.equals(partyId, other.partyId);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("WebMessage [id=").append(id).append(", municipalityId=").append(municipalityId).append(", partyId=").append(partyId).append(", message=").append(message).append(", oepInstance=").append(oepInstance).append(", externalReferences=")
			.append(externalReferences).append(", created=").append(created).append(", attachments=").append(attachments).append("]");
		return builder.toString();
	}
}
