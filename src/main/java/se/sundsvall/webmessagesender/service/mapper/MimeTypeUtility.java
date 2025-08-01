package se.sundsvall.webmessagesender.service.mapper;

import static org.springframework.util.MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE;
import static se.sundsvall.dept44.util.LogUtils.sanitizeForLogging;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.overviewproject.mime_types.MimeTypeDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MimeTypeUtility {

	private static final Logger LOGGER = LoggerFactory.getLogger(MimeTypeUtility.class);
	private static final MimeTypeDetector DETECTOR = new MimeTypeDetector();

	private MimeTypeUtility() {}

	public static String detectMimeType(String fileName, byte[] byteArray) {
		try (InputStream stream = new ByteArrayInputStream(byteArray)) {
			return DETECTOR.detectMimeType(fileName, stream);
		} catch (final Exception e) {
			LOGGER.error(String.format("Exception when detecting mime type of file with filename '%s'", sanitizeForLogging(fileName)), e);
			return APPLICATION_OCTET_STREAM_VALUE; // Return mime type for arbitrary binary files
		}
	}
}
