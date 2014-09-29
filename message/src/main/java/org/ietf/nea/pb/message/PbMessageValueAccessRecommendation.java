package org.ietf.nea.pb.message;

import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;

/**
 * Reference IETF RFC 5793 section 4.7:
 * ------------------------------------
 * The PB-TNC message type named PB-Access-Recommendation (value 3) is
   used by the Posture Broker Server to provide an access recommendation
   after the Posture Broker Server has completed some assessment of the
   endpoint.  The PB-Assessment-Result and the PB-Access-Recommendation
   attribute together constitute the global assessment decision for an
   endpoint.  The PB-Access-Recommendation is not authoritative, and the
   network and host-based access control systems would typically use
   additional information to determine the network access that is
   granted to the endpoint.  The Posture Broker Server MAY include one
   message of this type in any batch of type RESULT and MUST NOT include
   a message of this type in any other type of batch.  Posture Broker
   Clients MUST NOT send a PB-TNC message with this message type.  If a
   Posture Broker Server receives a PB-TNC message with this message
   type, it MUST respond with a fatal Invalid Parameter error in a CLOSE 
   batch.  The Posture Broker Client MUST implement and process this
   message and MUST ignore any message with this message type that is
   not part of a batch of type RESULT.

   The NOSKIP flag in the PB-TNC Message Header MUST NOT be set for this
   message type.  Any Posture Broker Client or Posture Broker Server
   that receives a PB-Access-Recommendation message with the NOSKIP flag
   set MUST ignore the message and MUST respond with a fatal Invalid
   Parameter error code in a CLOSE batch.
   
   The PB-TNC Message Length field MUST contain the value 16 since 
   that is the total of the length of the fixed-length fields at the start 
   of the PB-TNC message (the fields Flags, PB-TNC Vendor ID, PB-TNC Message 
   Type, and PB-TNC Message Length) along with the Access Recommendation 
   field.
 */
public class PbMessageValueAccessRecommendation extends AbstractPbMessageValue{

    protected final short reserved;   // 16 bit(s) should be 0
    
    private final PbMessageAccessRecommendationEnum recommendation;  //16 bit(s)

    
    
    PbMessageValueAccessRecommendation(final short reserved,final long length,
			final PbMessageAccessRecommendationEnum recommendation) {
		super(length);
		this.reserved = reserved;
		this.recommendation = recommendation;
	}

	/**
     * @return the recommendationEnum
     */
    public PbMessageAccessRecommendationEnum getRecommendation() {
        return recommendation;
        
    }

	/**
	 * @return the reserved
	 */
	public short getReserved() {
		return this.reserved;
	}
    
}
