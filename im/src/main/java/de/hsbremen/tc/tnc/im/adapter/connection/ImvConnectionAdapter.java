package de.hsbremen.tc.tnc.im.adapter.connection;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;

public interface ImvConnectionAdapter extends ImConnectionAdapter {

	public abstract void provideRecommendation(ImvRecommendationPair pair) throws TncException;
}
