package de.hsbremen.tc.tnc.message.handler.pb;

import java.util.EnumSet;

import org.ietf.nea.IETFConstants;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageValueAccessRecommendation;
import org.ietf.nea.pb.message.PbMessageValueAssessmentResult;
import org.ietf.nea.pb.message.PbMessageValueError;
import org.ietf.nea.pb.message.PbMessageValueExperimental;
import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.PbMessageValueLanguagePreference;
import org.ietf.nea.pb.message.PbMessageValueReasonString;
import org.ietf.nea.pb.message.PbMessageValueRemediationParameterString;
import org.ietf.nea.pb.message.PbMessageValueRemediationParameterUri;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.trustedcomputinggroup.tnc.TNCException;

import de.hsbremen.tc.tnc.tnccs.handler.TnccsMessageHandler;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;

public abstract class AbstractPbIetfMessageHandler implements TnccsMessageHandler{

	private long vendorId = IETFConstants.IETF_PEN_VENDORID;

	@Override
	public long getVendorId() {
		// TODO Auto-generated method stub
		return vendorId;
	}

	@Override
	public boolean canHandle(TnccsMessage m) {
		if(m != null && m instanceof PbMessage){
			if(((PbMessage) m).getVendorId() == IETFConstants.IETF_PEN_VENDORID 
					&& EnumSet.allOf(PbMessageTypeEnum.class).contains(((PbMessage)m).getType())){
				return true;
			}
		}
		return false;
	}

	@Override
	public void handle(TnccsMessage m) throws TNCException {
		if(canHandle(m)){
			PbMessage message = (PbMessage) m;
			if(message.getVendorId() == IETFConstants.IETF_PEN_VENDORID){
	            // because of long values no switch allowed
	            if(message.getType() == PbMessageTypeEnum.IETF_PB_PA.messageType()){

	                this.handlePbMessageIm((PbMessageValueIm)message);
	                
	            }else if(message.getType() == PbMessageTypeEnum.IETF_PB_ERROR.messageType()){
	                
	                this.handlePbMessageError((PbMessageValueError)message);
	
	            }else if(message.getType() == PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.messageType()){
	                
	            	this.handlePbMessageAssessmentResult((PbMessageValueAssessmentResult)message);
	                
	            }else if(message.getType() == PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.messageType()){
	                
	                this.handlePbMessageAccessRecommendation((PbMessageValueAccessRecommendation)message);
	                
	            }else if(message.getType() == PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.messageType()){
	            	
	            	// ToDo change this to Remediation Parameter type after cast to RemediationParameter Message
	            	if(message instanceof PbMessageValueRemediationParameterString){
	                	
	                	this.handlePbMessageRemediationParameterString((PbMessageValueRemediationParameterString)message);
	                
	                }else if(message instanceof PbMessageValueRemediationParameterUri){
	                	
	                	this.handlePbMessageRemediationParameterUri((PbMessageValueRemediationParameterUri)message);
	                }
	            }else if(message.getType() == PbMessageTypeEnum.IETF_PB_LANGUAGE_PREFERENCE.messageType()){
	                
	                this.handlePbMessageLanguagePreference((PbMessageValueLanguagePreference)message);
	                
	            }else if(message.getType() == PbMessageTypeEnum.IETF_PB_REASON_STRING.messageType()){
	                
	                this.handlePbMessageReasonString((PbMessageValueReasonString)message);
	                
	            }else if(message.getType() == PbMessageTypeEnum.IETF_PB_EXPERIMENTAL.messageType()){
	                
	                this.handlePbMessageExperimental((PbMessageValueExperimental)message);
	            
	            }else{
	                
	                throw new TNCException("The message type " + message.getType() +" was not found as a valid message type.", TNCException.TNC_RESULT_INVALID_PARAMETER);
	            }
			}
		}
	}
	
	 protected abstract void handlePbMessageIm(PbMessageValueIm actualMessage) throws TNCException;
       
	 protected abstract void handlePbMessageError(PbMessageValueError actualMessage) throws TNCException;
	 
	 protected abstract void handlePbMessageAssessmentResult(PbMessageValueAssessmentResult actualMessage) throws TNCException;
	 
	 protected abstract void handlePbMessageAccessRecommendation(PbMessageValueAccessRecommendation actualMessage) throws TNCException;
     
	 protected abstract void handlePbMessageRemediationParameterString (PbMessageValueRemediationParameterString actualMessage) throws TNCException;
     
	 protected abstract void handlePbMessageRemediationParameterUri(PbMessageValueRemediationParameterUri actualMessage) throws TNCException;
         
	 protected abstract void handlePbMessageLanguagePreference(PbMessageValueLanguagePreference actualMessage) throws TNCException;
     
	 protected abstract void handlePbMessageReasonString(PbMessageValueReasonString actualMessage) throws TNCException;
     
	 protected abstract void handlePbMessageExperimental(PbMessageValueExperimental actualMessage) throws TNCException;
}
