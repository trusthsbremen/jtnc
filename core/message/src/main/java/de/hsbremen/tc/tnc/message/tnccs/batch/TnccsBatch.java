package de.hsbremen.tc.tnc.message.tnccs.batch;

import java.util.List;

import de.hsbremen.tc.tnc.message.tnccs.TnccsData;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;

/**
 * Marker for TNCCS batch classes.
 * @author sidanetdev
 *
 */
public interface TnccsBatch extends TnccsData{

	public TnccsBatchHeader getHeader();
	
	public List<? extends TnccsMessage> getMessages();
}
