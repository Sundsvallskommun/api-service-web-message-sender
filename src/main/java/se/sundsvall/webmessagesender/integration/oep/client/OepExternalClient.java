package se.sundsvall.webmessagesender.integration.oep.client;

import static se.sundsvall.webmessagesender.integration.oep.config.OepConfiguration.OEP_EXTERNAL_CLIENT;

import org.springframework.cloud.openfeign.FeignClient;

import se.sundsvall.webmessagesender.integration.oep.config.OepConfiguration;

@FeignClient(
	name = OEP_EXTERNAL_CLIENT,
	url = "${integration.oep.external-url}",
	configuration = OepConfiguration.class,
	qualifiers = "externalFeignBuilderCustomizer"
)
public interface OepExternalClient extends OepBaseClient {

}
