package se.sundsvall.webmessagesender.integration.oep;

import javax.xml.soap.SOAPConstants;

import org.springframework.cloud.openfeign.FeignBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import feign.auth.BasicAuthRequestInterceptor;
import feign.jaxb.JAXBContextFactory;
import feign.soap.SOAPDecoder;
import feign.soap.SOAPEncoder.Builder;
import feign.soap.SOAPErrorDecoder;
import se.sundsvall.dept44.configuration.feign.FeignConfiguration;
import se.sundsvall.dept44.configuration.feign.FeignMultiCustomizer;

@Import(FeignConfiguration.class)
public class OepIntegrationConfiguration {

	private static final JAXBContextFactory JAXB_FACTORY = new JAXBContextFactory.Builder()
		.build();

	private static final Builder ENCODER_BUILDER = new Builder()
		.withFormattedOutput(false)
		.withJAXBContextFactory(JAXB_FACTORY)
		.withSOAPProtocol(SOAPConstants.SOAP_1_1_PROTOCOL)
		.withWriteXmlDeclaration(true);

	@Bean
	FeignBuilderCustomizer feignBuilderCustomizer(OepIntegrationProperties properties) {
		return FeignMultiCustomizer.create()
			.withDecoder(new SOAPDecoder(JAXB_FACTORY))
			.withEncoder(ENCODER_BUILDER.build())
			.withErrorDecoder(new SOAPErrorDecoder())
			.withRequestInterceptor(new BasicAuthRequestInterceptor(properties.username(), properties.password()))
			.withRequestTimeoutsInSeconds(properties.connectTimeout(), properties.readTimeout())
			.composeCustomizersToOne();
	}
}
