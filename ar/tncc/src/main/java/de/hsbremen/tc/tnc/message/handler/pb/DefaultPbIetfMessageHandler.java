package de.hsbremen.tc.tnc.message.handler.pb;

import org.ietf.nea.pb.message.PbMessageValueAccessRecommendation;
import org.ietf.nea.pb.message.PbMessageValueAssessmentResult;
import org.ietf.nea.pb.message.PbMessageValueError;
import org.ietf.nea.pb.message.PbMessageValueExperimental;
import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.PbMessageValueLanguagePreference;
import org.ietf.nea.pb.message.PbMessageValueReasonString;
import org.ietf.nea.pb.message.PbMessageValueRemediationParameterString;
import org.ietf.nea.pb.message.PbMessageValueRemediationParameterUri;
import org.ietf.nea.pb.validate.PbMessageAccessRecommendationValidator2;
import org.ietf.nea.pb.validate.PbMessageAssessmentResultValidator2;
import org.ietf.nea.pb.validate.PbMessageErrorValidator2;
import org.ietf.nea.pb.validate.PbMessageExperimentalValidator2;
import org.ietf.nea.pb.validate.PbMessageImValidator2;
import org.ietf.nea.pb.validate.PbMessageLanguagePreferenceValidator2;
import org.ietf.nea.pb.validate.PbMessageReasonStringValidator2;
import org.ietf.nea.pb.validate.PbMessageRemediationParameterStringValidator2;
import org.ietf.nea.pb.validate.PbMessageRemediationParameterUriValidator2;
import org.trustedcomputinggroup.tnc.TNCException;

import de.hsbremen.tc.tnc.message.handler.pb.util.PaDeliveryAgent;
import de.hsbremen.tc.tnc.tncc.session.TncSession;

public class DefaultPbIetfMessageHandler extends AbstractPbIetfMessageHandler{

	private TncSession context;
	private PaDeliveryAgent imcDistribution;
	
	public DefaultPbIetfMessageHandler(TncSession context, PaDeliveryAgent imcDistribution){
		super();
		this.context = context;
		this.imcDistribution = imcDistribution;
	}
	
	@Override
	 protected void handlePbMessageIm(PbMessageValueIm actualMessage) throws TNCException{
         //validate
         PbMessageImValidator2.getInstance().validate(actualMessage); 
         //send to IM
         imcDistribution.dispatchMessage(actualMessage, context);
     }

	@Override
	 protected void handlePbMessageError(PbMessageValueError actualMessage) throws TNCException{
         //validate
         PbMessageErrorValidator2.getInstance().validate(actualMessage);;
         // TODO Let TncClient handle this messages

     }
	@Override
	 protected void handlePbMessageAssessmentResult(PbMessageValueAssessmentResult actualMessage) throws TNCException{
         //validate
         PbMessageAssessmentResultValidator2.getInstance().validate(actualMessage);
         // TODO Let TncClient handle this messages
	 }
	@Override
     protected void handlePbMessageAccessRecommendation(PbMessageValueAccessRecommendation actualMessage) throws TNCException{
	     //validate
	     PbMessageAccessRecommendationValidator2.getInstance().validate(actualMessage);
	     // TODO Let TncClient handle this messages
     }
	@Override
     protected void handlePbMessageRemediationParameterString (PbMessageValueRemediationParameterString actualMessage) throws TNCException{
         	// validate
         	PbMessageRemediationParameterStringValidator2.getInstance().validate(actualMessage);
         	// TODO Let TncClient handle this messages
     }
	@Override
     protected void handlePbMessageRemediationParameterUri(PbMessageValueRemediationParameterUri actualMessage) throws TNCException{
         	// validate
         	PbMessageRemediationParameterUriValidator2.getInstance().validate(actualMessage);
         	// TODO Let TncClient handle this messages
     }
	@Override    
     protected void handlePbMessageLanguagePreference(PbMessageValueLanguagePreference actualMessage) throws TNCException{
         //validate
         PbMessageLanguagePreferenceValidator2.getInstance().validate(actualMessage);
         // TODO Let TncClient handle this messages
     }
	@Override
     protected void handlePbMessageReasonString(PbMessageValueReasonString actualMessage) throws TNCException{
         //validate
         PbMessageReasonStringValidator2.getInstance().validate(actualMessage);
         // TODO Let TncClient handle this messages
         
     }
	@Override
     protected void handlePbMessageExperimental(PbMessageValueExperimental actualMessage) throws TNCException{
         //validate
         PbMessageExperimentalValidator2.getInstance().validate(actualMessage);
         // TODO Let TncClient handle this messages
     }
}
