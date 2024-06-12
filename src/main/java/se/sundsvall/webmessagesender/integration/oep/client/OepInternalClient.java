package se.sundsvall.webmessagesender.integration.oep.client;

import static se.sundsvall.webmessagesender.integration.oep.config.InternalOepConfiguration.OEP_INTERNAL_CLIENT;

import org.springframework.cloud.openfeign.FeignClient;

import se.sundsvall.webmessagesender.integration.oep.config.InternalOepConfiguration;

@FeignClient(
	name = OEP_INTERNAL_CLIENT,
	url = "${integration.oep.internal-url}",
	configuration = InternalOepConfiguration.class
)
public interface OepInternalClient extends OepBaseClient {

}
