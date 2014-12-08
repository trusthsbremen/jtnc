package de.hsbremen.tc.tnc.tnccs.adapter.im;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImvConnectionAdapter;
import de.hsbremen.tc.tnc.tnccs.adapter.im.exception.TerminatedException;

public interface ImvAdapter extends ImAdapter<ImvConnectionAdapter> {

	public void solicitRecommendation(ImvConnectionAdapter connection) throws TncException, TerminatedException;
}
