package org.ietf.nea.pt.value;

import java.util.Arrays;

/**
 * Reference IETF RFC 6876 section 3.9:
 * ------------------------------------
 * When a PT-TLS error is received, the recipient MUST NOT respond with
 * a PT-TLS error, because this could result in an infinite loop of
 * error messages being sent.  Instead, the recipient MAY log the error,
 * modify its behavior to avoid future errors, ignore the error,
 * terminate the assessment, or take other action as appropriate (as
 * long as it is consistent with the requirements of this
 * specification).
 * 
 * This variable-length value MUST contain a copy (up to 1024 bytes)
 * of the original PT-TLS message that caused the error.  If the
 * original message is longer than 1024 bytes, only the initial 1024
 * bytes will be included in this field. 
 * 
 */
public class PtTlsMessageValueError extends AbstractPtTlsMessageValue{
    
    private final long errorVendorId;                           // 24 bit(s)
    private final long errorCode;                               // 32 bit(s)
    private final byte[] partialMessageCopy; 					// max 1024 byte(s)

	PtTlsMessageValueError( final long errorVendorId,
			final long errorCode, final long length,
			final byte[] partialMessageCopy) {
		super(length);
		

		this.errorVendorId = errorVendorId;
		this.errorCode = errorCode;
		this.partialMessageCopy = partialMessageCopy;
	}

	/**
	 * @return the errVendorId
	 */
	public long getErrorVendorId() {
		return this.errorVendorId;
	}

	/**
	 * @return the errCode
	 */
	public long getErrorCode() {
		return this.errorCode;
	}

	/**
	 * @return the partialMessageCopy
	 */
	public byte[] getPartialMessageCopy() {
		return Arrays.copyOf(this.partialMessageCopy, this.partialMessageCopy.length);
	}

	

}
