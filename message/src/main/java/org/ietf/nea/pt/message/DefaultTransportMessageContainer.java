package org.ietf.nea.pt.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.message.t.serialize.TransportMessageContainer;

public class DefaultTransportMessageContainer implements TransportMessageContainer{

	private final TransportMessage message;
	private final List<ValidationException> exceptions;

	public DefaultTransportMessageContainer(TransportMessage message, List<ValidationException> exceptions) {
		this.message = message;
		this.exceptions = ((exceptions != null) ? exceptions : new ArrayList<ValidationException>(0));
	}
	
	@Override
	public List<ValidationException> getExceptions() {
		return Collections.unmodifiableList(this.exceptions);
	}

	@Override
	public TransportMessage getResult() {
		return this.message;
	}

}
