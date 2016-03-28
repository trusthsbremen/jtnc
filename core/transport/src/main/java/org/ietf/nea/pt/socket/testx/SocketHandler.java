package org.ietf.nea.pt.socket.testx;

import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public interface SocketHandler {

    public abstract boolean isOpen();

    public abstract void initialize() throws ConnectionException;

    public abstract void close();

    public abstract long getRxCounter();

    public abstract long getTxCounter();

    public abstract void resetCounters();

}