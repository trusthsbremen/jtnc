package org.ietf.nea.pt.value.enums;

/**
 * Reference IETF RFC 5793 section 4.9.1:
 * --------------------------------------
 *  Value (Name)                         Definition
 * --------------------------  ---------------------------------------
 * 0 (Reserved)               Reserved value indicates that the
 *                            PT-TLS Error message SHOULD be ignored
 *                            by all recipients.  This MAY be used
 *                            for debugging purposes to allow a
 *                            sender to see a copy of the message
 *                            that was received.
 *
 * 1 (Malformed Message)      PT-TLS message unrecognized or
 *                            unsupported.  This error code SHOULD be
 *                            sent when the basic message content
 *                            sanity test fails.  The sender of this
 *                            error code MUST consider it a fatal
 *                            error and abort the assessment.
 *
 * 2 (Version Not Supported)  This error SHOULD be sent when a PT-TLS
 *                            Responder receives a PT-TLS Version
 *                            Request message containing a range of
 *                            version numbers that doesn't include
 *                            any version numbers that the recipient
 *                            is willing and able to support on the
 *                            session.  All PT-TLS messages carrying
 *                            the Version Not Supported error code
 *                            MUST use a version number of 1.  All
 *                            parties that receive or send PT-TLS
 *                            messages MUST be able to properly
 *                            process an error message that meets
 *                            this description, even if they cannot
 *                            process any other aspect of PT-TLS
 *                            version 1.  The sender and receiver of
 *                            this error code MUST consider it a
 *                            fatal error and close the TLS session
 *                            after sending or receiving this PT-TLS
 *                            message.
 *                            
 * 3 (Type Not Supported)     PT-TLS Message Type unknown or not
 *                            supported.  When a recipient receives a
 *                            PT-TLS Message Type that it does not
 *                            support, it MUST send back this error,
 *                            ignore the message, and proceed.  For
 *                            example, this could occur if the sender
 *                            used a Vendor ID for the Message Type
 *                            that is not supported by the recipient.
 *                            This error message does not indicate
 *                            that a fatal error has occurred, so the
 *                            assessment is allowed to continue. 
 *                            
 * 4 (Invalid Message)        PT-TLS message received was invalid
 *                            based on the protocol state.  For
 *                            example, this error would be sent if a
 *                            recipient receives a message associated
 *                            with the PT-TLS Negotiation Phase (such
 *                            as Version messages) after the protocol
 *                            has reached the PT-TLS Data Transport
 *                            Phase.  The sender and receiver of this
 *                            error code MUST consider it a fatal
 *                            error and close the TLS session after
 *                            sending or receiving this PT-TLS
 *                            message.
 *
 * 5 (SASL Mechanism Error)   A fatal error occurred while trying to
 *                            perform the client authentication.  For
 *                            example, the NEA Client is unable to
 *                            support any of the offered SASL
 *                            mechanisms.  The sender and receiver of
 *                            this error code MUST consider it a
 *                            fatal error and close the TLS session
 *                            after sending or receiving this PT-TLS
 *                            message.
 *
 * 6 (Invalid Parameter)      The PT-TLS Error message sender has
 *                            received a message with an invalid or
 *                            unsupported value in the PT-TLS header.
 *                            This could occur if the NEA Client
 *                            receives a PT-TLS message from the NEA
 *                            Server with a Message Length of zero
 *                            (see Section 3.5 for details).  The
 *                            sender and receiver of this error code
 *                            MUST consider it a fatal error and
 *                            close the TLS session after sending or
 *                            receiving this PT-TLS message.
 */
public enum PtTlsMessageErrorCodeEnum {
    // IETF
    IETF_RESERVED          				(0),
    IETF_MALFORMED_MESSAGE              (1),
    IETF_UNSUPPORTED_VERSION            (2),
    IETF_UNSUPPORTED_MESSAGE_TYPE		(3),
    IETF_INVALID_MESSAGE 			 	(4),
    IETF_SASL_MECHANISM_ERROR           (5),
    IETF_INVALID_PARAMETER				(6);
    
    private int code;
    
    private PtTlsMessageErrorCodeEnum(int code){
        this.code = code;
    }
    
    public int code(){
        return this.code;
    }
    
    public static PtTlsMessageErrorCodeEnum fromCode(int code){
    	
    	if(code == IETF_RESERVED.code){
    		return IETF_RESERVED;
    	}
    	
    	if(code == IETF_MALFORMED_MESSAGE.code){
    		return IETF_MALFORMED_MESSAGE;
    	}
    	
    	if(code == IETF_UNSUPPORTED_VERSION.code){
    		return IETF_UNSUPPORTED_VERSION;
    	}
    	
    	if(code == IETF_UNSUPPORTED_MESSAGE_TYPE.code){
    		return IETF_UNSUPPORTED_MESSAGE_TYPE;
    	}
    	
    	if(code == IETF_INVALID_MESSAGE.code){
    		return IETF_INVALID_MESSAGE;
    	}
    	
    	if(code == IETF_SASL_MECHANISM_ERROR.code){
    		return IETF_SASL_MECHANISM_ERROR;
    	}
    	
    	if(code == IETF_INVALID_PARAMETER.code){
    		return IETF_INVALID_PARAMETER;
    	}
    	
    	return null;
    }
}
