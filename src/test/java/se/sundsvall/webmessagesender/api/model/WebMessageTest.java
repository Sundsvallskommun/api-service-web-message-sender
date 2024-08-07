package se.sundsvall.webmessagesender.api.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class WebMessageTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), OffsetDateTime.class);
	}

	@Test
	void testBean() {
		assertThat(WebMessage.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {

		final var created = OffsetDateTime.now();
		final var id = UUID.randomUUID().toString();
		final var externalReferences = List.of(ExternalReference.create());
		final var message = "message";
		final var municipalityId = "municipalityId";
		final var partyId = UUID.randomUUID().toString();
		final var attachments = List.of(Attachment.create());
		final var oepInstance = "internal";

		final var webMessage = WebMessage.create()
			.withCreated(created)
			.withOepInstance(oepInstance)
			.withId(id)
			.withExternalReferences(externalReferences)
			.withMessage(message)
			.withMunicipalityId(municipalityId)
			.withPartyId(partyId)
			.withAttachments(attachments);

		assertThat(webMessage).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(webMessage.getCreated()).isEqualTo(created);
		assertThat(webMessage.getId()).isEqualTo(id);
		assertThat(webMessage.getOepInstance()).isEqualTo(oepInstance);
		assertThat(webMessage.getExternalReferences()).isEqualTo(externalReferences);
		assertThat(webMessage.getMessage()).isEqualTo(message);
		assertThat(webMessage.getMunicipalityId()).isEqualTo(municipalityId);
		assertThat(webMessage.getPartyId()).isEqualTo(partyId);
		assertThat(webMessage.getAttachments()).isEqualTo(attachments);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(WebMessage.create()).hasAllNullFieldsOrProperties();
	}
}
