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
public class OepInternalConfiguration {

	public static final String OEP_INTERNAL_CLIENT = "oep-internal";

	private static final JAXBContextFactory JAXB_FACTORY = new JAXBContextFactory.Builder().build();

	@Bean
	FeignBuilderCustomizer internalFeignBuilderCustomizer(final OepProperties properties) {
		return FeignMultiCustomizer.create()
			.withDecoder(new SOAPDecoder(JAXB_FACTORY))
			.withEncoder(new SOAPJAXBEncoder())
			.withErrorDecoder(new SOAPErrorDecoder())
			.withRequestInterceptor(new BasicAuthRequestInterceptor(properties.username(), properties.internalPassword()))
			.withRequestTimeoutsInSeconds(properties.connectTimeout(), properties.readTimeout())
			.composeCustomizersToOne();
	}
}
