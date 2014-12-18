package de.hsbremen.tc.tnc.tnccs.session.base;

import de.hsbremen.tc.tnc.transport.newp.connection.TnccsValueListener;

public interface NewSession extends NewSessionBase, TnccsValueListener, HandshakeRetryListener{

}
