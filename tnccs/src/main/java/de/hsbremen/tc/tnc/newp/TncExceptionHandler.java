package de.hsbremen.tc.tnc.newp;

import de.hsbremen.tc.tnc.exception.TncException;
@Deprecated
public interface TncExceptionHandler {

	public void handleException(TncException e);
	public void handleException(long imcId, TncException e);
}
