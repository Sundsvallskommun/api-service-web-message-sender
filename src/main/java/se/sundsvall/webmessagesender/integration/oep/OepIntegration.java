package se.sundsvall.webmessagesender.integration.oep;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import se.sundsvall.webmessagesender.generatedsources.oep.AddMessage;
import se.sundsvall.webmessagesender.generatedsources.oep.AddMessageResponse;

@FeignClient(name = "oep.integration", url = "${integration.oep.url}", configuration = OepIntegrationConfiguration.class)
@CircuitBreaker(name = "oep")
public interface OepIntegration {
	static final String TEXT_XML_UTF_8 = "text/xml; charset=UTF-8";

	@PostMapping(consumes = TEXT_XML_UTF_8, produces = TEXT_XML_UTF_8)
	AddMessageResponse addMessage(@RequestBody AddMessage addMessage);
}
