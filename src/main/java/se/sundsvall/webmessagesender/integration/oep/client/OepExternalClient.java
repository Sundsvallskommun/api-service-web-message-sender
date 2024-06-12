package se.sundsvall.webmessagesender.integration.oep.client;


import static se.sundsvall.webmessagesender.integration.oep.config.ExternalOepConfiguration.OEP_EXTERNAL_CLIENT;

import org.springframework.cloud.openfeign.FeignClient;

import se.sundsvall.webmessagesender.integration.oep.config.ExternalOepConfiguration;

@FeignClient(
	name = OEP_EXTERNAL_CLIENT,
	url = "${integration.oep.external-url}",
	configuration = ExternalOepConfiguration.class
)
public interface OepExternalClient extends OepBaseClient {

}
