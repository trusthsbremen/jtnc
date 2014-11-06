package de.hsbremen.tc.tnc.im.evaluate;

import java.util.ArrayList;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttributeValueAssessmentResult;
import org.ietf.nea.pa.attribute.PaAttributeValueAttributeRequest;
import org.ietf.nea.pa.attribute.PaAttributeValueError;
import org.ietf.nea.pa.attribute.PaAttributeValueRemediationParameters;
import org.ietf.nea.pa.attribute.util.AttributeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.m.attribute.ImAttribute;

public class DefaultImEvaluationUnit extends AbstractImEvaluationUnitIetf{


	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultImEvaluationUnit.class);
	
	public final static long VENDOR_ID = TNCConstants.TNC_VENDORID_ANY;
	public final static long TYPE = TNCConstants.TNC_SUBTYPE_ANY;
	
	protected DefaultImEvaluationUnit(
			GlobalHandshakeRetryListener globalHandshakeRetryListener) {
		super(globalHandshakeRetryListener);
	}

	@Override
	public long getVendorId() {
		return VENDOR_ID;
	}

	@Override
	public long getType() {
		return TYPE;
	}

	@Override
	public List<ImAttribute> evaluate(ImSessionContext context) {
		
		LOGGER.debug("evaluate() called, with connection state: " + context.getConnectionState());
		
		return new ArrayList<>(0);
	}

	@Override
	public void terminate() {
		LOGGER.debug("terminate() called.");
		
	}

	@Override
	protected List<? extends ImAttribute> handleAttributeRequest(
			PaAttributeValueAttributeRequest value, ImSessionContext context) {
		
		List<AttributeReference> references = value.getReferences();
		StringBuilder b = new StringBuilder();
		if(references != null){
			for (AttributeReference ref: references) {
				b.append(ref.toString());
				b.append("\n");
			}
		}
		
		LOGGER.debug("handleAttributeRequest() called, with connection state: " + context.getConnectionState() +"\n"
					+ "Requested attributes:\n"
					+ b.toString()
				);

		return new ArrayList<>(0);
	}

	@Override
	protected List<? extends ImAttribute> handleError(
			PaAttributeValueError value, ImSessionContext context) {
		LOGGER.debug("handleError() called, with connection state: " + context.getConnectionState() +"\n"
					+ "Error vendor ID (" + value.getErrorVendorId() +"), error code (" + value.getErrorCode() +"), "
					+ "and information type " + value.getErrorInformation().getClass().getSimpleName() + ".");
		
		return new ArrayList<>(0);
	}

	@Override
	protected List<? extends ImAttribute> handleRemediation(
			PaAttributeValueRemediationParameters value,
			ImSessionContext context) {
		
		LOGGER.debug("handleRemediation() called, with connection state: " + context.getConnectionState() +"\n"
				+ "Remediation vendor ID (" + value.getRpVendorId() +"), error code (" + value.getRpType() +"), "
				+ "and parameter type " + value.getParameter().getClass().getSimpleName() + ".");
		
		return new ArrayList<>(0);
	}

	@Override
	protected List<? extends ImAttribute> handleResult(
			PaAttributeValueAssessmentResult value, ImSessionContext context) {
		
		LOGGER.debug("handleResult() called, with connection state: " + context.getConnectionState() + "\n"
				 + "Result: " + value.getResult().toString());
		
		return new ArrayList<>(0);
	}

	@Override
	public List<ImAttribute> lastCall(ImSessionContext context) {
		
		LOGGER.debug("lastCall() called, with connection state: " + context.getConnectionState());
		
		return new ArrayList<>(0);
	}

}
