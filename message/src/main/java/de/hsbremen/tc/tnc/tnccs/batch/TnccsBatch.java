package de.hsbremen.tc.tnc.tnccs.batch;

import java.util.List;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;

/**
 * Marker for TNCCS batch classes.
 * @author sidanetdev
 *
 */
public interface TnccsBatch {

	public TnccsBatchHeader getHeader();
	
	public List<? extends TnccsMessage> getMessages();
}
