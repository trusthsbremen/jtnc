package org.ietf.nea.pa.validate.enums;

import org.ietf.nea.exception.RuleException;

public enum PaErrorCauseEnum {

	NOT_SPECIFIED 			(RuleException.NOT_SPECIFIED),
	VENDOR_ID_RESERVED 		(1),
	TYPE_RESERVED			(2),
    LENGTH_TO_SHORT 		(3),
    null_TERMINATION        (4),
    ZERO_STRING 			(5),
    ILLEGAL_ATTRIBUTE_REQUEST (6), 
    MESSAGE_VERSION_NOT_SUPPORTED(7),
    
    // own specifications
    VENDOR_ID_NOT_SUPPORTED (8),
	MESSAGE_TYPE_NOT_SUPPORTED (9),
	URI_SYNTAX_NOT_VALID		(10),
	VALUE_TO_LARGE(11),
	NEGATIV_UNSIGNED(12),
	ASSESSMENT_RESULT_NOT_SUPPORTED(13), 
	FORWARDING_STATUS_NOT_SUPPORTED (14), 
	FACTORY_DEFAULT_PW_STATUS_NOT_SUPPORTED (15), 
	INVALID_PRODUCT_ID(16), 
	DUPLICATE_PORT_ENTRY(17), 
	MIXED_PROTOCOL_BLOCKING(18), 
	TIME_FORMAT_NOT_VALID(19), 
	NOSKIP_MISSING (20), 
	NOSKIP_NOT_ALLOWED (21); 
	
	
	
    private int number;
    
    private PaErrorCauseEnum(int number){
        this.number = number;
    }
    
    public int number(){
        return this.number;
    }
    
    //TODO add cases
    public static PaErrorCauseEnum fromNumber(int number){
    	VENDOR_ID_RESERVED.number();
    	switch(number){
    		case 1:
    			return VENDOR_ID_RESERVED;
    		case 2: 
    			return TYPE_RESERVED;
    		case 3:
    			return LENGTH_TO_SHORT;
    		case 4:
    			return null_TERMINATION;
    		case 5:
    			return ZERO_STRING;
    		case 6:
    			return ILLEGAL_ATTRIBUTE_REQUEST;
    		case 7:	
    			return MESSAGE_VERSION_NOT_SUPPORTED;
    		case 8:
    			return VENDOR_ID_NOT_SUPPORTED;
    		case 9:
    			return MESSAGE_TYPE_NOT_SUPPORTED;
    		case 10:
    			return URI_SYNTAX_NOT_VALID;
    		case 11:
    			return VALUE_TO_LARGE;
    		case 12:
    			return NEGATIV_UNSIGNED;
    		case 13:
    			return ASSESSMENT_RESULT_NOT_SUPPORTED;
    		case 14:
    			return FORWARDING_STATUS_NOT_SUPPORTED;
    		case 15:
    			return FACTORY_DEFAULT_PW_STATUS_NOT_SUPPORTED;
    		case 16:
    			return INVALID_PRODUCT_ID;
    		case 17:
    			return DUPLICATE_PORT_ENTRY;
    		case 18:
    			return MIXED_PROTOCOL_BLOCKING;
    		case 19:
    			return TIME_FORMAT_NOT_VALID;
    		default:
    			return NOT_SPECIFIED;
    	}
    }
}
