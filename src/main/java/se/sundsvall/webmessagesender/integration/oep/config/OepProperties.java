package se.sundsvall.webmessagesender.integration.oep.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("integration.oep")
public record OepProperties(int connectTimeout, int readTimeout, String username,
	String externalPassword, String internalPassword) {
}
