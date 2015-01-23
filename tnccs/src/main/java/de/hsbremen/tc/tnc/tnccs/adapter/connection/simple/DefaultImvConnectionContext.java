package de.hsbremen.tc.tnc.tnccs.adapter.connection.simple;

import java.util.HashMap;
import java.util.Map;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.AbstractImConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImvConnectionContext;
import de.hsbremen.tc.tnc.tnccs.session.base.HandshakeRetryListener;

//FIXME This is a tradeoff, because I could not figure out a way to fix the 
//circular dependency between session and IMC/VConnection.
public class DefaultImvConnectionContext extends AbstractImConnectionContext implements ImvConnectionContext{

	private final Map<Long,ImvRecommendationPair> recommendations;

	
	public DefaultImvConnectionContext(Attributed attributes, HandshakeRetryListener listener) {
		super(attributes,listener);
		this.recommendations = new HashMap<>();
	}

	@Override
	public void addRecommendation(long id,
			ImvRecommendationPair recommendationPair) throws TncException {
		if(super.isValid()){
			this.recommendations.put(new Long(id), recommendationPair);
		}else{
			throw new TncException("Cannot add message. Session and connection may be closed.", TncExceptionCodeEnum.TNC_RESULT_ILLEGAL_OPERATION);
		}
		
	}

	@Override
	public Map<Long, ImvRecommendationPair> clearRecommendations() {
		Map<Long,ImvRecommendationPair> r = new HashMap<>(this.recommendations);
		this.recommendations.clear();
		
		return r;
	}
	
}
