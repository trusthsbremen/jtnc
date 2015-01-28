package de.hsbremen.tc.tnc.tnccs.message.handler.simple;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsHandlerBuilder;
import de.hsbremen.tc.tnc.tnccs.message.handler.TncsHandler;
import de.hsbremen.tc.tnc.tnccs.session.base.AttributeCollection;

public class DefaultTncsHandlerBuilder implements TnccsHandlerBuilder<TncsHandler> {

	private Attributed attributes;
	
	public DefaultTncsHandlerBuilder(){
		this.attributes = new AttributeCollection();
	}
	
	@Override
	public TnccsHandlerBuilder<TncsHandler> setAttributes(Attributed attributes){
		if(attributes != null){
			this.attributes = attributes;
		}
		
		return this;
	}
	
	@Override
	public TncsHandler toHandler(){
		
		TncsHandler tncsHandler = new DefaultTncsHandler(attributes);
		
		// clear side effects
		this.attributes = new AttributeCollection();
		
		return tncsHandler;
	}
	
}
