package se.sundsvall.webmessagesender.integration.oep.config.encoder;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAnyElement;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class SOAPBody {

	@XmlAnyElement(lax = true)
	private Object content;

	public Object getContent() {
		return content;
	}

	public void setContent(final Object content) {
		this.content = content;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final SOAPBody soapBody = (SOAPBody) o;
		return Objects.equals(content, soapBody.content);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(content);
	}

	@Override
	public String toString() {
		return "SOAPBody{" +
			"content=" + content +
			'}';
	}
}
