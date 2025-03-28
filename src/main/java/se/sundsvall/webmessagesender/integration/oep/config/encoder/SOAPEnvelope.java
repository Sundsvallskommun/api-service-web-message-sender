package se.sundsvall.webmessagesender.integration.oep.config.encoder;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement(name = "Envelope", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
@XmlAccessorType(XmlAccessType.FIELD)
public class SOAPEnvelope {

	@XmlElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
	private SOAPBody body;

	public SOAPBody getBody() {
		return body;
	}

	public void setBody(final SOAPBody body) {
		this.body = body;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final SOAPEnvelope that = (SOAPEnvelope) o;
		return Objects.equals(body, that.body);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(body);
	}

	@Override
	public String toString() {
		return "SOAPEnvelope{" +
			"body=" + body +
			'}';
	}
}
