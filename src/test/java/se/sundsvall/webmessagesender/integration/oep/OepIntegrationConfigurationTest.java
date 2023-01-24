package se.sundsvall.webmessagesender.integration.oep;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import feign.auth.BasicAuthRequestInterceptor;
import feign.soap.SOAPDecoder;
import feign.soap.SOAPEncoder;
import feign.soap.SOAPErrorDecoder;
import se.sundsvall.dept44.configuration.feign.FeignMultiCustomizer;
import se.sundsvall.webmessagesender.Application;

@SpringBootTest(classes = { Application.class, OepIntegrationConfiguration.class })
@ActiveProfiles("junit")
class OepIntegrationConfigurationTest {

	@Autowired
	private OepIntegrationConfiguration configuration;
	
	@Mock
	private OepIntegrationProperties propertiesMock;

	@Captor
	private ArgumentCaptor<BasicAuthRequestInterceptor> basicAuthRequestInterceptorCaptor;
	
	@Spy
	private FeignMultiCustomizer feignMultiCustomizerSpy;
	
	@Test
	void testFeignMultiCustomizer() {

		final var connectTimeout = 123;
		final var readTimeout = 321;
		final var password = "password";
		final var username = "username";

		when(propertiesMock.connectTimeout()).thenReturn(connectTimeout);
		when(propertiesMock.readTimeout()).thenReturn(readTimeout);
		when(propertiesMock.password()).thenReturn(password);
		when(propertiesMock.username()).thenReturn(username);

		// Mock static FeignMultiCustomizer to enable spy and to verify that static method is being called
		try (MockedStatic<FeignMultiCustomizer> feignMultiCustomizerMock = Mockito.mockStatic(FeignMultiCustomizer.class)) {
			feignMultiCustomizerMock.when(FeignMultiCustomizer::create).thenReturn(feignMultiCustomizerSpy);

			configuration.feignBuilderCustomizer(propertiesMock);

			feignMultiCustomizerMock.verify(FeignMultiCustomizer::create);
		}

		// Verifications
		verify(propertiesMock).connectTimeout();
		verify(propertiesMock).readTimeout();
		verify(propertiesMock).password();
		verify(propertiesMock).username();
		verify(feignMultiCustomizerSpy).withDecoder(any(SOAPDecoder.class));
		verify(feignMultiCustomizerSpy).withEncoder(any(SOAPEncoder.class));
		verify(feignMultiCustomizerSpy).withErrorDecoder(any(SOAPErrorDecoder.class));
		verify(feignMultiCustomizerSpy).withRequestInterceptor(basicAuthRequestInterceptorCaptor.capture());
		verify(feignMultiCustomizerSpy).withRequestTimeoutsInSeconds(connectTimeout, readTimeout);
		verify(feignMultiCustomizerSpy).composeCustomizersToOne();

		// Assert captors
		assertThat(basicAuthRequestInterceptorCaptor.getValue()).hasFieldOrPropertyWithValue("headerValue", "Basic " + base64Encode((username + ":" + password)));
	}
	
	private String base64Encode(String string) {
		return new String(Base64.getEncoder().encode(string.getBytes()));
	}	
}
