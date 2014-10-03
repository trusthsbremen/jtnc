package org.ietf.nea.pb.serialize.reader;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.PbBatchHeaderBuilderIetf;
import org.ietf.nea.pb.message.PbMessageHeaderBuilderIetf;
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
		
		reader.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.messageType(),
				(TnccsReader)new PbMessageAccessRecommendationValueReader(new PbMessageValueAccessRecommendationBuilderIetf()));
		reader.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.messageType(), 
				(TnccsReader)new PbMessageAssessmentResultValueReader(new PbMessageValueAssessmentResultBuilderIetf()));
		reader.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ERROR.messageType(), 
				(TnccsReader)new PbMessageErrorValueReader(new PbMessageValueErrorBuilderIetf()));
		reader.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_EXPERIMENTAL.messageType(),
				(TnccsReader)new PbMessageExperimentalValueReader( new PbMessageValueExperimentalBuilderIetf()));
		reader.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_LANGUAGE_PREFERENCE.messageType(),
				(TnccsReader)new PbMessageLanguagePreferenceValueReader(new PbMessageValueLanguagePreferenceBuilderIetf()));
		reader.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_PA.messageType(),
				(TnccsReader)new PbMessageImValueReader(new PbMessageValueImBuilderIetf()));
		reader.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_REASON_STRING.messageType(),
				(TnccsReader)new PbMessageReasonStringValueReader(new PbMessageValueReasonStringBuilderIetf()));
		reader.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.messageType(),
				(TnccsReader)new PbMessageRemediationParametersValueReader(
						new PbMessageValueRemediationParametersBuilderIetf(), 
						new PbMessageRemediationParameterStringSubValueReader(new PbMessageValueRemediationParameterStringBuilderIetf()),
						new PbMessageRemediationParameterUriSubValueReader(new PbMessageValueRemediationParameterUriBuilderIetf())
						));
		
		
	
		
		return reader;
	}
	
}
