package se.sundsvall.webmessagesender.integration.db.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "external_reference", indexes = {
	@Index(name = "external_reference_ref_key_index", columnList = "ref_key"),
	@Index(name = "external_reference_ref_value_index", columnList = "ref_value")
}, uniqueConstraints = {
	@UniqueConstraint(name = "unique_external_reference", columnNames = { "ref_key", "ref_value", "parent_id" })
})
public class ExternalReferenceEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "ref_key")
	private String key;

	@Column(name = "ref_value")
	private String value;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", nullable = false, foreignKey = @ForeignKey(name = "fk_external_reference_parent_id_web_message_id"))
	private WebMessageEntity webMessageEntity;

	public static ExternalReferenceEntity create() {
		return new ExternalReferenceEntity();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ExternalReferenceEntity withId(long id) {
		this.id = id;
		return this;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public ExternalReferenceEntity withKey(String key) {
		this.key = key;
		return this;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ExternalReferenceEntity withValue(String value) {
		this.value = value;
		return this;
	}

	public WebMessageEntity getWebMessageEntity() {
		return webMessageEntity;
	}

	public void setWebMessageEntity(WebMessageEntity webMessageEntity) {
		this.webMessageEntity = webMessageEntity;
	}

	public ExternalReferenceEntity withWebMessageEntity(WebMessageEntity webMessageEntity) {
		this.webMessageEntity = webMessageEntity;
		return this;
	}

	@Override
	public int hashCode() { return Objects.hash(id, key, value); }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExternalReferenceEntity other = (ExternalReferenceEntity) obj;
		return id == other.id && Objects.equals(key, other.key) && Objects.equals(value, other.value);
	}

	@Override
	public String toString() {
		final var webMessageEntityId = webMessageEntity == null ? null : webMessageEntity.getId();
		StringBuilder builder = new StringBuilder();
		builder.append("ExternalReferenceEntity [id=").append(id).append(", key=").append(key).append(", value=").append(value).append(", webMessageEntity=").append(
			webMessageEntityId).append("]");
		return builder.toString();
	}
}
