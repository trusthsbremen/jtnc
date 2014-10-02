package org.ietf.nea.pb.serialize.reader;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.PbBatchHeaderBuilderIetf;
import org.ietf.nea.pb.message.PbMessageHeaderBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueImBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueReasonStringBuilderIetf;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsReader;

public class PbReaderFactory {

	@SuppressWarnings({"unchecked","rawtypes"})
	public static TnccsReader<PbBatch> createDefault(){

		/* 
		 * TODO Remove raw types and unchecked conversion.
		 * Unfortunately I could not find a way around using 
		 * raw types and unchecked conversion my be some one 
		 * else can.
		 */
	
		
		PbMessageHeaderReader mReader = new PbMessageHeaderReader(new PbMessageHeaderBuilderIetf());
		
		PbBatchHeaderReader bReader = new PbBatchHeaderReader(new PbBatchHeaderBuilderIetf());
		
		PbReader reader = new PbReader(bReader, mReader);

		
		reader.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_PA.messageType(),
		(TnccsReader)new PbMessageImValueReader(new PbMessageValueImBuilderIetf()));
		reader.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_REASON_STRING.messageType(),
		(TnccsReader)new PbMessageReasonStringValueReader(new PbMessageValueReasonStringBuilderIetf()));
		
//		mSerializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.messageType(),
//				(TnccsSerializer)new PbMessageAccessRecommendationSerializer(new PbMessageValueAccessRecommendationBuilderIetf()));
//		mSerializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.messageType(), 
//				(TnccsSerializer)new PbMessageAssessmentResultSerializer(new PbMessageValueAssessmentResultBuilderIetf()));
//		mSerializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ERROR.messageType(), 
//				(TnccsSerializer)new PbMessageErrorSerializer(new PbMessageValueErrorBuilderIetf()));
//		mSerializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_EXPERIMENTAL.messageType(),
//				(TnccsSerializer)new PbMessageExperimentalSerializer( new PbMessageValueExperimentalBuilderIetf()));
//		mSerializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_LANGUAGE_PREFERENCE.messageType(),
//				(TnccsSerializer)new PbMessageLanguagePreferenceSerializer(new PbMessageValueLanguagePreferenceBuilderIetf()));
//		mSerializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_PA.messageType(),
//				(TnccsSerializer)new PbMessageImSerializer(new PbMessageValueImBuilderIetf()));
//		mSerializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_REASON_STRING.messageType(),
//				(TnccsSerializer)new PbMessageReasonStringSerializer(new PbMessageValueReasonStringBuilderIetf()));
//		mSerializer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.messageType(),
//				(TnccsSerializer)new PbMessageRemediationParameterSerializer(
//						new PbMessageValueRemediationParametersBuilderIetf(), 
//						new PbMessageRemediationParameterStringSerializer(new PbMessageValueRemediationParameterStringBuilderIetf()),
//						new PbMessageRemediationParameterUriSerializer(new PbMessageValueRemediationParameterUriBuilderIetf())
//						));
//		
		
	
		
		return reader;
	}
	
}
