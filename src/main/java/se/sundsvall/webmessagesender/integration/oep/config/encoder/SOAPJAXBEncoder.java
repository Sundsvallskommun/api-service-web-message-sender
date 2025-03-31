package se.sundsvall.webmessagesender.integration.oep.config.encoder;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import org.springframework.util.FastByteArrayOutputStream;

public class SOAPJAXBEncoder implements Encoder {

	@Override
	public void encode(final Object object, final Type bodyType, final RequestTemplate template) throws EncodeException {
		try {
			final var jaxbContext = JAXBContext.newInstance(SOAPEnvelope.class, object.getClass());
			final var marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

			final var body = new SOAPBody();
			body.setContent(object);
			final var envelope = new SOAPEnvelope();
			envelope.setBody(body);

			final var baos = new FastByteArrayOutputStream();

			try (final var writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8)) {
				marshaller.marshal(envelope, writer);
			}

			template.body(baos.toByteArray(), StandardCharsets.UTF_8);
		} catch (final JAXBException | IOException e) {
			throw new EncodeException("Failed to encode object to XML", e);
		}
	}

}
