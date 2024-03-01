package apptest;

import static java.util.Objects.isNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.webmessagesender.Application;
import se.sundsvall.webmessagesender.integration.db.WebMessageRepository;
import se.sundsvall.webmessagesender.integration.db.model.WebMessageEntity;

@WireMockAppTestSuite(
    files = "classpath:/CreateWebMessage/",
    classes = Application.class
)
class CreateWebMessageIT extends AbstractAppTest {

	@Autowired
	private WebMessageRepository repository;
	
    @Test
    void test1_createSuccessful() {
        setupCall()
            .withServicePath("/webmessages")
            .withHttpMethod(POST)
            .withRequest("request.json")
            .withExpectedResponseStatus(CREATED)
            .withExpectedResponseHeader(LOCATION, List.of("^/webmessages/(.*)$"))
            .sendRequestAndVerifyResponse();

        var webMessages = repository.findByExternalReferencesKeyAndExternalReferencesValueOrderByCreated("flowInstanceId", "356434");
        assertThat(webMessages).hasSize(1);
    }

    @Test
    void test2_timeoutFromOep() {
        setupCall()
            .withServicePath("/webmessages")
            .withHttpMethod(POST)
            .withRequest("request.json")
            .withExpectedResponseStatus(INTERNAL_SERVER_ERROR)
            .withExpectedResponse("response.json")
            .sendRequestAndVerifyResponse();
    }

    @Test
    void test3_instanceIdNotFound() {
        setupCall()
            .withServicePath("/webmessages")
            .withHttpMethod(POST)
            .withRequest("request.json")
            .withExpectedResponseStatus(NOT_FOUND)
            .withExpectedResponse("response.json")
            .sendRequestAndVerifyResponse();
    }

    @Test
    void test4_createWithAttachmentSuccessful() {
        setupCall()
            .withServicePath("/webmessages")
            .withHttpMethod(POST)
            .withRequest("request.json")
            .withExpectedResponseStatus(CREATED)
            .withExpectedResponseHeader(LOCATION, List.of("^/webmessages/(.*)$"))
            .sendRequestAndVerifyResponse();

        assertThat(repository.findByExternalReferencesKeyAndExternalReferencesValueOrderByCreated("flowInstanceId", "2154"))
            .extracting(WebMessageEntity::getAttachments).hasSize(1);
    }
}
