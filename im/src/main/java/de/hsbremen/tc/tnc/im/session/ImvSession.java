package de.hsbremen.tc.tnc.im.session;

import de.hsbremen.tc.tnc.exception.TncException;

public interface ImvSession extends ImSession, ImSessionContext{

	public abstract void solicitRecommendation() throws TncException;

}
