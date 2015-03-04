package org.ietf.nea.pb.serialize.reader.bytebuffer;

import org.ietf.nea.pb.batch.PbBatchHeaderBuilderIetf;
import org.ietf.nea.pb.message.PbMessageHeaderBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueAccessRecommendationBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueAssessmentResultBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueErrorBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueExperimentalBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueImBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueLanguagePreferenceBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueReasonStringBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueRemediationParametersBuilderIetf;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterOffsetBuilderIetf;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterVersionBuilderIetf;
import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterStringBuilderIetf;
import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterUriBuilderIetf;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.TcgProtocolBindingIdentifier;
import de.hsbremen.tc.tnc.message.tnccs.enums.TcgTnccsProtocolBindingEnum;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;

public class PbReaderFactory {

    public static TcgProtocolBindingIdentifier getProtocolIdentifier(){
        return TcgTnccsProtocolBindingEnum.TNCCS2;
    }
	
	@SuppressWarnings({"unchecked","rawtypes"})
	public static TnccsReader<TnccsBatchContainer> createProductionDefault(){

		/* 
		 * TODO Remove raw types and unchecked conversion.
		 * Unfortunately I could not find a way around using 
		 * raw types and unchecked conversion my be some one 
		 * else can.
		 */
	
		
		PbMessageHeaderReader mReader = new PbMessageHeaderReader(new PbMessageHeaderBuilderIetf());
		
		PbBatchHeaderReader bReader = new PbBatchHeaderReader(new PbBatchHeaderBuilderIetf());
		
		PbReader reader = new PbReader(bReader, mReader);

		
		reader.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_PA.id(),
		(TnccsReader)new PbMessageImValueReader(new PbMessageValueImBuilderIetf()));
		reader.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_REASON_STRING.id(),
		(TnccsReader)new PbMessageReasonStringValueReader(new PbMessageValueReasonStringBuilderIetf()));
		
		reader.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.id(),
				(TnccsReader)new PbMessageAccessRecommendationValueReader(new PbMessageValueAccessRecommendationBuilderIetf()));
		reader.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.id(), 
				(TnccsReader)new PbMessageAssessmentResultValueReader(new PbMessageValueAssessmentResultBuilderIetf()));
		
		reader.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_LANGUAGE_PREFERENCE.id(),
				(TnccsReader)new PbMessageLanguagePreferenceValueReader(new PbMessageValueLanguagePreferenceBuilderIetf()));
		reader.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_PA.id(),
				(TnccsReader)new PbMessageImValueReader(new PbMessageValueImBuilderIetf()));
		reader.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_REASON_STRING.id(),
				(TnccsReader)new PbMessageReasonStringValueReader(new PbMessageValueReasonStringBuilderIetf()));
		
		reader.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ERROR.id(), 
				(TnccsReader)new PbMessageErrorValueReader(
						new PbMessageValueErrorBuilderIetf(), 
						new PbMessageErrorParameterOffsetSubValueReader(new PbMessageValueErrorParameterOffsetBuilderIetf()),
						new PbMessageErrorParameterVersionSubValueReader(new PbMessageValueErrorParameterVersionBuilderIetf())
						));
		
		reader.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.id(),
				(TnccsReader)new PbMessageRemediationParametersValueReader(
						new PbMessageValueRemediationParametersBuilderIetf(), 
						new PbMessageRemediationParameterStringSubValueReader(new PbMessageValueRemediationParameterStringBuilderIetf()),
						new PbMessageRemediationParameterUriSubValueReader(new PbMessageValueRemediationParameterUriBuilderIetf())
						));
		
		
	
		
		return reader;
	}
	
	@SuppressWarnings({"unchecked","rawtypes"})
	public static TnccsReader<TnccsBatchContainer> createExperimentalDefault(){
		
		/* 
		 * TODO Remove raw types and unchecked conversion.
		 * Unfortunately I could not find a way around using 
		 * raw types and unchecked conversion my be some one 
		 * else can.
		 */
		
		PbReader reader = (PbReader) createProductionDefault();
		
		reader.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_EXPERIMENTAL.id(),
				(TnccsReader)new PbMessageExperimentalValueReader( new PbMessageValueExperimentalBuilderIetf()));
		
		return reader;
	}
	
}
