package se.sundsvall.webmessagesender.api.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class ExternalReferenceTest {

	@Test
	void testBean() {
		assertThat(ExternalReference.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {

		final var key = "key";
		final var value = "value;";

		final var externalReference = ExternalReference.create()
			.withKey(key)
			.withValue(value);

		assertThat(externalReference).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(externalReference.getKey()).isEqualTo(key);
		assertThat(externalReference.getValue()).isEqualTo(value);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(ExternalReference.create()).hasAllNullFieldsOrProperties();
	}
}
