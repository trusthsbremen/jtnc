package de.hsbremen.tc.tnc.tnccs.session.base;

import de.hsbremen.tc.tnc.transport.TnccsValueListener;

public interface Session extends SessionBase, TnccsValueListener, HandshakeRetryListener{

}
