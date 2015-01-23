package de.hsbremen.tc.tnc.tnccs.session.base;

import de.hsbremen.tc.tnc.transport.TnccsListener;

public interface Session extends SessionBase, TnccsListener, HandshakeRetryListener{

}
