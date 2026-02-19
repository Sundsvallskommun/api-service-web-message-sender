package se.sundsvall.webmessagesender.api.model;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

class CreateWebMessageRequestTest {

	@Test
	void testBean() {
		assertThat(CreateWebMessageRequest.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {

		final var externalReferences = List.of(ExternalReference.create());
		final var message = "message";
		final var partyId = UUID.randomUUID().toString();
		final var attachments = List.of(Attachment.create());
		final var oepInstance = "internal";
		final var sender = Sender.create();

		final var createWebMessageRequest = CreateWebMessageRequest.create()
			.withExternalReferences(externalReferences)
			.withMessage(message)
			.withOepInstance(oepInstance)
			.withPartyId(partyId)
			.withSender(sender)
			.withAttachments(attachments);

		assertThat(createWebMessageRequest).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(createWebMessageRequest.getExternalReferences()).isEqualTo(externalReferences);
		assertThat(createWebMessageRequest.getMessage()).isEqualTo(message);
		assertThat(createWebMessageRequest.getOepInstance()).isEqualTo(oepInstance);
		assertThat(createWebMessageRequest.getPartyId()).isEqualTo(partyId);
		assertThat(createWebMessageRequest.getSender()).isEqualTo(sender);
		assertThat(createWebMessageRequest.getAttachments()).isEqualTo(attachments);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(CreateWebMessageRequest.create()).hasAllNullFieldsOrProperties();
	}
}
