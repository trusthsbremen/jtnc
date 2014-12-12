package org.ietf.nea.pt.value;

import de.hsbremen.tc.tnc.message.util.ByteBuffer;


/**
 * Reference IETF RFC 6876 section 3.6:
 * --------------------------------------
 * Contains a PB-TNC batch.  For more information on PB-TNC batches, 
 * see RFC 5793 (PB-TNC) Section 4. This message type MUST only be sent 
 * when the NEA Client and NEA Server are in the PT-TLS Data Transport 
 * phase. Recipients SHOULD send an Invalid Message error code in a 
 * PT-TLS Error message if a PB-TNC Batch is received outside of the 
 * Data Transport phase.
 * 
 * These bytes must not be interpreted by the PT-TLS layer, in that PT-TLS 
 * is not dependent on PB-TNC for its transfer state decisions.
 * 
 */

public class PtTlsMessageValuePbBatch extends AbstractPtTlsMessageValue{

    private final ByteBuffer tnccsData;

	PtTlsMessageValuePbBatch(final long length, ByteBuffer tnccsData) {
		super(length);
		this.tnccsData = tnccsData;
	}

	public ByteBuffer getTnccsData() {
		return tnccsData;
	}
}
