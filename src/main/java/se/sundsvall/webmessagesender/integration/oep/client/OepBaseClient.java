package se.sundsvall.webmessagesender.integration.oep.client;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import se.sundsvall.webmessagesender.generatedsources.oep.AddMessage;
import se.sundsvall.webmessagesender.generatedsources.oep.AddMessageResponse;

public interface OepBaseClient {

	String TEXT_XML_UTF_8 = "text/xml; charset=UTF-8";

	@PostMapping(consumes = TEXT_XML_UTF_8, produces = TEXT_XML_UTF_8)
	AddMessageResponse addMessage(@RequestBody AddMessage addMessage);
}
