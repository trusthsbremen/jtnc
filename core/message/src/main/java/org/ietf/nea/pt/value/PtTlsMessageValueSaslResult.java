package org.ietf.nea.pt.value;

import java.util.Arrays;

import org.ietf.nea.pt.value.enums.PtTlsSaslResultEnum;


/**
 * Reference IETF RFC 6876 section 3.8.10:
 * --------------------------------------
 * This TLV is sent by the NEA Server at the conclusion of the SASL
 * exchange to indicate the authentication result.  Upon reception of a
 * SASL Result TLV indicating an Abort, the NEA Client MUST terminate
 * the current authentication conversation.  The recipient may retry the
 * authentication in the event of an authentication failure.  Similarly,
 * the NEA Server may request that additional SASL authentication(s) be
 * performed after the completion of a SASL mechanism by sending another
 * SASL Mechanisms TLV including any mechanisms dictated by its policy.
 * 
 * This field contains a variable-length set of additional data for a
 * successful result.  This field MUST be zero length unless the NEA
 * Server is returning a Result Code of Success and has more data to
 * return.
 * 
 */

public class PtTlsMessageValueSaslResult extends AbstractPtTlsMessageValue{

    private final PtTlsSaslResultEnum result;
    private final byte[] resultData;

	PtTlsMessageValueSaslResult(final long length, final PtTlsSaslResultEnum result) {
		this(length, result, new byte[0]);
	}

	PtTlsMessageValueSaslResult(final long length, final PtTlsSaslResultEnum result, byte[] resultData) {
		super(length);
		this.result = result;
		this.resultData = resultData;
	}

	/**
	 * @return the result
	 */
	public PtTlsSaslResultEnum getResult() {
		return this.result;
	}

	/**
	 * @return the resultData
	 */
	public byte[] getResultData() {
		return Arrays.copyOf(this.resultData, this.resultData.length);
	}
	
	

	
	
	
}
