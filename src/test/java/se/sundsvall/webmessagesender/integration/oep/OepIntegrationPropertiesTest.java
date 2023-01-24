package se.sundsvall.webmessagesender.integration.oep;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import se.sundsvall.webmessagesender.Application;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("junit")
class OepIntegrationPropertiesTest {

	@Autowired
	private OepIntegrationProperties properties;
	
	@Test
	void testProperties() {
		assertThat(properties.connectTimeout()).isEqualTo(10);
		assertThat(properties.password()).isEqualTo("oep.client.password");
		assertThat(properties.readTimeout()).isEqualTo(20);
		assertThat(properties.username()).isEqualTo("oep.client.username");
	}
}
