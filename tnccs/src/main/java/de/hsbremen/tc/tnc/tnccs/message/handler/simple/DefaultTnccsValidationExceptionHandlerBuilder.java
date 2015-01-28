package de.hsbremen.tc.tnc.tnccs.message.handler.simple;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsHandlerBuilder;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsValidationExceptionHandler;
import de.hsbremen.tc.tnc.tnccs.session.base.AttributeCollection;

public class DefaultTnccsValidationExceptionHandlerBuilder implements TnccsHandlerBuilder<TnccsValidationExceptionHandler> {

	private Attributed attributes;
	
	public DefaultTnccsValidationExceptionHandlerBuilder(){
		this.attributes = new AttributeCollection();
	}
	
	public TnccsHandlerBuilder<TnccsValidationExceptionHandler> setAttributes(Attributed attributes){
		if(attributes != null){
			this.attributes = attributes;
		}
		
		return this;
	}
	
	public TnccsValidationExceptionHandler toHandler(){
		
		TnccsValidationExceptionHandler exHandler = new DefaultTnccsValidationExceptionHandler(attributes);
		
		// clear side effects
		this.attributes = new AttributeCollection();
		
		return exHandler;
	}
	
}
