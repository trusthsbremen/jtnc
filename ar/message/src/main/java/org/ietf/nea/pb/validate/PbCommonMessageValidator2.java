package org.ietf.nea.pb.validate;

import org.ietf.nea.IETFConstants;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.trustedcomputinggroup.tnc.TNCConstants;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.validate.TnccsHeaderValidator;

class PbCommonMessageValidator2 implements TnccsHeaderValidator<PbMessage> {
	
	private static final class Singleton{
		private static final PbCommonMessageValidator2 INSTANCE = new PbCommonMessageValidator2();
	}
      
    
    static PbCommonMessageValidator2 getInstance(){
    	return Singleton.INSTANCE;
    }
    
    private PbCommonMessageValidator2(){
    	// Singleton
    }
    
    @Override
    public void validate(final PbMessage message) throws ValidationException {
        this.checkVendorId(message);
        this.checkMessageType(message);
        this.checkFixedMessageLength(message);
//        this.validateExtendedMessage(message);
    }
  

    private void checkVendorId(final PbMessage message) throws ValidationException{
        if(message.getVendorId() == TNCConstants.TNC_VENDORID_ANY){
            throw new ValidationException("Vendor ID is set to reserved value.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),Long.toString(message.getVendorId()));
        }
        
    }
    
    private void checkMessageType(final PbMessage message) throws ValidationException {
        if(message.getType() == IETFConstants.IETF_MESSAGE_TYPE_RESERVED){
            throw new ValidationException("Message type is set to reserved value.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),Long.toString(message.getType()));
        }  
        
    }
    
    private void checkFixedMessageLength(final PbMessage message) throws ValidationException {
        if(message.getLength() < PbMessage.FIXED_LENGTH){
            throw new ValidationException("Message has an invalid length for its type.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),Long.toString(message.getLength()));
        }
    }
    
//    private TnccsMessageVendorIdAndTypeEnum validateExtendedMessage(TnccsAbstractMessage message) throws TNCException{
//        // because of long values no switch allowed
//        if(message.getType() == TnccsMessageVendorIdAndTypeEnum.IETF_PB_PA.messageType()){
//            
//            return TnccsMessageVendorIdAndTypeEnum.IETF_PB_PA;
//            //...
//        }else if(message.getType() == TnccsMessageVendorIdAndTypeEnum.IETF_PB_ERROR.messageType()){
//            
//            return TnccsMessageVendorIdAndTypeEnum.IETF_PB_ERROR;
//            
//        }else if(message.getType() == TnccsMessageVendorIdAndTypeEnum.IETF_PB_ASSESSMENT_RESULT.messageType()){
//            
//            return TnccsMessageVendorIdAndTypeEnum.IETF_PB_ASSESSMENT_RESULT;
//            
//        }else if(message.getType() == TnccsMessageVendorIdAndTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.messageType()){
//            
//            return TnccsMessageVendorIdAndTypeEnum.IETF_PB_ACCESS_RECOMMENDATION;
//        }else if(message.getType() == TnccsMessageVendorIdAndTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.messageType()){
//            
//            return TnccsMessageVendorIdAndTypeEnum.IETF_PB_REMEDIATION_PARAMETERS;
//        }else if(message.getType() == TnccsMessageVendorIdAndTypeEnum.IETF_PB_LANGUAGE_PREFERENCE.messageType()){
//            
//            return TnccsMessageVendorIdAndTypeEnum.IETF_PB_LANGUAGE_PREFERENCE;
//        }else if(message.getType() == TnccsMessageVendorIdAndTypeEnum.IETF_PB_REASON_STRING.messageType()){
//            
//            return TnccsMessageVendorIdAndTypeEnum.IETF_PB_REASON_STRING;
//        }else if(message.getType() == TnccsMessageVendorIdAndTypeEnum.IETF_PB_EXPERIMENTAL.messageType()){
//            
//            return TnccsMessageVendorIdAndTypeEnum.IETF_PB_EXPERIMENTAL;
//        }else{
//            
//            throw new TNCException("The message type " + message.getType() +" was not found as a valid message type.", TNCException.TNC_RESULT_INVALID_PARAMETER);
//        }
//    }

}
