package se.sundsvall.webmessagesender.api.model;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;
import se.sundsvall.dept44.common.validators.annotation.ValidBase64;
import se.sundsvall.webmessagesender.api.validation.ValidFileSize;

@Schema(description = "File attachment")
public class Attachment {

	@Schema(description = "Name of file")
	@NotBlank
	private String fileName;

	@Schema(description = "mimeType of file", accessMode = READ_ONLY)
	private String mimeType;

	@Schema(description = "Base 64 encoded file, max size 50 MB", format = "base64")
	@ValidBase64
	@ValidFileSize
	private String base64Data;

	public static Attachment create() {
		return new Attachment();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	public Attachment withFileName(final String fileName) {
		this.fileName = fileName;
		return this;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(final String mimeType) {
		this.mimeType = mimeType;
	}

	public Attachment withMimeType(final String mimeType) {
		this.mimeType = mimeType;
		return this;
	}

	public String getBase64Data() {
		return base64Data;
	}

	public void setBase64Data(final String base64Data) {
		this.base64Data = base64Data;
	}

	public Attachment withBase64Data(final String base64Data) {
		this.base64Data = base64Data;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final Attachment that = (Attachment) o;
		return Objects.equals(fileName, that.fileName) && Objects.equals(base64Data, that.base64Data) && Objects.equals(mimeType, that.mimeType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fileName, base64Data, mimeType);
	}

	@Override
	public String toString() {
		return "Attachment{" + "fileName='" + fileName + '\''
			+ ", base64Data='" + base64Data + '\''
			+ ", mimeType='" + mimeType + '\''
			+ '}';
	}
}
