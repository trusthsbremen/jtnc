package org.ietf.nea.pt.serialize.writer.bytebuffer;

import org.ietf.nea.pt.message.enums.PtTlsMessageTypeEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.TcgProtocolBindingIdentifier;
import de.hsbremen.tc.tnc.message.t.enums.TcgTProtocolBindingEnum;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportWriter;

public class PtTlsWriterFactory {

    public static TcgProtocolBindingIdentifier getProtocolIdentifier(){
        return TcgTProtocolBindingEnum.TLS1;
    }
    
	
	@SuppressWarnings({"unchecked","rawtypes"})
	public static TransportWriter<TransportMessage> createProductionDefault(){

		/* 
		 * TODO Remove raw types and unchecked conversion.
		 * Unfortunately I could not find a way around using 
		 * raw types and unchecked conversion my be some one 
		 * else can.
		 */
	
		
		PtTlsMessageHeaderWriter mWriter = new PtTlsMessageHeaderWriter();
		
		
		PtTlsWriter writer = new PtTlsWriter(mWriter);
		
		writer.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_VERSION_REQUEST.id(),
		(TransportWriter)new PtTlsMessageVersionRequestValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_VERSION_RESPONSE.id(),
				(TransportWriter)new PtTlsMessageVersionResponseValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_PB_BATCH.id(),
				(TransportWriter)new PtTlsMessagePbBatchValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_ERROR.id(),
				(TransportWriter)new PtTlsMessageErrorValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_MECHANISMS.id(),
				(TransportWriter)new PtTlsMessageSaslMechanismsValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_MECHANISM_SELECTION.id(),
				(TransportWriter)new PtTlsMessageSaslMechanismSelectionValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_AUTHENTICATION_DATA.id(),
				(TransportWriter)new PtTlsMessageSaslAutenticationDataValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_RESULT.id(),
				(TransportWriter)new PtTlsMessageSaslResultValueWriter());
		
		return writer;
	}
	
	@SuppressWarnings({"unchecked","rawtypes"})
	public static TransportWriter<TransportMessage> createExperimentalDefault(){
		
		/* 
		 * TODO Remove raw types and unchecked conversion.
		 * Unfortunately I could not find a way around using 
		 * raw types and unchecked conversion my be some one 
		 * else can.
		 */
		
		PtTlsWriter writer = (PtTlsWriter) createProductionDefault();
		
		writer.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_EXERIMENTAL.id(),
				(TransportWriter)new PtTlsMessageExperimentalValueWriter());
		
		return writer;
		
	}
	
}
