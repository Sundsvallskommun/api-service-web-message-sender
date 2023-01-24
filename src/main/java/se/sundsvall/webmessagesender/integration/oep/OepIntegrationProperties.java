package se.sundsvall.webmessagesender.integration.oep;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("integration.oep")
public record OepIntegrationProperties(int connectTimeout, int readTimeout, String username, String password) {}
