package de.hsbremen.tc.tnc.im.adapter;

import java.io.ByteArrayInputStream;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.im.adapter.data.ImComponentFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.data.ImRawComponent;
import de.hsbremen.tc.tnc.m.message.ImMessage;
import de.hsbremen.tc.tnc.m.serialize.ImReader;

public abstract class ImAdapter{

	private final ImReader<ImMessage> byteReader;
	
	@SuppressWarnings("unchecked")
	protected ImAdapter(ImReader<? extends ImMessage> imReader) {
		this.byteReader = (ImReader<ImMessage>)imReader;
	}
	
	protected ImObjectComponent receiveMessage(ImRawComponent rawComponent) throws TncException{
		ImMessage imMessage = this.byteArrayToMessage(rawComponent.getMessage());
			
		ImObjectComponent component = ImComponentFactory.createObjectComponent(rawComponent.getImFlags(), 
				rawComponent.getVendorId(), 
				rawComponent.getType(),
				rawComponent.getCollectorId(),rawComponent.getValidatorId(), imMessage.getAttributes());
		
		return component;
	
	}
	
	private ImMessage byteArrayToMessage(byte[] message) throws TncException{
		ImMessage imMessage = null;
		try {
			imMessage = this.byteReader.read(new ByteArrayInputStream(message), message.length);
		} catch (SerializationException e) {
			throw new TncException(e.getMessage(),TncExceptionCodeEnum.TNC_RESULT_OTHER);
		} catch (ValidationException e) {
			throw new TncException(e.getMessage(),TncExceptionCodeEnum.TNC_RESULT_OTHER);
		}
		
		return imMessage;
	}
	
}
