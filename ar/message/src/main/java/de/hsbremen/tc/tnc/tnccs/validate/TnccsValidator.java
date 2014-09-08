package de.hsbremen.tc.tnc.tnccs.validate;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public interface TnccsValidator<T> {

    public abstract void validate(final T structure) throws ValidationException;

    public abstract void addValidator(Long vendorId , Long messageType, TnccsValidator<T> validator);

    public abstract void removeValidator(Long vendorId, Long messageType);
}