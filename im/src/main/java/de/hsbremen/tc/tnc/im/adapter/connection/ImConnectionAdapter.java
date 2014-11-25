package de.hsbremen.tc.tnc.im.adapter.connection;

import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.connection.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;

public interface ImConnectionAdapter {

	public abstract void sendMessage(ImObjectComponent component, long identifier) throws TncException, ValidationException;

	public abstract void requestHandshakeRetry(ImHandshakeRetryReasonEnum reason) throws TncException;

	public abstract Object getAttribute(TncAttributeType type) throws TncException;
}