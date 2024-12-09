package se.sundsvall.webmessagesender.integration.db.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEqualsExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCodeExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToStringExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AttachmentEntityTest {

	@Test
	void testBean() {
		assertThat(AttachmentEntity.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCodeExcluding("webMessageEntity"),
			hasValidBeanEqualsExcluding("webMessageEntity"),
			hasValidBeanToStringExcluding("webMessageEntity")));
	}

	@Test
	void testBuilderMethods() {

		final var id = 1;
		final var fileName = "fileName";
		final var mimeType = "mimeType";
		final var fileContent = UUID.randomUUID().toString();
		final var file = fileContent.getBytes();
		final var webMessageEntity = WebMessageEntity.create();

		final var attachment = AttachmentEntity.create()
			.withId(id)
			.withFileName(fileName)
			.withMimeType(mimeType)
			.withFile(file)
			.withWebMessageEntity(webMessageEntity);

		assertThat(attachment).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(attachment.getId()).isEqualTo(id);
		assertThat(attachment.getFileName()).isEqualTo(fileName);
		assertThat(attachment.getMimeType()).isEqualTo(mimeType);
		assertThat(attachment.getFile()).asString().isEqualTo(fileContent);
		assertThat(attachment.getWebMessageEntity()).isSameAs(webMessageEntity);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		Assertions.assertThat(AttachmentEntity.create()).hasAllNullFieldsOrPropertiesExcept("id").hasFieldOrPropertyWithValue("id", 0L);
	}
}
