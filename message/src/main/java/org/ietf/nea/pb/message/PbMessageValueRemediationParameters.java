package org.ietf.nea.pb.message;

import org.ietf.nea.pb.message.util.AbstractPbMessageValueRemediationParameter;

/**
 * Reference IETF RFC 5793 section 4.8:
 * ------------------------------------
 * The PB-TNC message type named PB-Remediation-Parameters (value 4) is
 * used by the Posture Broker Server to provide global (not Posture
 * Validator-specific) remediation parameters after the Posture Broker
 * Server has completed some assessment of the endpoint.  The Posture
 * Broker Server MAY include one or more messages of this type in any
 * batch of any type, but this message type is most useful in batches of
 * type RESULT.
 * 
 * The Posture Broker Client MUST NOT send a PB-TNC message with this
 * message type.  If a Posture Broker Server receives a PB-TNC message
 * with this message type, it MUST respond with a fatal Invalid
 * Parameter error in a CLOSE batch.  The Posture Broker Client may
 * implement and process this message but is not required to do so.  It
 * may skip this message.  Even if the Posture Broker Client implements
 * this message type, it is not obligated to act on it.
 * 
 * The NOSKIP flag in the PB-TNC Message Header MUST NOT be set for this
 * message type.
 * 
 * Since the Remediation Parameters field is variable length, the value 
 * in the PB-TNC Message Length field will vary also.  However, it 
 * MUST always be at least 20 to cover the fixed-length fields
 * 
 */
public class PbMessageValueRemediationParameters extends AbstractPbMessageValue{

    private final long rpVendorId;         // 24 bit(s)
    private final long rpType;             // 32 bit(s)
    
    private final AbstractPbMessageValueRemediationParameter parameter;
    

    
    
    PbMessageValueRemediationParameters(final long length,
			final long rpVendorId, final long rpType,
			final AbstractPbMessageValueRemediationParameter parameter) {
		super(length);
		this.rpVendorId = rpVendorId;
		this.rpType = rpType;
		this.parameter = parameter;
	}


	/**
	 * @return the rpVendorId
	 */
	public long getRpVendorId() {
		return this.rpVendorId;
	}


	/**
	 * @return the rpType
	 */
	public long getRpType() {
		return this.rpType;
	}

	public AbstractPbMessageValueRemediationParameter getParameter(){
		return this.parameter;
	}
}
