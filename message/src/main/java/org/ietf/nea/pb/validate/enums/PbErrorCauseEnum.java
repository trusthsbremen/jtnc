package org.ietf.nea.pb.validate.enums;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public enum PbErrorCauseEnum {

	NOT_SPECIFIED 			(ValidationException.NOT_SPECIFIED),
	VENDOR_ID_RESERVED 		(1),
	MESSAGE_TYPE_RESERVED	(2),
	SUB_VENDOR_ID_RESERVED	(3),
	SUB_TYPE_RESERVED		(4),
	NOSKIP_NOT_ALLOWED      (5),
    NOSKIP_MISSING       	(6),
    LENGTH_TO_SHORT 		(7),
    NULL_TERMINATION        (8),
    ZERO_STRING 			(9),
	
    // own specifications
    VENDOR_ID_NOT_SUPPORTED (10),
	MESSAGE_TYPE_NOT_SUPPORTED (11),
	BATCH_DIRECTION_OR_TYPE_UNEXPECTED (12),
    BATCH_VERSION_NOT_SUPPORTED (13),
	EXCL_DELIVERY_NOT_POSSIBLE 	(14),
	URI_SYNTAX_NOT_VALID		(15),
	BATCH_RESULT_NO_ASSESSMENT_RESULT (16);
	
	
    private int number;
    
    private PbErrorCauseEnum(int number){
        this.number = number;
    }
    
    public int number(){
        return this.number;
    }
    
    public static PbErrorCauseEnum fromNumber(int number){
    	VENDOR_ID_RESERVED.number();
    	switch(number){
    		case 1:
    			return VENDOR_ID_RESERVED;
    		case 2: 
    			return MESSAGE_TYPE_RESERVED;
    		case 3:
    			return SUB_VENDOR_ID_RESERVED;
    		case 4:
    			return SUB_TYPE_RESERVED;
    		case 5:
    			return NOSKIP_NOT_ALLOWED;
    		case 6:
    			return NOSKIP_MISSING;
    		case 7:
    			return LENGTH_TO_SHORT;
    		case 8:
    			return NULL_TERMINATION;
    		case 9:
    			return ZERO_STRING;
    		default:
    			return NOT_SPECIFIED;
    	}
    }
}
