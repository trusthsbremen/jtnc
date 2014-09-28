package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValueBuilder;

public interface PbMessageValueAccessRecommendationBuilder extends TnccsMessageValueBuilder{

	public abstract void setRecommendation(short recommendation)
			throws ValidationException;

}