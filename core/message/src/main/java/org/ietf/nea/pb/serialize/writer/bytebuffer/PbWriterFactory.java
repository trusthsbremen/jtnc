package org.ietf.nea.pb.serialize.writer.bytebuffer;

import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.TcgProtocolBindingIdentifier;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.enums.TcgTnccsProtocolBindingEnum;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;

public class PbWriterFactory {

    public static TcgProtocolBindingIdentifier getProtocolIdentifier(){
        return TcgTnccsProtocolBindingEnum.TNCCS2;
    }
	
	@SuppressWarnings({"unchecked","rawtypes"})
	public static TnccsWriter<TnccsBatch> createProductionDefault(){

		/* 
		 * TODO Remove raw types and unchecked conversion.
		 * Unfortunately I could not find a way around using 
		 * raw types and unchecked conversion my be some one 
		 * else can.
		 */
	
		
		PbMessageHeaderWriter mWriter = new PbMessageHeaderWriter();
		
		PbBatchHeaderWriter bWriter = new PbBatchHeaderWriter();
		
		PbWriter writer = new PbWriter(bWriter, mWriter);
		
		writer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_PA.id(),
		(TnccsWriter)new PbMessageImValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_REASON_STRING.id(),
		(TnccsWriter)new PbMessageReasonStringValueWriter());
		
		writer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.id(),
				(TnccsWriter)new PbMessageAccessRecommendationValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.id(), 
				(TnccsWriter)new PbMessageAssessmentResultValueWriter());
		
		writer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_LANGUAGE_PREFERENCE.id(),
				(TnccsWriter)new PbMessageLanguagePreferenceValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_PA.id(),
				(TnccsWriter)new PbMessageImValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_REASON_STRING.id(),
				(TnccsWriter)new PbMessageReasonStringValueWriter());
		
		writer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ERROR.id(), 
				(TnccsWriter)new PbMessageErrorValueWriter(
						new PbMessageErrorParameterOffsetSubValueWriter(), 
						new PbMessageErrorParameterVersionSubValueWriter()
						));
		
		writer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.id(),
				(TnccsWriter)new PbMessageRemediationParametersValueWriter(
						new PbMessageRemediationParameterStringSubValueWriter(),
						new PbMessageRemediationParameterUriSubValueWriter()
						));
		
		
	
		
		return writer;
	}
	
	@SuppressWarnings({"unchecked","rawtypes"})
	public static TnccsWriter<TnccsBatch> createExperimentalDefault(){
		
		/* 
		 * TODO Remove raw types and unchecked conversion.
		 * Unfortunately I could not find a way around using 
		 * raw types and unchecked conversion my be some one 
		 * else can.
		 */
		
		PbWriter writer = (PbWriter) createProductionDefault();
		
		writer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_EXPERIMENTAL.id(),
				(TnccsWriter)new PbMessageExperimentalValueWriter());
		
		return writer;
		
	}
	
}
