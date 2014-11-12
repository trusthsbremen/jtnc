package de.hsbremen.tc.tnc.im.adapter.connection;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.evaluate.ImvRecommendationObject;

public interface ImvConnectionAdapter extends ImConnectionAdapter {

	public abstract void provideRecommendation(ImvRecommendationObject pair) throws TncException;
}
