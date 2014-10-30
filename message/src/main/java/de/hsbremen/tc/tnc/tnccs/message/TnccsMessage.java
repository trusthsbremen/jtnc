package de.hsbremen.tc.tnc.tnccs.message;

import de.hsbremen.tc.tnc.tnccs.TnccsData;

/**
 * Marker for TNCCS message classes.
 * @author Carl-Heinz Genzel
 *
 */
public interface TnccsMessage extends TnccsData{
	
	public TnccsMessageHeader getHeader();

	public TnccsMessageValue getValue();
}
