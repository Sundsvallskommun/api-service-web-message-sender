package se.sundsvall.webmessagesender.integration.db.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.UuidGenerator;

import static java.time.temporal.ChronoUnit.MILLIS;
import static org.hibernate.Length.LONG32;
import static org.hibernate.annotations.TimeZoneStorageType.NORMALIZE;

@Entity
@Table(name = "web_message",
	indexes = {
		@Index(name = "web_message_municipality_id_index", columnList = "municipality_id"),
		@Index(name = "web_message_party_id_index", columnList = "party_id")
	})
public class WebMessageEntity {

	@Id
	@UuidGenerator
	@Column(name = "id")
	private String id;

	@Column(name = "municipality_id")
	private String municipalityId;

	@Column(name = "party_id")
	private String partyId;

	@Column(name = "sender_user_id")
	private String senderUserId;

	@Column(name = "message", length = LONG32)
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

	public String getMunicipalityId() {
		return municipalityId;
	}

	public void setMunicipalityId(String municipalityId) {
		this.municipalityId = municipalityId;
	}

	public WebMessageEntity withMunicipalityId(String municipalityId) {
		this.municipalityId = municipalityId;
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

	public String getSenderUserId() {
		return senderUserId;
	}

	public void setSenderUserId(String senderUserId) {
		this.senderUserId = senderUserId;
	}

	public WebMessageEntity withSenderUserId(String senderUserId) {
		this.senderUserId = senderUserId;
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
	public int hashCode() {
		return Objects.hash(attachments, created, externalReferences, id, message, municipalityId, oepInstance, oepMessageId, partyId, senderUserId);
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
		WebMessageEntity other = (WebMessageEntity) obj;
		return Objects.equals(attachments, other.attachments) && Objects.equals(created, other.created) && Objects.equals(externalReferences, other.externalReferences) && Objects.equals(id, other.id) && Objects.equals(message, other.message) && Objects
			.equals(municipalityId, other.municipalityId) && Objects.equals(oepInstance, other.oepInstance) && Objects.equals(oepMessageId, other.oepMessageId) && Objects.equals(partyId, other.partyId) && Objects.equals(senderUserId, other.senderUserId);
	}

	@Override
	public String toString() {
		return "WebMessageEntity [id=" + id + ", municipalityId=" + municipalityId + ", partyId=" + partyId + ", senderUserId=" + senderUserId + ", message=" + message + ", oepInstance=" + oepInstance + ", created=" + created + ", oepMessageId="
			+ oepMessageId + ", externalReferences=" + externalReferences + ", attachments=" + attachments + "]";
	}
}
