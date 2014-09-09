package org.ietf.nea.pb.message;

import org.ietf.nea.pb.message.enums.PbMessageAssessmentResultEnum;

/**
 * Reference IETF RFC 5793 section 4.6:
 * ------------------------------------
 * The PB-TNC message type named PB-Assessment-Result (value 2) is used 
 * by the Posture Broker Server to provide the assessment result after
 * the Posture Broker Server has completed the assessment of the
 * endpoint.  The Posture Broker Server will typically compute the
 * assessment result as a cumulative of the individual assessment
 * results received from the various Posture Validators; the algorithm
 * for computation of assessment result at the Posture Broker layer is
 * implementation specific and can also change based on policies in a
 * specific deployment.  The Posture Broker Server MUST include one
 * message of this type in any batch of type RESULT and MUST NOT include
 * a message of this type in any other type of batch.  The Posture
 * Broker Client MUST NOT send a PB-TNC message with this message type.
 * If a Posture Broker Server receives a PB-TNC message with this
 * message type, it MUST respond with a fatal Invalid Parameter error in
 * a CLOSE batch.  The Posture Broker Client MUST implement and process
 * this message and MUST ignore any message with this message type that
 * is not part of a batch of type RESULT.
 * 
 * The NOSKIP flag in the PB-TNC Message Header MUST be set for this
 * message type.
 *  
 * The PB-TNC Message Length field MUST contain the value 16 since that
 * is the total of the length of the fixed-length fields at the start of
 * the PB-TNC message (the fields Flags, PB-TNC Vendor ID, PB-TNC
 * Message Type, and PB-TNC Message Length) along with the Assessment
 * Result field described below. 
 * 
 */

public class PbMessageValueAssessmentResult extends AbstractPbMessageValue{
    
	public static final byte FIXED_LENGTH = 4;

    private final PbMessageAssessmentResultEnum result;                                        //32 bit(s)

	PbMessageValueAssessmentResult(final PbMessageAssessmentResultEnum result) {
		super(FIXED_LENGTH);
		this.result = result;
	}



	/**
     * @return the message
     */
    public PbMessageAssessmentResultEnum getResult() {
        return result;
    }
}
