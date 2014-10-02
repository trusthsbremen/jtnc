package de.hsbremen.tc.tnc.tnccs.message;

/**
 * Marker for TNCCS message classes.
 * @author Carl-Heinz Genzel
 *
 */
public interface TnccsMessage {
	
	public TnccsMessageHeader getHeader();

	public TnccsMessageValue getValue();
}
