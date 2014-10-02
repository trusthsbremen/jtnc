package de.hsbremen.tc.tnc.tnccs.validate;

import org.ietf.nea.pb.exception.RuleException;

public interface TnccsValidator<T> {

    public abstract void validate(final T structure) throws RuleException;

}