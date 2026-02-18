package se.sundsvall.webmessagesender.integration.oep.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import se.sundsvall.webmessagesender.integration.oep.config.OepInternalConfiguration;

import static se.sundsvall.webmessagesender.integration.oep.config.OepInternalConfiguration.OEP_INTERNAL_CLIENT;

@FeignClient(
	name = OEP_INTERNAL_CLIENT,
	url = "${integration.oep.internal-url}",
	configuration = OepInternalConfiguration.class)
@CircuitBreaker(name = OEP_INTERNAL_CLIENT)
public interface OepInternalClient extends OepBaseClient {

}
