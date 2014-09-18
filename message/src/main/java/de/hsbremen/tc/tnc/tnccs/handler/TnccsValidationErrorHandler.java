package de.hsbremen.tc.tnc.tnccs.handler;

import javax.xml.bind.ValidationException;

public interface TnccsValidationErrorHandler {

	abstract void handle(ValidationException m);
}
