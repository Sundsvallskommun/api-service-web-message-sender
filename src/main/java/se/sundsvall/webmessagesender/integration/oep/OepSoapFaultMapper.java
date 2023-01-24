package se.sundsvall.webmessagesender.integration.oep;

import static java.util.Objects.nonNull;
import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;
import static org.zalando.problem.Status.NOT_FOUND;
import static org.zalando.problem.Status.UNAUTHORIZED;

import javax.xml.ws.soap.SOAPFaultException;

import org.zalando.problem.Problem;
import org.zalando.problem.ThrowableProblem;
import se.sundsvall.dept44.exception.ClientProblem;
import se.sundsvall.dept44.exception.ServerProblem;

public class OepSoapFaultMapper {

	private static final String ACCESS_DENIED_FAULT = "AccessDeniedFault";
	private static final String FLOW_INSTANCE_NOT_FOUND_FAULT = "FlowInstanceNotFoundFault";

	private OepSoapFaultMapper() {}

	/**
	 * Helper method for converting SOAPFaultException objects to a throwable problem. Incoming fault has the following
	 * structure:
	 * 
	 * <pre>
	 *	<S:Fault xmlns:ns4="http://www.w3.org/2003/05/soap-envelope">
	 *		<faultcode>S:Server</faultcode>
	 *		<faultstring>The requested flow instance was not found</faultstring>
	 *		<detail>
	 *			<FlowInstanceNotFoundFault xmlns="http://www.oeplatform.org/version/1.0/schemas/integration/callback"/>
	 *		</detail>
	 *	</S:Fault>
	 * </pre>
	 * 
	 * which the method interprets and converts to a throwable problem. If detail contains a child with one of the known
	 * faults
	 * that can be thrown by OEP, a throwable problem containing corresponding status code will be returned. In all other
	 * cases
	 * a throwable problem with internal server error as status code will be returned.
	 * 
	 * @param soapFaultException to be interpreted and converted to a throwable problem
	 * @return a throwable problem representation of the SOAPFaultException
	 */
	public static ThrowableProblem convertToThrowableProblem(SOAPFaultException soapFaultException) {
		var wrapper = new Object() { ThrowableProblem problem = null; };

		soapFaultException
			.getFault()
			.getDetail()
			.getDetailEntries()
			.forEachRemaining(entry -> {
				ThrowableProblem throwableProblem = switch (entry.getLocalName()) {
					case ACCESS_DENIED_FAULT -> new ServerProblem(UNAUTHORIZED, soapFaultException.getFault().getFaultString());
					case FLOW_INSTANCE_NOT_FOUND_FAULT -> new ClientProblem(NOT_FOUND, soapFaultException.getFault().getFaultString());
					default -> null;
				};
				if (nonNull(throwableProblem))
					wrapper.problem = throwableProblem;
			});

		return nonNull(wrapper.problem) ? wrapper.problem : Problem.valueOf(INTERNAL_SERVER_ERROR, soapFaultException.getFault().getFaultString());
	}
}
