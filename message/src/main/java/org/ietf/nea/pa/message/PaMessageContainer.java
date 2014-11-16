package org.ietf.nea.pa.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.m.serialize.ImMessageContainer;

public class PaMessageContainer implements ImMessageContainer {

	private final PaMessage message;
	
	private final List<ValidationException> exceptions;

	public PaMessageContainer(PaMessage message, List<ValidationException> exceptions) {
		
		if(message == null){
			throw new NullPointerException("Message cannot be null.");
		}
		
		this.message = message;
		this.exceptions = ((exceptions != null) ? exceptions : new ArrayList<ValidationException>(0));
	}

	@Override
	public List<ValidationException> getExceptions() {
		return Collections.unmodifiableList(this.exceptions);
	}

	@Override
	public PaMessage getResult() {
		return this.message;
	}

}
