package se.sundsvall.webmessagesender.api.model;

import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

class SenderTest {

	@Test
	void testBean() {
		assertThat(Sender.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {

		final var userId = "userId";

		final var webMessage = Sender.create()
			.withUserId(userId);

		assertThat(webMessage).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(webMessage.getUserId()).isEqualTo(userId);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(WebMessage.create()).hasAllNullFieldsOrProperties();
	}
}
