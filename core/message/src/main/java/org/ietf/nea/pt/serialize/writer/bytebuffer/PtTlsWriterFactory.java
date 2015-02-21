package org.ietf.nea.pt.serialize.writer.bytebuffer;

import org.ietf.nea.pt.message.enums.PtTlsMessageTypeEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.t.enums.TcgTProtocolEnum;
import de.hsbremen.tc.tnc.message.t.enums.TcgTVersionEnum;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportWriter;

public class PtTlsWriterFactory {

	public static String getTransportProtocol(){
		return TcgTProtocolEnum.TLS.value();
	}
	
	public static String getTransportVersion(){
		return TcgTVersionEnum.V1.value();
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
		
		writer.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_VERSION_REQUEST.messageType(),
		(TransportWriter)new PtTlsMessageVersionRequestValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_VERSION_RESPONSE.messageType(),
				(TransportWriter)new PtTlsMessageVersionResponseValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_PB_BATCH.messageType(),
				(TransportWriter)new PtTlsMessagePbBatchValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_ERROR.messageType(),
				(TransportWriter)new PtTlsMessageErrorValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_MECHANISMS.messageType(),
				(TransportWriter)new PtTlsMessageSaslMechanismsValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_MECHANISM_SELECTION.messageType(),
				(TransportWriter)new PtTlsMessageSaslMechanismSelectionValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_AUTHENTICATION_DATA.messageType(),
				(TransportWriter)new PtTlsMessageSaslAutenticationDataValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_RESULT.messageType(),
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
		
		writer.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_EXERIMENTAL.messageType(),
				(TransportWriter)new PtTlsMessageExperimentalValueWriter());
		
		return writer;
		
	}
	
}
