package se.sundsvall.webmessagesender.integration.db.model;

import static java.time.temporal.ChronoUnit.MILLIS;
import static org.hibernate.annotations.TimeZoneStorageType.NORMALIZE;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import org.hibernate.Length;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "web_message",
	indexes = {
		@Index(name = "web_message_party_id_index", columnList = "party_id")
	})
public class WebMessageEntity {

	@Id
	@UuidGenerator
	@Column(name = "id")
	private String id;

	@Column(name = "party_id")
	private String partyId;

	@Column(name = "message", length = Length.LONG32)
	private String message;

	@Column(name = "oep_instance")
	private String oepInstance;

	@Column(name = "created")
	@TimeZoneStorage(NORMALIZE)
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

	public String getOepInstance() {
		return oepInstance;
	}

	public void setOepInstance(final String oepInstance) {
		this.oepInstance = oepInstance;
	}

	public WebMessageEntity withOepInstance(final String oepInstance) {
		this.oepInstance = oepInstance;
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
		Optional.ofNullable(externalReferences).ifPresent(entities -> entities.forEach(e -> e.setWebMessageEntity(this)));
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
		Optional.ofNullable(attachments).ifPresent(attachment -> attachment.forEach(e -> e.withWebMessageEntity(this)));
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
	public String toString() {
		return "WebMessageEntity{" +
			"id='" + id + '\'' +
			", partyId='" + partyId + '\'' +
			", message='" + message + '\'' +
			", oepInstance='" + oepInstance + '\'' +
			", created=" + created +
			", oepMessageId=" + oepMessageId +
			", externalReferences=" + externalReferences +
			", attachments=" + attachments +
			'}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final WebMessageEntity that = (WebMessageEntity) o;
		return Objects.equals(id, that.id) && Objects.equals(partyId, that.partyId) && Objects.equals(message, that.message) && Objects.equals(oepInstance, that.oepInstance) && Objects.equals(created, that.created) && Objects.equals(oepMessageId, that.oepMessageId) && Objects.equals(externalReferences, that.externalReferences) && Objects.equals(attachments, that.attachments);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, partyId, message, oepInstance, created, oepMessageId, externalReferences, attachments);
	}
}
