package apptest;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

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
class GetWebMessageIT extends AbstractAppTest {

	private static final String RESPONSE_FILE = "response.json";

	@Test
	void test1_getWebmessageById() {
		final var id = "1e098e28-d9ba-459c-94c7-5508be826c08";
		setupCall()
			.withServicePath("/2281/webmessages/" + id)
			.withHttpMethod(GET)
			.withExpectedResponse(RESPONSE_FILE)
			.withExpectedResponseStatus(OK)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test2_getWebmessagesByPartyId() {
		final var partyId = "b7bd0e55-0811-4d3a-91d9-6bab7fd9ce5e";
		setupCall()
			.withServicePath("/2281/webmessages/recipients/" + partyId)
			.withHttpMethod(GET)
			.withExpectedResponse(RESPONSE_FILE)
			.withExpectedResponseStatus(OK)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test3_getWebmessagesByExternalReferences() {
		final var key = "key1";
		final var value = "value1";
		setupCall()
			.withServicePath("/2281/webmessages/external-references/" + key + "/" + value)
			.withHttpMethod(GET)
			.withExpectedResponse(RESPONSE_FILE)
			.withExpectedResponseStatus(OK)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test4_getWebmessageWithAttachmentsById() {
		final var id = "37317c16-3fd6-11ed-b878-0242ac120002";
		setupCall()
			.withServicePath("/2281/webmessages/" + id)
			.withHttpMethod(GET)
			.withExpectedResponse(RESPONSE_FILE)
			.withExpectedResponseStatus(OK)
			.sendRequestAndVerifyResponse();
	}
}
