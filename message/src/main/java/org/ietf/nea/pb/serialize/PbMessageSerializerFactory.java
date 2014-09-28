package org.ietf.nea.pb.serialize;

import org.ietf.nea.pb.message.PbMessageBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueAccessRecommendationBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueAssessmentResultBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueErrorBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueExperimentalBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueImBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueLanguagePreferenceBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueReasonStringBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueRemediationParameterStringBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueRemediationParameterUriBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueRemediationParametersBuilderIetf;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;

public class PbMessageSerializerFactory {

	
	 
	@SuppressWarnings({"unchecked","rawtypes"})
	public static PbMessageSerializer createDefault(){
		
		/* 
		 * TODO Remove raw types and unchecked conversion.
		 * Unfortunately I could not find a way around using 
		 * raw types and unchecked conversion my be some one 
		 * else can.
		 */
		PbMessageSerializer serializer = new PbMessageSerializer(new PbMessageBuilderIetf());
		
		serializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.messageType(),
				(TnccsSerializer)new PbMessageAccessRecommendationSerializer(new PbMessageValueAccessRecommendationBuilderIetf()));
		serializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.messageType(), 
				(TnccsSerializer)new PbMessageAssessmentResultSerializer(new PbMessageValueAssessmentResultBuilderIetf()));
		serializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ERROR.messageType(), 
				(TnccsSerializer)new PbMessageErrorSerializer(new PbMessageValueErrorBuilderIetf()));
		serializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_EXPERIMENTAL.messageType(),
				(TnccsSerializer)new PbMessageExperimentalSerializer( new PbMessageValueExperimentalBuilderIetf()));
		serializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_LANGUAGE_PREFERENCE.messageType(),
				(TnccsSerializer)new PbMessageLanguagePreferenceSerializer(new PbMessageValueLanguagePreferenceBuilderIetf()));
		serializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_PA.messageType(),
				(TnccsSerializer)new PbMessageImSerializer(new PbMessageValueImBuilderIetf()));
		serializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_REASON_STRING.messageType(),
				(TnccsSerializer)new PbMessageReasonStringSerializer(new PbMessageValueReasonStringBuilderIetf()));
		serializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.messageType(),
				(TnccsSerializer)new PbMessageRemediationParameterSerializer(
						new PbMessageValueRemediationParametersBuilderIetf(), 
						new PbMessageRemediationParameterStringSerializer(new PbMessageValueRemediationParameterStringBuilderIetf()),
						new PbMessageRemediationParameterUriSerializer(new PbMessageValueRemediationParameterUriBuilderIetf())
						));
		
		return serializer;
	}
	
}
