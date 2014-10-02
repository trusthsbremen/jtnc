package org.ietf.nea.pb.message;

import org.ietf.nea.pb.exception.RuleException;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValueBuilder;

public interface PbMessageValueAccessRecommendationBuilder extends TnccsMessageValueBuilder{

	public abstract PbMessageValueAccessRecommendationBuilder setRecommendation(short recommendation)
			throws RuleException;

}