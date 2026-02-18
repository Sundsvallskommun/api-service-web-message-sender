package se.sundsvall.webmessagesender.integration.db.model;

import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEqualsExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCodeExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToStringExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

class ExternalReferenceEntityTest {

	@Test
	void testBean() {
		assertThat(ExternalReferenceEntity.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCodeExcluding("webMessageEntity"),
			hasValidBeanEqualsExcluding("webMessageEntity"),
			hasValidBeanToStringExcluding("webMessageEntity")));
	}

	@Test
	void testBuilderMethods() {

		final var id = 123;
		final var key = "key";
		final var value = "value";
		final var webMessageEntity = WebMessageEntity.create();

		final var externalReference = ExternalReferenceEntity.create()
			.withId(id)
			.withKey(key)
			.withValue(value)
			.withWebMessageEntity(webMessageEntity);

		assertThat(externalReference).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(externalReference.getId()).isEqualTo(id);
		assertThat(externalReference.getKey()).isEqualTo(key);
		assertThat(externalReference.getValue()).isEqualTo(value);
		assertThat(externalReference.getWebMessageEntity()).isEqualTo(webMessageEntity);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(ExternalReferenceEntity.create()).hasAllNullFieldsOrPropertiesExcept("id").hasFieldOrPropertyWithValue("id", 0L);
	}
}
