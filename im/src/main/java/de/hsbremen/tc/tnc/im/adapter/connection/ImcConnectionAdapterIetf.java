package de.hsbremen.tc.tnc.im.adapter.connection;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttribute;
import org.ietf.nea.pa.message.PaMessageFactoryIetf;
import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnectionLong;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.data.enums.ImComponentFlagsEnum;
import de.hsbremen.tc.tnc.m.attribute.ImAttribute;
import de.hsbremen.tc.tnc.m.message.ImMessage;
import de.hsbremen.tc.tnc.m.serialize.ImWriter;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

class ImcConnectionAdapterIetf implements ImcConnectionAdapter {
	
	// TODO these seems to be better somewhere else, ideas?
	private final short version;
	
	private ImWriter<ImMessage> byteWriter;
	private IMCConnection connection;
	
	
	@SuppressWarnings("unchecked")
	ImcConnectionAdapterIetf(short ifMVersion,ImWriter<? extends ImMessage> byteWriter,
			IMCConnection connection) {
		this.version = ifMVersion;
		this.byteWriter = (ImWriter<ImMessage>) byteWriter;
		this.connection = connection;
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapter#sendMessage(de.hsbremen.tc.tnc.im.adapter.ImComponent, long, short)
	 */
	@Override
	public void sendMessage( ImObjectComponent component, long identifier)
			throws TncException, ValidationException {
		
		if(component != null && component.getAttributes() != null){
			ImMessage message = PaMessageFactoryIetf.createMessage(version, identifier, this.filterTypes(component.getAttributes()));
			
			byte [] byteMessage = this.messageToByteArray(message);
			
			byte flags = 0;
			for (ImComponentFlagsEnum flagEnum: component.getImFlags()) {
				flags |= flagEnum.bit();
			}
					
			try {
				this.send(flags,component.getVendorId(), component.getType(), component.getCollectorId(), component.getValidatorId(), byteMessage);
			} catch (TNCException e) {
				throw new TncException(e);
			}

		} // else ignore and do nothing	
		
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapter#requestHandshakeRetry(long)
	 */
	@Override
	public void requestHandshakeRetry(ImHandshakeRetryReasonEnum reason) throws TncException {
		if(reason.toString().contains("IMC")){
			try {
				this.connection.requestHandshakeRetry(reason.code());
			} catch (TNCException e) {
				throw new TncException(e);
			}
		}else{
			throw new TncException("Reason is not useable with IMC and IMCConnection.", TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
		}
	}

	
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapter#getAttribute(de.hsbremen.tc.tnc.attribute.TncAttributeType)
	 */
	@Override
	public Object getAttribute(TncAttributeType type) throws TncException {
		if(this.connection instanceof AttributeSupport){
			try {
				return ((AttributeSupport) this.connection).getAttribute(type.id());
			} catch (TNCException e) {
				throw new TncException(e);
			}
		}else{
			throw new UnsupportedOperationException("The connection " + this.connection.toString() + " of type " 
					+ this.connection.getClass().getCanonicalName() + " does not support attributes.");
		}
	}

	private void send(byte flags, long vendorId, long type, long collectorId, long validatorId, byte[] message) throws TNCException, TncException{
		
		// FIXME it maybe better to check the IMC type here too.
		if(this.connection instanceof IMCConnectionLong && collectorId != HSBConstants.HSB_IM_ID_UNKNOWN){
			((IMCConnectionLong) this.connection).sendMessageLong(flags, vendorId, type, message, collectorId, validatorId);
		}else{
			if(type >= TNCConstants.TNC_SUBTYPE_ANY){
				throw new TNCException("Connection does not support the message type "+ type +", which is greater than " + TNCConstants.TNC_SUBTYPE_ANY + ".", TNCException.TNC_RESULT_NO_LONG_MESSAGE_TYPES);
			}
			long msgType = (long)(vendorId << 8) | (type & 0xFF);
			this.connection.sendMessage(msgType, message);
		}
		
	}
	
	private List<PaAttribute> filterTypes(List<? extends ImAttribute> imAttributes){
		List<PaAttribute> attributes = new LinkedList<>();
		for (ImAttribute attr : imAttributes) {
			if(attr instanceof PaAttribute){
				attributes.add((PaAttribute)attr);
			}
		}
		return attributes;
	}
	
	private byte[] messageToByteArray(ImMessage message) throws TncException{
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			this.byteWriter.write(message, out);
		} catch (SerializationException e) {
			throw new TncException(e.getMessage(), TncExceptionCodeEnum.TNC_RESULT_OTHER);
		}
	
		return out.toByteArray();
	
	}

}
