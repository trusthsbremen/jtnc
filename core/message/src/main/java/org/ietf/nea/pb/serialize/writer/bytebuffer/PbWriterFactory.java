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
		
		writer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_PA.messageType(),
		(TnccsWriter)new PbMessageImValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_REASON_STRING.messageType(),
		(TnccsWriter)new PbMessageReasonStringValueWriter());
		
		writer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.messageType(),
				(TnccsWriter)new PbMessageAccessRecommendationValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.messageType(), 
				(TnccsWriter)new PbMessageAssessmentResultValueWriter());
		
		writer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_LANGUAGE_PREFERENCE.messageType(),
				(TnccsWriter)new PbMessageLanguagePreferenceValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_PA.messageType(),
				(TnccsWriter)new PbMessageImValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_REASON_STRING.messageType(),
				(TnccsWriter)new PbMessageReasonStringValueWriter());
		
		writer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ERROR.messageType(), 
				(TnccsWriter)new PbMessageErrorValueWriter(
						new PbMessageErrorParameterOffsetSubValueWriter(), 
						new PbMessageErrorParameterVersionSubValueWriter()
						));
		
		writer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.messageType(),
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
		
		writer.add(IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_EXPERIMENTAL.messageType(),
				(TnccsWriter)new PbMessageExperimentalValueWriter());
		
		return writer;
		
	}
	
}
