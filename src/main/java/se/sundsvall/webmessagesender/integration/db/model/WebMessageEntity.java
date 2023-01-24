package se.sundsvall.webmessagesender.integration.db.model;

import static java.time.OffsetDateTime.now;
import static java.time.temporal.ChronoUnit.MILLIS;

import java.time.OffsetDateTime;
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
@Table(name = "web_message", indexes = {
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

	public void setId(String id) {
		this.id = id;
	}

	public WebMessageEntity withId(String id) {
		this.id = id;
		return this;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public WebMessageEntity withPartyId(String partyId) {
		this.partyId = partyId;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public WebMessageEntity withMessage(String message) {
		this.message = message;
		return this;
	}

	public OffsetDateTime getCreated() {
		return created;
	}

	public void setCreated(OffsetDateTime created) {
		this.created = created;
	}

	public WebMessageEntity withCreated(OffsetDateTime created) {
		this.created = created;
		return this;
	}

	public List<ExternalReferenceEntity> getExternalReferences() {
		return externalReferences;
	}

	public void setExternalReferences(List<ExternalReferenceEntity> externalReferences) {
		Optional.ofNullable(externalReferences).ifPresent(entities -> entities.stream().forEach(e -> e.setWebMessageEntity(this)));
		this.externalReferences = externalReferences;
	}

	public WebMessageEntity withExternalReferences(List<ExternalReferenceEntity> externalReferences) {
		this.setExternalReferences(externalReferences);
		return this;
	}

	public List<AttachmentEntity> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentEntity> attachments) {
		Optional.ofNullable(attachments).ifPresent(attachment -> attachment.stream().forEach(e -> e.withWebMessageEntity(this)));
		this.attachments = attachments;
	}

	public WebMessageEntity withAttachments(List<AttachmentEntity> attachments) {
		this.setAttachments(attachments);
		return this;
	}

	public Integer getOepMessageId() {
		return oepMessageId;
	}

	public void setOepMessageId(Integer oepMessageId) {
		this.oepMessageId = oepMessageId;
	}

	public WebMessageEntity withOepMessageId(Integer oepMessageId) {
		this.oepMessageId = oepMessageId;
		return this;
	}
	
	@PrePersist
	void prePersist() {
		created = now().truncatedTo(MILLIS);
	}

	@Override
	public int hashCode() {
		return Objects.hash(created, externalReferences, id, message, oepMessageId, partyId, attachments);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WebMessageEntity other = (WebMessageEntity) obj;
		return Objects.equals(created, other.created) && Objects.equals(externalReferences, other.externalReferences)
				&& Objects.equals(id, other.id) && Objects.equals(message, other.message) && oepMessageId == other.oepMessageId
				&& Objects.equals(partyId, other.partyId)
				&& Objects.equals(attachments, attachments);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WebMessageEntity [id=").append(id)
				.append(", partyId=").append(partyId)
				.append(", message=").append(message)
				.append(", created=").append(created)
				.append(", oepMessageId=").append(oepMessageId)
				.append(", externalReferences=").append(externalReferences)
				.append(", attachments=").append(attachments)
				.append("]");
		return builder.toString();
	}
}
