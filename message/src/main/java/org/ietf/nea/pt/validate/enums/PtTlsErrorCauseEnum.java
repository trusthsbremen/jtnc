package org.ietf.nea.pt.validate.enums;

import org.ietf.nea.exception.RuleException;

public enum PtTlsErrorCauseEnum {

	NOT_SPECIFIED 			(RuleException.NOT_SPECIFIED),
	VENDOR_ID_RESERVED 		(1),
	MESSAGE_TYPE_RESERVED	(2),
    LENGTH_TO_SHORT 		(3),
    null_TERMINATION        (4),
    ZERO_STRING 			(5),
	
    // own specifications
    VENDOR_ID_NOT_SUPPORTED (6),
	MESSAGE_TYPE_NOT_SUPPORTED (7),
	MESSAGE_TYPE_UNEXPECTED (8),
	VALUE_TO_LARGE(9),
	NEGATIV_UNSIGNED(10), 
	SASL_NAMING_MISMATCH(11), 
	SASL_RESULT_NOT_SUPPORTED(12), 
	UNSUPPORTED_VERSION(13), 
	ADDITIONAL_SASL_NOT_SUPPORTED(14);
	
    private int number;
    
    private PtTlsErrorCauseEnum(int number){
        this.number = number;
    }
    
    public int number(){
        return this.number;
    }
    
    public static PtTlsErrorCauseEnum fromNumber(int number){
    	VENDOR_ID_RESERVED.number();
    	switch(number){
    		case 1:
    			return VENDOR_ID_RESERVED;
    		case 2: 
    			return MESSAGE_TYPE_RESERVED;
    		case 3:
    			return LENGTH_TO_SHORT;
    		case 4:
    			return null_TERMINATION;
    		case 5:
    			return ZERO_STRING;
    		case 6:
    			return VENDOR_ID_NOT_SUPPORTED;
    		case 7:
    			return MESSAGE_TYPE_NOT_SUPPORTED;
    		case 8:
    			return MESSAGE_TYPE_UNEXPECTED;
    		case 9:	
    			return VALUE_TO_LARGE;
    		case 10:	
    			return NEGATIV_UNSIGNED;
    		case 11:	
    			return SASL_NAMING_MISMATCH;
    		case 12:
    			return SASL_RESULT_NOT_SUPPORTED;
    		case 13:
    			return UNSUPPORTED_VERSION;	
    		case 14:
    			return ADDITIONAL_SASL_NOT_SUPPORTED;
    		default:
    			return NOT_SPECIFIED;
    	}
    }
}
