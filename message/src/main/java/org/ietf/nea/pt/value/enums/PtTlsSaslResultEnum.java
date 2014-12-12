package org.ietf.nea.pt.value.enums;


/**
 * Reference IETF RFC 6876 section 3.8.10:
 * --------------------------------------
 *      Value (Name)                         Definition
 * ---------------------  -------------------------------------------
 * 0 (Success)           SASL authentication was successful, and
 *                       identity was confirmed.
	 *
 * 1 (Failure)           SASL authentication failed.  This might be
 *                       caused by the client providing an invalid
 *                       user identity and/or credential pair.  Note
 *                       that this is not the same as a mechanism's
 *                       failure to process the authentication as
 *                       reported by the Mechanism Failure code.
 *
 * 2 (Abort)             SASL authentication exchange was aborted by
 *                       the sender.
 *
 * 3 (Mechanism Failure) SASL "mechanism failure" during the
 *                       processing of the client's authentication
 *                       (e.g., not related to the user's input).
 *                       For example, this could occur if the
 *                       mechanism is unable to allocate memory
 *                       (e.g., malloc) that is needed to process a
 *                       received SASL mechanism message.
 */
public enum PtTlsSaslResultEnum {

	
	SUCCESS(0), 
	FAILURE(1),
	ABORT (2),
	MECHANISM_FAILURE(3);
	
	private int code;
	
	private PtTlsSaslResultEnum(int code){
		this.code = code;
	}
	
	public int code(){
		return this.code;
	}
	
	public static PtTlsSaslResultEnum fromCode(long code){

    	if(code == SUCCESS.code){
    		return SUCCESS;
    	}
    	
    	if(code == FAILURE.code){
    		return FAILURE;
    	}
    	
    	if(code ==  ABORT.code){
    		return  ABORT;
    	}
    	
    	if(code == MECHANISM_FAILURE.code){
    		return MECHANISM_FAILURE;
    	}
    	
    	return null;
    }
}
