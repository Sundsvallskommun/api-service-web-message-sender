package se.sundsvall.webmessagesender.integration.db.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEqualsExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCodeExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToStringExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSettersExcluding;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class WebMessageEntityTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), OffsetDateTime.class);
	}

	@Test
	void testBean() {
		assertThat(WebMessageEntity.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSettersExcluding("externalReferences", "attachments"),
			hasValidBeanHashCodeExcluding("externalReferences", "attachments"),
			hasValidBeanEqualsExcluding("externalReferences", "attachments"),
			hasValidBeanToStringExcluding("externalReferences", "attachments")));
	}

	@Test
	void testBuilderMethods() {

		final var created = OffsetDateTime.now();
		final var id = UUID.randomUUID().toString();
		final var externalReferences = List.of(ExternalReferenceEntity.create());
		final var message = "message";
		final var municipalityId = "municipalityId";
		final var partyId = UUID.randomUUID().toString();
		final var oepMessageId = Integer.MAX_VALUE;
		final var attachments = List.of(AttachmentEntity.create());
		final var oepInstance = "internal";

		final var webMessage = WebMessageEntity.create()
			.withCreated(created)
			.withOepInstance(oepInstance)
			.withId(id)
			.withExternalReferences(externalReferences)
			.withMessage(message)
			.withMunicipalityId(municipalityId)
			.withPartyId(partyId)
			.withOepMessageId(oepMessageId)
			.withAttachments(attachments);

		assertThat(webMessage).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(webMessage.getCreated()).isEqualTo(created);
		assertThat(webMessage.getId()).isEqualTo(id);
		assertThat(webMessage.getOepInstance()).isEqualTo(oepInstance);
		assertThat(webMessage.getExternalReferences()).isSameAs(externalReferences);
		assertThat(webMessage.getMessage()).isEqualTo(message);
		assertThat(webMessage.getMunicipalityId()).isEqualTo(municipalityId);
		assertThat(webMessage.getPartyId()).isEqualTo(partyId);
		assertThat(webMessage.getOepMessageId()).isEqualTo(oepMessageId);
		assertThat(webMessage.getAttachments()).isSameAs(attachments);
	}

	@Test
	void testPrePersist() {
		final var webMessage = WebMessageEntity.create();
		webMessage.prePersist();
		assertThat(webMessage.getCreated()).isCloseTo(OffsetDateTime.now(), within(2, ChronoUnit.SECONDS));
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(WebMessageEntity.create()).hasAllNullFieldsOrProperties();
	}
}
