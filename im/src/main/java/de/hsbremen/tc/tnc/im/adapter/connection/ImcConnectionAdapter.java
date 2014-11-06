package de.hsbremen.tc.tnc.im.adapter.connection;

import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.im.adapter.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;

public interface ImcConnectionAdapter {

	public abstract void sendMessage(ImObjectComponent component, long identifier) throws TNCException;

	public abstract void requestHandshakeRetry(ImHandshakeRetryReasonEnum reason) throws TNCException;

}