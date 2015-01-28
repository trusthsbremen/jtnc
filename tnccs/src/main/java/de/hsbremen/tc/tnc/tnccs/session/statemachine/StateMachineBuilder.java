package de.hsbremen.tc.tnc.tnccs.session.statemachine;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsContentHandler;

public interface StateMachineBuilder<T extends TnccsContentHandler> {

	public abstract StateMachineBuilder<T> setAttributes(
			Attributed attributes);

	public abstract StateMachineBuilder<T> setContentHandler(
			T contentHandler);

	public abstract StateMachineBuilder<T> setStateHelper(
			StateHelper<T> serverStateFactory);

	public abstract StateMachine toStateMachine();

}