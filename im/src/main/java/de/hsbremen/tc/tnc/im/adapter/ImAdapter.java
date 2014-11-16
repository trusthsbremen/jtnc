package de.hsbremen.tc.tnc.im.adapter;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.im.adapter.data.ImComponentFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.data.ImRawComponent;
import de.hsbremen.tc.tnc.m.attribute.ImAttribute;
import de.hsbremen.tc.tnc.m.serialize.ImMessageContainer;
import de.hsbremen.tc.tnc.m.serialize.ImReader;

public abstract class ImAdapter{

	private final ImReader<ImMessageContainer> byteReader;
	private final int messageHeaderLength;
	
	@SuppressWarnings("unchecked")
	protected ImAdapter(ImReader<? extends ImMessageContainer> imReader) {
		this.byteReader = (ImReader<ImMessageContainer>)imReader;
		this.messageHeaderLength = this.byteReader.getMinDataLength();
	}
	
	protected ImObjectComponent receiveMessage(ImRawComponent rawComponent) throws TncException{
		ImMessageContainer imMessage = null;
		ImObjectComponent component = null;
		
		try{
			imMessage = this.byteArrayToMessage(rawComponent.getMessage());
		}catch(ValidationException e){
			List<ValidationException> exceptions = new ArrayList<>();
			exceptions.add(e);
			component =  ImComponentFactory.createFaultyObjectComponent(rawComponent.getImFlags(), 
					rawComponent.getVendorId(), 
					rawComponent.getType(),
					rawComponent.getCollectorId(),rawComponent.getValidatorId(), new ArrayList<ImAttribute>(),exceptions, Arrays.copyOf(rawComponent.getMessage(), messageHeaderLength));
		}
		
		if(imMessage != null){
			if(imMessage.getExceptions() == null || imMessage.getExceptions().isEmpty()){
				component = ImComponentFactory.createObjectComponent(rawComponent.getImFlags(), 
					rawComponent.getVendorId(), 
					rawComponent.getType(),
					rawComponent.getCollectorId(),rawComponent.getValidatorId(), (imMessage.getResult() != null) ? imMessage.getResult().getAttributes() : new ArrayList<ImAttribute>());
			}else{
				component = ImComponentFactory.createFaultyObjectComponent(rawComponent.getImFlags(), 
						rawComponent.getVendorId(), 
						rawComponent.getType(),
						rawComponent.getCollectorId(),rawComponent.getValidatorId(), (imMessage.getResult() != null) ? imMessage.getResult().getAttributes() : new ArrayList<ImAttribute>(), imMessage.getExceptions(), Arrays.copyOf(rawComponent.getMessage(), messageHeaderLength));
			}
		}else{
			throw new TncException("Message was NULL after parsing,  this should never happen.", TncExceptionCodeEnum.TNC_RESULT_FATAL);
		}
		
		return component;
	}

	
	
	
	private ImMessageContainer byteArrayToMessage(byte[] message) throws TncException, ValidationException{
		ImMessageContainer imMessage = null;
		try {
			imMessage = this.byteReader.read(new ByteArrayInputStream(message), message.length);
		} catch (SerializationException e) {
			throw new TncException(e.getMessage(),TncExceptionCodeEnum.TNC_RESULT_OTHER);
		}
		
		return imMessage;
	}
	
}
