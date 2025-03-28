package se.sundsvall.webmessagesender.integration.oep.config;

import feign.auth.BasicAuthRequestInterceptor;
import feign.jaxb.JAXBContextFactory;
import feign.soap.SOAPDecoder;
import feign.soap.SOAPErrorDecoder;
import org.springframework.cloud.openfeign.FeignBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import se.sundsvall.dept44.configuration.feign.FeignConfiguration;
import se.sundsvall.dept44.configuration.feign.FeignMultiCustomizer;
import se.sundsvall.webmessagesender.integration.oep.config.encoder.SOAPJAXBEncoder;

@Import(FeignConfiguration.class)
public class OepExternalConfiguration {

	public static final String OEP_EXTERNAL_CLIENT = "oep-external";

	private static final JAXBContextFactory JAXB_FACTORY = new JAXBContextFactory.Builder().build();

	@Bean
	FeignBuilderCustomizer externalFeignBuilderCustomizer(final OepProperties properties) {
		return FeignMultiCustomizer.create()
			.withDecoder(new SOAPDecoder(JAXB_FACTORY))
			.withEncoder(new SOAPJAXBEncoder())
			.withErrorDecoder(new SOAPErrorDecoder())
			.withRequestInterceptor(new BasicAuthRequestInterceptor(properties.username(), properties.externalPassword()))
			.withRequestTimeoutsInSeconds(properties.connectTimeout(), properties.readTimeout())
			.composeCustomizersToOne();
	}
}
