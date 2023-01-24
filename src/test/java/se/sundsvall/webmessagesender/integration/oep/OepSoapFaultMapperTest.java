package se.sundsvall.webmessagesender.integration.oep;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;
import static org.zalando.problem.Status.NOT_FOUND;
import static org.zalando.problem.Status.UNAUTHORIZED;

import java.util.List;

import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.ThrowableProblem;

@ExtendWith(MockitoExtension.class)
class OepSoapFaultMapperTest {

	private static final String ACCESS_DENIED_FAULT = "AccessDeniedFault";
	private static final String ACCESS_DENIED_FAULT_STRING = "User not authorized for the operation";
	private static final String FLOW_INSTANCE_NOT_FOUND_FAULT = "FlowInstanceNotFoundFault";
	private static final String FLOW_INSTANCE_NOT_FOUND_FAULT_STRING = "The requested flow instance was not found";
	private static final String UNHANDLED_FAULT = "UnhandledFault";
	private static final String UNHANDLED_FAULT_STRING = "This is an unhandled fault";

	@Mock
	private SOAPFaultException soapFaultException;

	@Mock
	private SOAPFault soapFault;

	@Mock
	private Detail detail;

	@Mock
	private DetailEntry detailEntry;

	@Test
	void testAccessDeniedFault() {
		when(soapFaultException.getFault()).thenReturn(soapFault);
		when(soapFault.getFaultString()).thenReturn(ACCESS_DENIED_FAULT_STRING);
		when(soapFault.getDetail()).thenReturn(detail);
		when(detail.getDetailEntries()).thenReturn(List.of(detailEntry).iterator());
		when(detailEntry.getLocalName()).thenReturn(ACCESS_DENIED_FAULT);

		ThrowableProblem problem = OepSoapFaultMapper.convertToThrowableProblem(soapFaultException);

		assertThat(problem.getStatus()).isEqualTo(UNAUTHORIZED);
		assertThat(problem.getTitle()).isEqualTo(UNAUTHORIZED.getReasonPhrase());
		assertThat(problem.getDetail()).isEqualTo(ACCESS_DENIED_FAULT_STRING);
	}

	@Test
	void testFlowInstanceNotFoundFault() {
		when(soapFaultException.getFault()).thenReturn(soapFault);
		when(soapFault.getFaultString()).thenReturn(FLOW_INSTANCE_NOT_FOUND_FAULT_STRING);
		when(soapFault.getDetail()).thenReturn(detail);
		when(detail.getDetailEntries()).thenReturn(List.of(detailEntry).iterator());
		when(detailEntry.getLocalName()).thenReturn(FLOW_INSTANCE_NOT_FOUND_FAULT);

		ThrowableProblem problem = OepSoapFaultMapper.convertToThrowableProblem(soapFaultException);

		assertThat(problem.getStatus()).isEqualTo(NOT_FOUND);
		assertThat(problem.getTitle()).isEqualTo(NOT_FOUND.getReasonPhrase());
		assertThat(problem.getDetail()).isEqualTo(FLOW_INSTANCE_NOT_FOUND_FAULT_STRING);
	}

	@Test
	void testUnhandledFault() {
		when(soapFaultException.getFault()).thenReturn(soapFault);
		when(soapFault.getFaultString()).thenReturn(UNHANDLED_FAULT_STRING);
		when(soapFault.getDetail()).thenReturn(detail);
		when(detail.getDetailEntries()).thenReturn(List.of(detailEntry).iterator());
		when(detailEntry.getLocalName()).thenReturn(UNHANDLED_FAULT);

		ThrowableProblem problem = OepSoapFaultMapper.convertToThrowableProblem(soapFaultException);

		assertThat(problem.getStatus()).isEqualTo(INTERNAL_SERVER_ERROR);
		assertThat(problem.getTitle()).isEqualTo(INTERNAL_SERVER_ERROR.getReasonPhrase());
		assertThat(problem.getDetail()).isEqualTo(UNHANDLED_FAULT_STRING);
	}

	@Test
	void testMultipleDetailEntries() {
		when(soapFaultException.getFault()).thenReturn(soapFault);
		when(soapFault.getFaultString()).thenReturn(FLOW_INSTANCE_NOT_FOUND_FAULT_STRING);
		when(soapFault.getDetail()).thenReturn(detail);
		when(detail.getDetailEntries()).thenReturn(List.of(detailEntry, detailEntry, detailEntry).iterator());
		when(detailEntry.getLocalName()).thenReturn(UNHANDLED_FAULT, FLOW_INSTANCE_NOT_FOUND_FAULT, UNHANDLED_FAULT);

		ThrowableProblem problem = OepSoapFaultMapper.convertToThrowableProblem(soapFaultException);

		assertThat(problem.getStatus()).isEqualTo(NOT_FOUND);
		assertThat(problem.getTitle()).isEqualTo(NOT_FOUND.getReasonPhrase());
		assertThat(problem.getDetail()).isEqualTo(FLOW_INSTANCE_NOT_FOUND_FAULT_STRING);
	}
}
