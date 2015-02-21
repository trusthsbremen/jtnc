package org.ietf.nea.pa.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.ImMessageContainer;
import de.hsbremen.tc.tnc.util.NotNull;

public class DefaultMessageContainer implements ImMessageContainer {

	private final PaMessage message;
	
	private final List<ValidationException> exceptions;

	public DefaultMessageContainer(PaMessage message, List<ValidationException> exceptions) {
		
		NotNull.check("Message cannot be null.", message);
		
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
