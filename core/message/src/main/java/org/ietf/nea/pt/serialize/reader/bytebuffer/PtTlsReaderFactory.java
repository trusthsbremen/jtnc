package org.ietf.nea.pt.serialize.reader.bytebuffer;


import org.ietf.nea.pt.message.PtTlsMessageHeaderBuilderIetf;
import org.ietf.nea.pt.message.enums.PtTlsMessageTypeEnum;
import org.ietf.nea.pt.value.PtTlsMessageValueErrorBuilderIetf;
import org.ietf.nea.pt.value.PtTlsMessageValueExperimentalBuilderIetf;
import org.ietf.nea.pt.value.PtTlsMessageValuePbBatchBuilderIetf;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslAuthenticationDataBuilderIetf;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanismSelectionBuilderIetf;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanismsBuilderIetf;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslResultBuilderIetf;
import org.ietf.nea.pt.value.PtTlsMessageValueVersionRequestBuilderIetf;
import org.ietf.nea.pt.value.PtTlsMessageValueVersionResponseBuilderIetf;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.TcgProtocolBindingIdentifier;
import de.hsbremen.tc.tnc.message.t.enums.TcgTProtocolBindingEnum;
import de.hsbremen.tc.tnc.message.t.serialize.TransportMessageContainer;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportReader;

public class PtTlsReaderFactory {

    public static TcgProtocolBindingIdentifier getProtocolIdentifier(){
        return TcgTProtocolBindingEnum.TLS1;
    }
	
	@SuppressWarnings({"unchecked","rawtypes"})
	public static TransportReader<TransportMessageContainer> createProductionDefault(){

		/* 
		 * TODO Remove raw types and unchecked conversion.
		 * Unfortunately I could not find a way around using 
		 * raw types and unchecked conversion my be some one 
		 * else can.
		 */
	
		
		PtTlsMessageHeaderReader mReader = new PtTlsMessageHeaderReader(new PtTlsMessageHeaderBuilderIetf());
		
		
		PtTlsReader reader = new PtTlsReader(mReader);

		reader.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_VERSION_REQUEST.id(),
				(TransportReader)new PtTlsMessageVersionRequestValueReader(new PtTlsMessageValueVersionRequestBuilderIetf()));
		reader.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_VERSION_RESPONSE.id(),
				(TransportReader)new PtTlsMessageVersionResponseValueReader(new PtTlsMessageValueVersionResponseBuilderIetf()));
		reader.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_PB_BATCH.id(),
		(TransportReader)new PtTlsMessagePbBatchValueReader(new PtTlsMessageValuePbBatchBuilderIetf()));
		reader.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_ERROR.id(),
				(TransportReader)new PtTlsMessageErrorValueReader(new PtTlsMessageValueErrorBuilderIetf()));
		reader.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_MECHANISMS.id(),
				(TransportReader)new PtTlsMessageSaslMechanismsValueReader(new PtTlsMessageValueSaslMechanismsBuilderIetf()));
		reader.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_MECHANISM_SELECTION.id(),
				(TransportReader)new PtTlsMessageSaslMechanismSelectionValueReader(new PtTlsMessageValueSaslMechanismSelectionBuilderIetf()));
		reader.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_AUTHENTICATION_DATA.id(),
				(TransportReader)new PtTlsMessageSaslAuthenticationDataValueReader(new PtTlsMessageValueSaslAuthenticationDataBuilderIetf()));
		reader.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_RESULT.id(),
				(TransportReader)new PtTlsMessageSaslResultValueReader(new PtTlsMessageValueSaslResultBuilderIetf()));
		
		
		return reader;
	}
	
	@SuppressWarnings({"unchecked","rawtypes"})
	public static TransportReader<TransportMessageContainer> createExperimentalDefault(long maxMessageSize){
		
		/* 
		 * TODO Remove raw types and unchecked conversion.
		 * Unfortunately I could not find a way around using 
		 * raw types and unchecked conversion my be some one 
		 * else can.
		 */
		
		PtTlsReader reader = (PtTlsReader) createProductionDefault();
		
		reader.add(IETFConstants.IETF_PEN_VENDORID, PtTlsMessageTypeEnum.IETF_PT_TLS_EXERIMENTAL.id(),
				(TransportReader)new PtTlsMessageExperimentalValueReader( new PtTlsMessageValueExperimentalBuilderIetf()));
		
		return reader;
	}
	
}