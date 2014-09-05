package de.hsbremen.tc.tnc.tnccs.validate;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public interface TnccsHeaderValidator<T> {

    public abstract void validate(final T structure) throws ValidationException;

}