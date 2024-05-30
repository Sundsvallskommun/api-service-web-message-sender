package se.sundsvall.webmessagesender.integration.oep.client;

import static se.sundsvall.webmessagesender.integration.oep.config.OepInternalConfiguration.OEP_INTERNAL_CLIENT;

import org.springframework.cloud.openfeign.FeignClient;

import se.sundsvall.webmessagesender.integration.oep.config.OepInternalConfiguration;

@FeignClient(
	name = OEP_INTERNAL_CLIENT,
	url = "${integration.oep.internal-url}",
	configuration = OepInternalConfiguration.class
)
public interface OepInternalClient extends OepBaseClient {

}
