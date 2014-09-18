package org.ietf.nea.pb.serialize;

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
		PbMessageSerializer serializer = new PbMessageSerializer();
		
		serializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.messageType(), (TnccsSerializer)PbMessageAccessRecommendationSerializer.getInstance());
		serializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.messageType(), (TnccsSerializer)PbMessageAssessmentResultSerializer.getInstance());
		serializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ERROR.messageType(), (TnccsSerializer)PbMessageErrorSerializer.getInstance());
		serializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_EXPERIMENTAL.messageType(), (TnccsSerializer)PbMessageExperimentalSerializer.getInstance());
		serializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_LANGUAGE_PREFERENCE.messageType(), (TnccsSerializer)PbMessageLanguagePreferenceSerializer.getInstance());
		serializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_PA.messageType(), (TnccsSerializer)PbMessageImSerializer.getInstance());
		serializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_REASON_STRING.messageType(), (TnccsSerializer)PbMessageReasonStringSerializer.getInstance());
		serializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.messageType(), (TnccsSerializer)PbMessageRemediationParameterSerializer.getInstance());
		
		return serializer;
	}
	
}
