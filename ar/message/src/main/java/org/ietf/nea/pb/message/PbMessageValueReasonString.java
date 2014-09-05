package org.ietf.nea.pb.message;

import java.nio.charset.Charset;



/**
*
* Reference IETF RFC 5793 section 4.10:
* ------------------------------------
* The PB-TNC message type named PB-Reason-String (value 7) is used by
* the Posture Broker Server to provide a human-readable explanation for
* the global assessment decision conveyed in the PB-Assessment-Result &
* PB-Access-Recommendation messages.  Therefore, a PB-Reason-String 
* message SHOULD only be included in the same batch as the PB-
* Assessment-Result and PB-Access-Recommendation message.  The Posture
* Broker Client MUST NOT ever send a PB-Reason-String message.
*  
* The Posture Broker Client is not required to implement this message
* type and the Posture Broker Server is not required to send it. 
* The Posture Broker Client MAY display the message to the user, log it, 
* ignore it, or take any other action that is not inconsistent with the 
* requirements of this specification. Since the strings contained in this 
* message are human-readable, the Posture Broker Server SHOULD adapt them 
* to the Posture Broker Client's language preferences as expressed in any PB-
* Language-Preference message sent by the Posture Broker Client in this
* PB-TNC session.
*  
* A Posture Broker Server MAY include more than one message of this
* type in any batch of any type.  However, it is suggested that this
* message be included in the same batch as the PB-Assessment-Result and
* PB-Access-Recommendation message.  If more than one PB-Reason-String
* message is included in a single batch, the Posture Broker Client
* SHOULD consider the strings included in these messages to be
* equivalent in meaning.  This allows the Posture Broker Server to
* return multiple equivalent reason strings in different languages,
* which may help if the Posture Broker Server is not able to
* accommodate the Posture Broker Client's language preferences.
*  
* The NOSKIP flag in the PB-TNC Message Header MUST NOT be set for this
* message type.
* 
* Since the Reason String and Reason String Language Code fields are 
* variable length, the value in the PB-TNC Message Length field will 
* vary also.  However, it MUST always be at least 17 to cover the 
* fixed-length fields.
*
*/

public class PbMessageValueReasonString extends AbstractPbMessageValue{

    public static final byte FIXED_LENGTH = 5;

    private final long stringLength;          // 32 bit(s) length of the string in octets
    private final String reasonString;        // variable length, UTF-8 encoded, NUL termination MUST NOT be included.
    private final byte langCodeLength;        // 8 bit(s) length of language code in octets, 0 = language unknown
    private final String langCode;            // variable length, US-ASCII string composed of a well-formed RFC 4646 [3] language tag

    
    
	public PbMessageValueReasonString(String reasonString, String langCode) {
		super(FIXED_LENGTH + reasonString.getBytes(Charset.forName("UTF-8")).length 
				+ langCode.getBytes(Charset.forName("US-ASCII")).length);
		this.reasonString = reasonString;
		this.stringLength = reasonString.getBytes(Charset.forName("UTF-8")).length;
		this.langCode = langCode;
		this.langCodeLength = (byte)langCode.getBytes(Charset.forName("US-ASCII")).length;
	}

	/**
	 * @return the stringLength
	 */
	public long getStringLength() {
		return this.stringLength;
	}

	/**
	 * @return the langCodeLength
	 */
	public byte getLangCodeLength() {
		return this.langCodeLength;
	}


	/**
	 * @return the reasonString
	 */
	public String getReasonString() {
		return this.reasonString;
	}

	/**
	 * @return the langCode
	 */
	public String getLangCode() {
		return this.langCode;
	}

}
