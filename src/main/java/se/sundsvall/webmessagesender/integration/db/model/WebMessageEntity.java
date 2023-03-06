package se.sundsvall.webmessagesender.integration.db.model;

import static java.time.temporal.ChronoUnit.MILLIS;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "web_message",
	indexes = {
		@Index(name = "web_message_party_id_index", columnList = "party_id")
	})
public class WebMessageEntity {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id")
	private String id;

	@Column(name = "party_id")
	private String partyId;

	@Column(name = "message")
	private String message;

	@Column(name = "created")
	private OffsetDateTime created;

	@Column(name = "oep_message_id")
	private Integer oepMessageId;

	@OneToMany(mappedBy = "webMessageEntity", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ExternalReferenceEntity> externalReferences;

	@OneToMany(mappedBy = "webMessageEntity", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AttachmentEntity> attachments;

	public static WebMessageEntity create() {
		return new WebMessageEntity();
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public WebMessageEntity withId(final String id) {
		this.id = id;
		return this;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(final String partyId) {
		this.partyId = partyId;
	}

	public WebMessageEntity withPartyId(final String partyId) {
		this.partyId = partyId;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public WebMessageEntity withMessage(final String message) {
		this.message = message;
		return this;
	}

	public OffsetDateTime getCreated() {
		return created;
	}

	public void setCreated(final OffsetDateTime created) {
		this.created = created;
	}

	public WebMessageEntity withCreated(final OffsetDateTime created) {
		this.created = created;
		return this;
	}

	public List<ExternalReferenceEntity> getExternalReferences() {
		return externalReferences;
	}

	public void setExternalReferences(final List<ExternalReferenceEntity> externalReferences) {
		Optional.ofNullable(externalReferences).ifPresent(entities -> entities.stream().forEach(e -> e.setWebMessageEntity(this)));
		this.externalReferences = externalReferences;
	}

	public WebMessageEntity withExternalReferences(final List<ExternalReferenceEntity> externalReferences) {
		this.setExternalReferences(externalReferences);
		return this;
	}

	public List<AttachmentEntity> getAttachments() {
		return attachments;
	}

	public void setAttachments(final List<AttachmentEntity> attachments) {
		Optional.ofNullable(attachments).ifPresent(attachment -> attachment.stream().forEach(e -> e.withWebMessageEntity(this)));
		this.attachments = attachments;
	}

	public WebMessageEntity withAttachments(final List<AttachmentEntity> attachments) {
		this.setAttachments(attachments);
		return this;
	}

	public Integer getOepMessageId() {
		return oepMessageId;
	}

	public void setOepMessageId(final Integer oepMessageId) {
		this.oepMessageId = oepMessageId;
	}

	public WebMessageEntity withOepMessageId(final Integer oepMessageId) {
		this.oepMessageId = oepMessageId;
		return this;
	}

	@PrePersist
	void prePersist() {
		created = OffsetDateTime.now(ZoneId.systemDefault()).truncatedTo(MILLIS);
	}

	@Override
	public int hashCode() {
		return Objects.hash(attachments, created, externalReferences, id, message, oepMessageId, partyId);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final WebMessageEntity other = (WebMessageEntity) obj;
		return Objects.equals(attachments, other.attachments) && Objects.equals(created, other.created) && Objects.equals(externalReferences, other.externalReferences) && Objects.equals(id, other.id) && Objects.equals(message, other.message) && Objects
			.equals(oepMessageId, other.oepMessageId) && Objects.equals(partyId, other.partyId);
	}

	@Override
	public String toString() {
		final var builder = new StringBuilder();
		builder.append("WebMessageEntity [id=").append(id).append(", partyId=").append(partyId).append(", message=").append(message).append(", created=").append(created).append(", oepMessageId=").append(oepMessageId).append(", externalReferences=").append(
			externalReferences).append(", attachments=").append(attachments).append("]");
		return builder.toString();
	}
}
