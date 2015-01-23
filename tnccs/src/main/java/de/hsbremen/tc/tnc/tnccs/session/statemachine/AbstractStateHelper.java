package de.hsbremen.tc.tnc.tnccs.session.statemachine;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsContentHandler;

public abstract class AbstractStateHelper<T extends TnccsContentHandler> implements StateHelper<T>{

	private final T contentHandler;
	private final Attributed attributes;
	
	public AbstractStateHelper(Attributed attributes, T contentHandler){
		if(attributes == null){
			throw new NullPointerException("Attributes cannot be null.");
		}
		if(contentHandler == null){
			throw new NullPointerException("Content handler cannot be null.");
		}
		this.attributes = attributes;
		this.contentHandler = contentHandler;
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper#getAttributes()
	 */
	@Override
	public Attributed getAttributes() {
		return this.attributes;
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper#getHander()
	 */
	@Override
	public T getHandler() {
		return this.contentHandler;
	}
}
