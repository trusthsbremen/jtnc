package de.hsbremen.tc.tnc.im.evaluate.example.simple;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.im.evaluate.AbstractImEvaluationUnitIetf;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluationUnit;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.m.attribute.ImAttribute;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;
import de.hsbremen.tc.tnc.report.ImvRecommendationPairFactory;

class DefaultImvEvaluationUnit extends AbstractImEvaluationUnitIetf implements ImvEvaluationUnit{


	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultImvEvaluationUnit.class);
	
	public final static long VENDOR_ID = TNCConstants.TNC_VENDORID_ANY;
	public final static long TYPE = TNCConstants.TNC_SUBTYPE_ANY;
	
	DefaultImvEvaluationUnit(
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
	public synchronized List<ImAttribute> evaluate(ImSessionContext context) {
		
		LOGGER.debug("evaluate() called, with connection state: " + context.getConnectionState());
		
		return new ArrayList<>(0);
	}

	@Override
	public void terminate() {
		LOGGER.debug("terminate() called.");
		
	}

	@Override
	public synchronized List<ImAttribute> lastCall(ImSessionContext context) {
		
		LOGGER.debug("lastCall() called, with connection state: " + context.getConnectionState());
		
		return new ArrayList<>(0);
	}

	@Override
	public synchronized List<ImAttribute> handle(List<? extends ImAttribute> components,
			ImSessionContext context) {
		StringBuilder b = new StringBuilder();
		b.append("handleCall() called, with connection state: ")
		 .append(context.getConnectionState())
		 .append("\n");
		
		for (ImAttribute imAttribute : components) {
			b.append("Attribute received: [ID = ")
			
			.append(imAttribute.getHeader().getVendorId())
			.append(", Type = ")
			.append(imAttribute.getHeader().getAttributeType())
			.append(" ]\n");
		}
		
		LOGGER.debug(b.toString());
		
		return new ArrayList<>(0);
	}

	@Override
	public synchronized ImvRecommendationPair getRecommendation(ImSessionContext context) {
		ImvRecommendationPair object = ImvRecommendationPairFactory.getDefaultRecommendationPair();
		LOGGER.debug("getRecommendation() called, with connection state: " + context.getConnectionState() + "\n" + object.toString() );
		return object;
	}

	@Override
	public boolean hasRecommendation() {
		LOGGER.debug("hasRecommendation() called. - TRUE");
		return true;
	}

}
