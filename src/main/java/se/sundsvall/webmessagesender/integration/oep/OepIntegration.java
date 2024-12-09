package se.sundsvall.webmessagesender.integration.oep;

import org.springframework.stereotype.Component;
import se.sundsvall.webmessagesender.generatedsources.oep.AddMessage;
import se.sundsvall.webmessagesender.generatedsources.oep.AddMessageResponse;
import se.sundsvall.webmessagesender.integration.oep.client.OepExternalClient;
import se.sundsvall.webmessagesender.integration.oep.client.OepInternalClient;

@Component
public class OepIntegration {

	private final OepExternalClient oepExternalClient;
	private final OepInternalClient oepInternalClient;

	public OepIntegration(final OepExternalClient oepExternalClient, final OepInternalClient oepInternalClient) {
		this.oepExternalClient = oepExternalClient;
		this.oepInternalClient = oepInternalClient;
	}

	public AddMessageResponse addMessage(final String instance, final AddMessage addMessage) {
		if (instance.equalsIgnoreCase("internal")) {
			return oepInternalClient.addMessage(addMessage);
		}
		if (instance.equalsIgnoreCase("external")) {
			return oepExternalClient.addMessage(addMessage);
		}
		return null;
	}

}
