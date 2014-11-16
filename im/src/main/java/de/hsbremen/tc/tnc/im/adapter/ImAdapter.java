package de.hsbremen.tc.tnc.im.adapter;

import java.io.ByteArrayInputStream;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.im.adapter.data.ImComponentFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.data.ImRawComponent;
import de.hsbremen.tc.tnc.m.serialize.ImMessageContainer;
import de.hsbremen.tc.tnc.m.serialize.ImReader;

public abstract class ImAdapter{

	private final ImReader<ImMessageContainer> byteReader;
	
	@SuppressWarnings("unchecked")
	protected ImAdapter(ImReader<? extends ImMessageContainer> imReader) {
		this.byteReader = (ImReader<ImMessageContainer>)imReader;
	}
	
	protected ImObjectComponent receiveMessage(ImRawComponent rawComponent) throws TncException{
		ImMessageContainer imMessage = this.byteArrayToMessage(rawComponent.getMessage());
			
		ImObjectComponent component = ImComponentFactory.createObjectComponent(rawComponent.getImFlags(), 
				rawComponent.getVendorId(), 
				rawComponent.getType(),
				rawComponent.getCollectorId(),rawComponent.getValidatorId(), imMessage.getResult().getAttributes());
		
		return component;
	
	}
	
	private ImMessageContainer byteArrayToMessage(byte[] message) throws TncException{
		ImMessageContainer imMessage = null;
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
