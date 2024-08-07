package apptest;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.webmessagesender.Application;

@WireMockAppTestSuite(
	files = "classpath:/GetWebMessage/",
	classes = Application.class)
@Sql(scripts = {
	"/db/scripts/truncate.sql",
	"/db/scripts/test-it.sql"
})
class DeleteWebMessageIT extends AbstractAppTest {

	@Test
	void test1_deleteWebmessageById() {
		final var id = "e535c9f7-c473-44f2-81d5-a8fbfcc932ea";
		setupCall()
			.withServicePath("/2281/webmessages/" + id)
			.withHttpMethod(DELETE)
			.withExpectedResponseStatus(NO_CONTENT)
			.sendRequestAndVerifyResponse();
	}
}
