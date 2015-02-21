package de.hsbremen.tc.tnc.message.tnccs.message;

import de.hsbremen.tc.tnc.message.tnccs.TnccsData;

/**
 * Marker for TNCCS message classes.
 * @author Carl-Heinz Genzel
 *
 */
public interface TnccsMessage extends TnccsData{
	
	public TnccsMessageHeader getHeader();

	public TnccsMessageValue getValue();
}
