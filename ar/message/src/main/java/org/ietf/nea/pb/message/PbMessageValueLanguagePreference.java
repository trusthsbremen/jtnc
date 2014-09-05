package org.ietf.nea.pb.message;

import java.nio.charset.Charset;

/**
 *
 * Reference IETF RFC 5793 section 4.10:
 * ------------------------------------
 * The PB-TNC message type named PB-Language-Parameters (value 6) is
 * used by the Posture Broker Client to indicate which language or
 * languages it would prefer for any human-readable strings that might
 * be sent to it.  This allows the Posture Broker Server and Posture
 * Validators to adapt any messages they may send to the Posture Broker
 * Client's preferences (probably determined by the language preferences
 * of the endpoint's users).
 *
 * The Posture Broker Server may also send this message type to the
 * Posture Broker Client to indicate the Posture Broker Server's
 * language preferences, but this is not very useful since the Posture
 * Broker Client rarely sends human-readable strings to the Posture
 * Broker Server and, if it does, rarely can adapt those strings to the
 * preferences of the Posture Broker Server.
 *
 * No Posture Broker Client or Posture Broker Server is required to send
 * or implement this message type.
 * 
 * A Posture Broker Client or Posture Broker Server may include a
 * message of this type in any batch of any type.  However, it is
 * suggested that this message be included in the first batch sent by
 * the Posture Broker Client or Posture Broker Server in a PB-TNC
 * session so that the recipient can start adapting its human-readable
 * messages as soon as possible.  If one PB-Language-Parameters message
 * is received and then another one is received in a later batch for the
 * same PB-TNC session, the value included in the later message should
 * be considered to replace the value in the earlier message.
 * 
 * A Posture Broker Client or Posture Broker Server MUST NOT include
 * more than one message of this type in a single batch.  If a Posture
 * Broker Client or Posture Broker Server receives more than one message
 * of this type in a single batch, it should ignore all but the last
 * one.
 * 
 * E.g.:  Accept-Language: da, en-gb;q=0.8, en;q=0.7
 * 
 * Pattern in ABNF:
 * Accept-Language = "Accept-Language:" [CFWS] language-q*( "," [CFWS] language-q )
 * language-q      = language-range [";" [CFWS] "q=" qvalue ] [CFWS]
 * qvalue          = ( "0" [ "." 0*3DIGIT ] ) / ( "1" [ "." 0*3("0") ] )
 * 
 */
public class PbMessageValueLanguagePreference extends AbstractPbMessageValue {

	
    private final String preferedLanguage; //32 bit(s), accept-Language header, as described in RFC 3282 [4]  as Accept-Language included in that RFC, US-ASCII only, no control characters allowed, no comments, no NUL termination)

    
	public PbMessageValueLanguagePreference(String preferedLanguage) {
		super(preferedLanguage.getBytes(Charset.forName("US-ASCII")).length);
		this.preferedLanguage = preferedLanguage;
	}

	/**
	 * @return the preferedLanguage
	 */
	public String getPreferedLanguage() {
		return this.preferedLanguage;
	}    
    
}
