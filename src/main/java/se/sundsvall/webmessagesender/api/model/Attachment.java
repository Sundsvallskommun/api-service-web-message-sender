package se.sundsvall.webmessagesender.api.model;

import java.util.Objects;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import se.sundsvall.dept44.common.validators.annotation.ValidBase64;
import se.sundsvall.webmessagesender.api.validation.ValidFileSize;

@Schema(description = "File attachment")
public class Attachment {

	@Schema(description = "Name of file")
	@NotBlank
	private String fileName;

	@Schema(description = "mimeType of file", accessMode = Schema.AccessMode.READ_ONLY)
	private String mimeType;

	@Schema(description = "Base 64 encoded file, max size 10 MB", format = "base64")
	@ValidBase64
	@ValidFileSize
	private String base64Data;

	public static Attachment create() {
		return new Attachment();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Attachment withFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Attachment withMimeType(String mimeType) {
		this.mimeType = mimeType;
		return this;
	}

	public String getBase64Data() {
		return base64Data;
	}

	public void setBase64Data(String base64Data) {
		this.base64Data = base64Data;
	}

	public Attachment withBase64Data(String base64Data) {
		this.base64Data = base64Data;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Attachment that = (Attachment) o;
		return Objects.equals(fileName, that.fileName) && Objects.equals(base64Data, that.base64Data) && Objects.equals(mimeType, that.mimeType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fileName, base64Data, mimeType);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Attachment{");
		sb.append("fileName='").append(fileName).append('\'');
		sb.append(", base64Data='").append(base64Data).append('\'');
		sb.append(", mimeType='").append(mimeType).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
