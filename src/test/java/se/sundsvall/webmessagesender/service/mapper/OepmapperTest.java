package se.sundsvall.webmessagesender.service.mapper;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.groups.Tuple.tuple;
import static se.sundsvall.webmessagesender.api.model.Attachment.create;
import static se.sundsvall.webmessagesender.service.mapper.OepMapper.toAddMessage;

import java.util.Base64;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.sundsvall.webmessagesender.generatedsources.oep.Attachment;

class OepmapperTest {
	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Test
	void testToAddMessage() throws Exception {
		final var base64Data = new String(Base64.getEncoder().encode("base64Data".getBytes()));
		final var message = "A message with å, ä and ö";
		final var flowInstanceId = 1337;
		final var attachment = create().withBase64Data(base64Data).withFileName("fileName").withMimeType("mimeType");
		final var addMessage = toAddMessage(message, flowInstanceId, List.of(attachment));
		final var base64binary = MAPPER.convertValue(attachment.getBase64Data(), byte[].class);

		assertThat(addMessage.getExternalID()).isNull();
		assertThat(addMessage.getPrincipal()).isNull();
		assertThat(addMessage.getFlowInstanceID()).isEqualTo(flowInstanceId);
		assertThat(addMessage.getMessage()).isNotNull();
		assertThat(addMessage.getMessage().getAdded()).isNotNull();
		assertThat(addMessage.getMessage().getAdded().toGregorianCalendar().toZonedDateTime().toLocalDateTime()).isCloseTo(now(), within(2, SECONDS));
		assertThat(addMessage.getMessage().getMessage()).isEqualTo(message);
		assertThat(addMessage.getMessage().getUserID()).isNull();
		assertThat(addMessage.getMessage().getAttachments())
			.extracting(
				Attachment::getEncodedData,
				Attachment::getFilename,
				Attachment::getSize)
			.containsExactly(tuple(
				base64binary,
				attachment.getFileName(),
				(long) base64binary.length));
	}
}
