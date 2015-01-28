package de.hsbremen.tc.tnc.tnccs.session.statemachine.simple;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.base.AttributeCollection;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachine;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachineBuilder;

public class DefaultClientStateMachineBuilder implements StateMachineBuilder<TnccContentHandler> {


	private Attributed attributes;
	private TnccContentHandler contentHandler;
	private StateHelper<TnccContentHandler> clientStateFactory;
	
	public DefaultClientStateMachineBuilder(){
		this.attributes = new AttributeCollection();
		this.contentHandler = null;
		this.clientStateFactory = null;
	}
	
	@Override
	public StateMachineBuilder<TnccContentHandler> setAttributes(Attributed attributes){
		if(attributes != null){
			this.attributes = attributes;
		}
		
		return this;
	}
	
	@Override
	public StateMachineBuilder<TnccContentHandler> setContentHandler(TnccContentHandler contentHandler){
		if(contentHandler != null){
			this.contentHandler = contentHandler;
		}
		
		return this;
	}
	
	@Override
	public StateMachineBuilder<TnccContentHandler> setStateHelper(StateHelper<TnccContentHandler> clientStateFactory){
		if(clientStateFactory != null){
			this.clientStateFactory = clientStateFactory;
			this.contentHandler = null;
			this.attributes = null;
		}
		
		return this;
	}
	
	@Override
	public StateMachine toStateMachine(){
		
		StateMachine machine = null;
		
		if(this.clientStateFactory != null){
			machine = new DefaultClientStateMachine(clientStateFactory);
		}else{
			if(this.attributes != null && this.contentHandler != null){
				machine = new DefaultClientStateMachine(new DefaultClientStateFactory(attributes,contentHandler));
			}else{
				throw new IllegalStateException("Not all necessary attributes set. A state helper or the attributes and the content handler must be set first.");
			}
		}
		
		// remove side effects
		this.attributes = new AttributeCollection();
		this.contentHandler = null;
		this.clientStateFactory = null;
	
		return machine;
	}
	
}
