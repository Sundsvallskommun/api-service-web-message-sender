package se.sundsvall.webmessagesender.integration.oep.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.sundsvall.webmessagesender.Application;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("junit")
class OepPropertiesTest {

	@Autowired
	private OepProperties properties;

	@Test
	void testProperties() {
		assertThat(properties.connectTimeout()).isEqualTo(10);
		assertThat(properties.externalPassword()).isEqualTo("oep.client.external.password");
		assertThat(properties.internalPassword()).isEqualTo("oep.client.internal.password");
		assertThat(properties.readTimeout()).isEqualTo(20);
		assertThat(properties.username()).isEqualTo("oep.client.username");
	}
}
