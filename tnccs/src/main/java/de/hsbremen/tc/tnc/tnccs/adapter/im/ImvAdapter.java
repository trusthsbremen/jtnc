package de.hsbremen.tc.tnc.tnccs.adapter.im;

import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.tnccs.adapter.im.exception.TerminatedException;

public interface ImvAdapter extends ImAdapter<IMVConnection> {

	public void solicitRecommendation(IMVConnection connection) throws TncException, TerminatedException;
}
