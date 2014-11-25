package de.hsbremen.tc.tnc.adapter.im;

import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;

import de.hsbremen.tc.tnc.exception.TncException;

public interface ImvAdapter extends ImAdapter<IMVConnection> {

	public void solicitRecommendation(IMVConnection connection) throws TncException, TerminatedException;
}
