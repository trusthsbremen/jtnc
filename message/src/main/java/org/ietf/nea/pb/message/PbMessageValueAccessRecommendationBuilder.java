package org.ietf.nea.pb.message;

import org.ietf.nea.exception.RuleException;

import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageValueBuilder;

public interface PbMessageValueAccessRecommendationBuilder extends TnccsMessageValueBuilder{

	public abstract PbMessageValueAccessRecommendationBuilder setRecommendation(int recommendation)
			throws RuleException;

}