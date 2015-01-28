package de.hsbremen.tc.tnc.tnccs.message.handler.simple;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsHandlerBuilder;
import de.hsbremen.tc.tnc.tnccs.session.base.AttributeCollection;

public class DefaultTnccHandlerBuilder implements TnccsHandlerBuilder<TnccHandler> {

	private Attributed attributes;
	
	public DefaultTnccHandlerBuilder(){
		this.attributes = new AttributeCollection();
	}
	
	@Override
	public TnccsHandlerBuilder<TnccHandler> setAttributes(Attributed attributes){
		if(attributes != null){
			this.attributes = attributes;
		}
		
		return this;
	}
	
	@Override
	public TnccHandler toHandler(){
		
		TnccHandler tncsHandler = new DefaultTnccHandler(attributes);
		
		// clear side effects
		this.attributes = new AttributeCollection();
		
		return tncsHandler;
	}
	
}
