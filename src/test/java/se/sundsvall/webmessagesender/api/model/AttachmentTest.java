package se.sundsvall.webmessagesender.api.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

class AttachmentTest {

	@Test
	void testBean() {
		assertThat(Attachment.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {

		final var fileName = "fileName";
		final var mimeType = "mimeType";
		final var base64Data = "base64Data";

		final var attachment = Attachment.create()
			.withFileName(fileName)
			.withMimeType(mimeType)
			.withBase64Data(base64Data);

		assertThat(attachment.getFileName()).isEqualTo(fileName);
		assertThat(attachment.getMimeType()).isEqualTo(mimeType);
		assertThat(attachment.getBase64Data()).isEqualTo(base64Data);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		Assertions.assertThat(Attachment.create()).hasAllNullFieldsOrProperties();
	}
}
