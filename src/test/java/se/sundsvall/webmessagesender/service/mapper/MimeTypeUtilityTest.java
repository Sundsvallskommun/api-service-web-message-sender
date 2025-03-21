package se.sundsvall.webmessagesender.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.StreamUtils.copyToByteArray;

import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.io.ClassPathResource;

class MimeTypeUtilityTest {

	private static final String PATH = "mimetype_files/";
	private static final String IMG_FILE_NAME = "image.jpg";
	private static final String DOC_FILE_NAME = "document.doc";
	private static final String DOCX_FILE_NAME = "document.docx";
	private static final String PDF_FILE_NAME = "document.pdf";
	private static final String TXT_FILE_NAME = "document.txt";

	@Test
	void detectMimeTypeThrowsException() {
		assertThat(MimeTypeUtility.detectMimeType(null, null)).isEqualTo("application/octet-stream");
	}

	@ParameterizedTest
	@MethodSource("mimeTypeArguments")
	void detectMimeType(String fileName, byte[] fileBytes, String expectedTypeWithFilename, String expectedTypeWithoutFilename) {
		assertThat(MimeTypeUtility.detectMimeType(fileName, fileBytes)).isEqualTo(expectedTypeWithFilename);
		assertThat(MimeTypeUtility.detectMimeType(null, fileBytes)).isEqualTo(expectedTypeWithoutFilename);
	}

	private static Stream<Arguments> mimeTypeArguments() throws IOException {
		return Stream.of(
			Arguments.of(IMG_FILE_NAME, copyToByteArray(new ClassPathResource(PATH + IMG_FILE_NAME).getInputStream()), "image/jpeg", "image/jpeg"),
			Arguments.of(DOC_FILE_NAME, copyToByteArray(new ClassPathResource(PATH + DOC_FILE_NAME).getInputStream()), "application/msword", "application/msword"),
			Arguments.of(DOCX_FILE_NAME, copyToByteArray(new ClassPathResource(PATH + DOC_FILE_NAME).getInputStream()), "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword"),
			Arguments.of(PDF_FILE_NAME, copyToByteArray(new ClassPathResource(PATH + PDF_FILE_NAME).getInputStream()), "application/pdf", "application/pdf"),
			Arguments.of(TXT_FILE_NAME, copyToByteArray(new ClassPathResource(PATH + TXT_FILE_NAME).getInputStream()), "text/plain", "text/plain"));
	}
}
