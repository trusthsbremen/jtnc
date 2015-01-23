package de.hsbremen.tc.tnc.tnccs.session.base;

import java.util.HashSet;
import java.util.Set;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;

public class AttributeCollection implements Attributed{

	private final Set<? super Attributed> attributes;
	
	public AttributeCollection(){
		this.attributes = new HashSet<>();
	}
	
	public <T extends Attributed> void add(T attributes){
		this.attributes.add(attributes);
	}

	public <T extends Attributed> boolean remove(T attributes){
		return this.attributes.remove(attributes);
	}
	
	@Override
	public Object getAttribute(TncAttributeType type) throws TncException {
		Object o = null;
		for (Object attribut : attributes) {
			if(attribut instanceof Attributed){
				try{
					o = ((Attributed) attribut).getAttribute(type);
				}catch(TncException e){
					// ignore
				}
			}
		}
		
		if(o != null){
			return o;
		}else{
			throw new TncException("The attribute with ID " + type.id() + " is unknown.", TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
		}
	}

	@Override
	public void setAttribute(TncAttributeType type, Object value)
			throws TncException {
		for (Object attribut : attributes) {
			if(attribut instanceof Attributed){
				try{
					((Attributed) attribut).setAttribute(type, value);
				}catch(TncException e){
					// ignore
				}
			}
		}
		
		throw new TncException("The attribute with ID " + type.id() + " is unknown or not writeable.", TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
	}
	
	
	
	
}
