package se.sundsvall.webmessagesender.api.model;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "ExternalReference model")
public class ExternalReference {

	@Schema(description = "The external reference key", example = "flowInstanceId")
	private String key;

	@Schema(description = "The external reference value", example = "356746349")
	private String value;

	public static ExternalReference create() {
		return new ExternalReference();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public ExternalReference withKey(String key) {
		this.key = key;
		return this;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ExternalReference withValue(String value) {
		this.value = value;
		return this;
	}

	@Override
	public int hashCode() { return Objects.hash(key, value); }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExternalReference other = (ExternalReference) obj;
		return Objects.equals(key, other.key) && Objects.equals(value, other.value);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ExternalReference [key=").append(key).append(", value=").append(value).append("]");
		return builder.toString();
	}
}
