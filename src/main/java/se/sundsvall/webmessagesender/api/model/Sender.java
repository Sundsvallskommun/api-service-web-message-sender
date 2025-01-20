package se.sundsvall.webmessagesender.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Schema(description = "Sender model")
public class Sender {

	@Schema(description = "The user ID of the sender. I.e. employee ID", example = "joe01doe")
	private String userId;

	public static Sender create() {
		return new Sender();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Sender withUserId(String userId) {
		this.userId = userId;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId);
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
		Sender other = (Sender) obj;
		return Objects.equals(userId, other.userId);
	}

	@Override
	public String toString() {
		return "Sender [userId=" + userId + "]";
	}
}
