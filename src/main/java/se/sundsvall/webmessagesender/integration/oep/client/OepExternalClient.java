package se.sundsvall.webmessagesender.integration.oep.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import se.sundsvall.webmessagesender.integration.oep.config.OepExternalConfiguration;

import static se.sundsvall.webmessagesender.integration.oep.config.OepExternalConfiguration.OEP_EXTERNAL_CLIENT;

@FeignClient(
	name = OEP_EXTERNAL_CLIENT,
	url = "${integration.oep.external-url}",
	configuration = OepExternalConfiguration.class)
@CircuitBreaker(name = OEP_EXTERNAL_CLIENT)
public interface OepExternalClient extends OepBaseClient {

}
