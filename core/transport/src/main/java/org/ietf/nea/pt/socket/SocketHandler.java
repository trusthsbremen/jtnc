package org.ietf.nea.pt.socket;

import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public interface SocketHandler {

    boolean isOpen();

    void initialize() throws ConnectionException;

    void close();

    long getRxCounter();

    long getTxCounter();

    void resetCounters();

}