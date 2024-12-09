package se.sundsvall.webmessagesender.integration.db.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Arrays;
import java.util.Objects;
import org.hibernate.Length;

@Entity
@Table(name = "attachment")
public class AttachmentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "mime_type")
	private String mimeType;

	@Column(name = "file", length = Length.LONG32)
	private byte[] file;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", nullable = false, foreignKey = @ForeignKey(name = "fk_attachment_parent_id_web_message_id"))
	private WebMessageEntity webMessageEntity;

	public static AttachmentEntity create() {
		return new AttachmentEntity();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AttachmentEntity withId(long id) {
		this.id = id;
		return this;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public AttachmentEntity withFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public AttachmentEntity withMimeType(String mimeType) {
		this.mimeType = mimeType;
		return this;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public AttachmentEntity withFile(byte[] file) {
		this.file = file;
		return this;
	}

	public WebMessageEntity getWebMessageEntity() {
		return webMessageEntity;
	}

	public void setWebMessageEntity(WebMessageEntity webMessageEntity) {
		this.webMessageEntity = webMessageEntity;
	}

	public AttachmentEntity withWebMessageEntity(WebMessageEntity webMessageEntity) {
		this.webMessageEntity = webMessageEntity;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		AttachmentEntity that = (AttachmentEntity) o;
		return id == that.id && Objects.equals(mimeType, that.mimeType) && Arrays.equals(file, that.file)
			&& Objects.equals(webMessageEntity, that.webMessageEntity)
			&& Objects.equals(fileName, that.fileName);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(id, mimeType, webMessageEntity, fileName);
		result = 31 * result + Arrays.hashCode(file);
		return result;
	}

	@Override
	public String toString() {
		final var webMessageEntityId = webMessageEntity == null ? null : webMessageEntity.getId();
		final StringBuilder sb = new StringBuilder("AttachmentEntity{")
			.append("id=").append(id)
			.append(", fileName=").append(fileName)
			.append(", mimeType='").append(mimeType)
			.append(", file=").append(Arrays.toString(file))
			.append(", webMessageEntity=").append(webMessageEntityId)
			.append('}');
		return sb.toString();
	}
}
