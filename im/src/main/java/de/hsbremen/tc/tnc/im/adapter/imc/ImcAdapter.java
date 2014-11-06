package de.hsbremen.tc.tnc.im.adapter.imc;

import java.io.ByteArrayInputStream;

import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.im.adapter.data.ImComponentFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.data.ImRawComponent;
import de.hsbremen.tc.tnc.m.message.ImMessage;
import de.hsbremen.tc.tnc.m.serialize.ImReader;

abstract class ImcAdapter implements IMC, AttributeSupport{

	private final ImReader<ImMessage> byteReader;
	
	@SuppressWarnings("unchecked")
	ImcAdapter(ImReader<? extends ImMessage> imReader) {
		this.byteReader = (ImReader<ImMessage>)imReader;
	}
	
	protected ImObjectComponent receiveMessage(ImRawComponent rawComponent) throws TNCException{
		ImMessage imMessage = this.byteArrayToMessage(rawComponent.getMessage());
			
		ImObjectComponent component = ImComponentFactory.createObjectComponent(rawComponent.getImFlags(), 
				rawComponent.getVendorId(), 
				rawComponent.getType(),
				rawComponent.getCollectorId(),rawComponent.getValidatorId(), imMessage.getAttributes());
		
		return component;
	
	}
	
	private ImMessage byteArrayToMessage(byte[] message) throws TNCException{
		ImMessage imMessage = null;
		try {
			imMessage = this.byteReader.read(new ByteArrayInputStream(message), message.length);
		} catch (SerializationException e) {
			throw new TNCException(e.getMessage(),TNCException.TNC_RESULT_OTHER);
		} catch (ValidationException e) {
			throw new TNCException(e.getMessage(),TNCException.TNC_RESULT_OTHER);
		}
		
		return imMessage;
	}
	
	protected abstract void checkInitialization() throws TNCException;
	
}
