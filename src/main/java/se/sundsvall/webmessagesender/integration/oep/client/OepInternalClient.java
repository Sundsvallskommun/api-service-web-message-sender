package se.sundsvall.webmessagesender.integration.oep.client;

import static se.sundsvall.webmessagesender.integration.oep.config.OepConfiguration.OEP_INTERNAL_CLIENT;

import org.springframework.cloud.openfeign.FeignClient;

import se.sundsvall.webmessagesender.integration.oep.config.OepConfiguration;

@FeignClient(
	name = OEP_INTERNAL_CLIENT,
	url = "${integration.oep.internal-url}",
	configuration = OepConfiguration.class,
	qualifiers = "internalFeignBuilderCustomizer"
)
public interface OepInternalClient extends OepBaseClient {

}
