package de.hsbremen.tc.tnc.tnccs.session.statemachine.simple;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.tnccs.message.handler.TncsContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.base.AttributeCollection;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachine;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachineBuilder;

public class DefaultServerStateMachineBuilder implements StateMachineBuilder<TncsContentHandler> {


	private Attributed attributes;
	private TncsContentHandler contentHandler;
	private StateHelper<TncsContentHandler> serverStateFactory;
	
	public DefaultServerStateMachineBuilder(){
		this.attributes = new AttributeCollection();
		this.contentHandler = null;
		this.serverStateFactory = null;
	}
	
	@Override
	public StateMachineBuilder<TncsContentHandler> setAttributes(Attributed attributes){
		if(attributes != null){
			this.attributes = attributes;
		}
		
		return this;
	}
	
	@Override
	public StateMachineBuilder<TncsContentHandler> setContentHandler(TncsContentHandler contentHandler){
		if(contentHandler != null){
			this.contentHandler = contentHandler;
		}
		
		return this;
	}
	
	@Override
	public StateMachineBuilder<TncsContentHandler> setStateHelper(StateHelper<TncsContentHandler> serverStateFactory){
		if(serverStateFactory != null){
			this.serverStateFactory = serverStateFactory;
			this.contentHandler = null;
			this.attributes = null;
		}
		
		return this;
	}
	
	@Override
	public StateMachine toStateMachine(){
		
		StateMachine machine = null;
		
		if(this.serverStateFactory != null){
			machine = new DefaultServerStateMachine(serverStateFactory);
		}else{
			if(this.attributes != null && this.contentHandler != null){
				machine = new DefaultServerStateMachine(new DefaultServerStateFactory(attributes,contentHandler));
			}else{
				throw new IllegalStateException("Not all necessary attributes set. A state helper or the attributes and the content handler must be set first.");
			}
		}
		
		// remove side effects
		this.attributes = new AttributeCollection();
		this.contentHandler = null;
		this.serverStateFactory = null;
	
		return machine;
	}
	
}
