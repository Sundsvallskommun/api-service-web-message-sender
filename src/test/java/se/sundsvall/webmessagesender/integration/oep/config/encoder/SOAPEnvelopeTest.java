package se.sundsvall.webmessagesender.integration.oep.config.encoder;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class SOAPEnvelopeTest {

	@Test
	void testClassAnnotations() {
		final var xmlRootElement = SOAPEnvelope.class.getAnnotation(XmlRootElement.class);
		assertThat(xmlRootElement).isNotNull();
		assertThat(xmlRootElement.name()).isEqualTo("Envelope");
		assertThat(xmlRootElement.namespace()).isEqualTo("http://schemas.xmlsoap.org/soap/envelope/");

		final var xmlAccessorType = SOAPEnvelope.class.getAnnotation(XmlAccessorType.class);
		assertThat(xmlAccessorType).isNotNull();
		assertThat(xmlAccessorType.value()).isEqualTo(XmlAccessType.FIELD);
	}

	@Test
	void testFieldAnnotation() throws NoSuchFieldException {
		final var body = SOAPEnvelope.class.getDeclaredField("body");
		final var xmlElement = body.getAnnotation(XmlElement.class);
		assertThat(xmlElement).isNotNull();
		assertThat(xmlElement.name()).isEqualTo("Body");
		assertThat(xmlElement.namespace()).isEqualTo("http://schemas.xmlsoap.org/soap/envelope/");
	}

	@Test
	void testBean() {
		MatcherAssert.assertThat(SOAPEnvelope.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanEquals(),
			hasValidBeanHashCode(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {
		final var body = new SOAPBody();
		final var bean = new SOAPEnvelope();
		bean.setBody(body);

		assertThat(bean).hasNoNullFieldsOrProperties();
		assertThat(bean.getBody()).isEqualTo(body);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(new SOAPEnvelope()).hasAllNullFieldsOrProperties();
	}
}
