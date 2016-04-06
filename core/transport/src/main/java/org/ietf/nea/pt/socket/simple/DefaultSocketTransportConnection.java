/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright © 2015 Trust HS Bremen and its Contributors. All rights   
 * reserved.
 *
 * See the CONTRIBUTORS file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.ietf.nea.pt.socket.simple;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ietf.nea.pt.message.PtTlsMessageFactoryIetf;
import org.ietf.nea.pt.socket.Authenticator;
import org.ietf.nea.pt.socket.Negotiator;
import org.ietf.nea.pt.socket.Receiver;
import org.ietf.nea.pt.socket.SocketTransportConnectionPhaseEnum;
import org.ietf.nea.pt.socket.SocketWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.transport.TransportAttributes;
import de.hsbremen.tc.tnc.transport.TransportConnection;
import de.hsbremen.tc.tnc.transport.TransportConnectionPhase;
import de.hsbremen.tc.tnc.transport.TransportListener;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;
import de.hsbremen.tc.tnc.transport.exception.ListenerClosedException;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Transport connection with an underlying Socket.
 *
 */
public class DefaultSocketTransportConnection implements TransportConnection {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultSocketTransportConnection.class);

    private final boolean selfInitiated;

    private final TransportAttributes attributes;

    private final SocketWrapper socketWrapper;

    private final ExecutorService runner;

    private final Authenticator authenticator;

    private final Negotiator negotiator;

    private final Receiver transporter;

    private TransportConnectionPhase phase;

    private TransportListener listener;

    /**
     * Creates a SocketTransportConnection with corresponding transport
     * attributes and a wrapped socket.
     * @param selfInitiated true if connection was initiated by this side and
     * false if not
     * @param attributes the transport connection attributes
     * @param socketWrapper the wrapped socket
     * @param negotiator the version negotiator for version negotiation phase
     * @param authenticator the authenticator for authentication phase
     * @param receiver the message receiver for transport phase
     * @param runner the executor to run the receiver in transport phase
     */
    public DefaultSocketTransportConnection(final boolean selfInitiated,
            final TransportAttributes attributes,
            final SocketWrapper socketWrapper,
            final Negotiator negotiator, final Authenticator authenticator,
            final Receiver receiver, final ExecutorService runner) {
        
        NotNull.check("Constructor arguments cannot be null.", attributes,
                socketWrapper, negotiator, authenticator, receiver);
        
        this.selfInitiated = selfInitiated;
        this.socketWrapper = socketWrapper;

        this.negotiator = negotiator;
        this.authenticator = authenticator;
        this.transporter = receiver;

        this.attributes = attributes;
        this.runner = (runner == null) ? Executors.newSingleThreadExecutor()
                : runner;

        this.phase = SocketTransportConnectionPhaseEnum
                .TRSPT_CONNECTION_PHASE_PENDING;
    }

    @Override
    public boolean isSelfInititated() {
        return this.selfInitiated;
    }

    @Override
    public boolean isOpen() {
        return this.socketWrapper.isOpen();
    }

    @Override
    public void bootstrap() throws ConnectionException {
        LOGGER.debug("Open transport connection.");

        if (!this.phase
                .equals(SocketTransportConnectionPhaseEnum
                        .TRSPT_CONNECTION_PHASE_PENDING)) {
            if (this.phase
                    .equals(SocketTransportConnectionPhaseEnum
                            .TRSPT_CONNECTION_PHASE_TRANSPORT)) {

                LOGGER.warn("Initialize was already called. "
                        + "Connection is in phase " + this.phase.toString()
                        + " Ignoring this call.");
            } else {

                LOGGER.error(new StringBuilder()
                        .append("Connection is stuck in phase ")
                        .append(this.phase.toString())
                        .append(". Connection will be closed.").toString());

                this.close();

                throw new ConnectionException("Phase transition was stuck"
                        + "in phase " + this.phase.toString()
                        + "and connection was closed.");
            }
        }

        try {

            this.socketWrapper.initialize();

            this.phase = SocketTransportConnectionPhaseEnum
                    .TRSPT_CONNECTION_PHASE_NEGOTIATE_VERSION;

            this.negotiator.negotiate(this.socketWrapper);

            LOGGER.debug("Version " + this.negotiator.getNegotiatedVersion()
                    + " has been negotiated.");

            this.phase = SocketTransportConnectionPhaseEnum
                    .TRSPT_CONNECTION_PHASE_AUTHENTICATE;

            this.authenticator.authenticate(this.socketWrapper);

            LOGGER.debug("Authentication result: "
                    + ((this.authenticator.getAuthenticationResult() == null)
                            ? "UNKNOWN" : this.authenticator
                                    .getAuthenticationResult().toString()));

            this.phase = SocketTransportConnectionPhaseEnum
                    .TRSPT_CONNECTION_PHASE_TRANSPORT;

        } catch (SerializationException | ConnectionException e) {
            LOGGER.error("Error occured, while initializing the connection. "
                    + "Connection will be closed.", e);

            this.close();

            throw new ConnectionException("Fatal exception has occured "
                    + "and connection was closed.", e);

        } catch (ValidationException e) {

            LOGGER.error("Error occured, while initializing the connection. "
                    + "Will try to close connection gracefully.", e);

            try {
                // try to close gracefully
                TransportMessage m = this.socketWrapper
                        .createValidationErrorMessage(e);

                this.socketWrapper.writeToStream(m);

            } catch (ValidationException | SerializationException e1) {

                LOGGER.error("Gracefull close was not successfull. "
                        + "Connection will be closed.", e);

                this.close();

                throw new ConnectionException("Fatal exception has occured "
                        + "and connection was closed.", e);

            }

        }

    }

    @Override
    public void activate(final TransportListener tnccsMessageListener)
            throws ConnectionException {

        if (this.phase
                .equals(SocketTransportConnectionPhaseEnum
                        .TRSPT_CONNECTION_PHASE_PENDING)) {
            this.bootstrap();
        }

        if (!this.phase
                .equals(SocketTransportConnectionPhaseEnum
                        .TRSPT_CONNECTION_PHASE_TRANSPORT)) {

            throw new ConnectionException(
                    new StringBuilder(
                            "Connection is not ready. Connection is in ")
                            .append("phase ")
                            .append(this.phase.toString())
                            .append(". It has to be in phase ")
                            .append(SocketTransportConnectionPhaseEnum
                                    .TRSPT_CONNECTION_PHASE_TRANSPORT
                                    .toString())
                            .append(" to receive messages")
                            .append(" for the upper layers.")
                            .append(" Initialize must be called first.")
                            .toString(), this.phase);
        }

        this.listener = tnccsMessageListener;

        // reset rx/tx counter to measure only the integrity transports
        LOGGER.debug("Version negotiation and authentication"
                + " completed successfully."
                + " Transport phase started.");
        this.socketWrapper.resetCounters();

        // start transport phase
        this.runner.execute(new Receive());

    }

    @Override
    public void send(ByteBuffer buf) throws ConnectionException {
        if (buf != null && buf.bytesWritten() > buf.bytesRead()) {
            if (isOpen()) {
                try {

                    TransportMessage m = PtTlsMessageFactoryIetf.createPbBatch(
                            this.socketWrapper.getIdentifier(), buf);
                    this.socketWrapper.writeToStream(m);
                    this.checkRoundTrips();
                } catch (SerializationException | ValidationException e) {
                    throw new ConnectionException(
                            "Data could not be written to stream.", e);
                }
            } else {
                throw new ConnectionException("Connection seems not open.");
            }
        }
    }

    @Override
    public void close() {
        LOGGER.debug("Connection close() called. Closing connection...");

        runner.shutdown();
        this.socketWrapper.close();

        runner.shutdownNow();
        this.listener.notifyClose();

    }

    @Override
    public Attributed getAttributes() {
        return this.attributes;
    }

    /**
     * Checks the round trip counter if the maximum round trips are exceeded.
     */
    private void checkRoundTrips() {
        long maxRoundTrips = this.attributes.getMaxRoundTrips();
        if (HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNKNOWN < maxRoundTrips
                && maxRoundTrips < HSBConstants
                    .TCG_IM_MAX_ROUND_TRIPS_UNLIMITED) {

            long rx = this.socketWrapper.getRxCounter();
            long tx = this.socketWrapper.getTxCounter();
            long roundTrips = Math.min(rx, tx);

            if (roundTrips >= maxRoundTrips) {
                LOGGER.debug("Round trip limit exceeded: ( Messages received:"
                        + rx + ", Messages send:" + tx + ", Rounds:"
                        + roundTrips + ")");

                this.close();
            }
        }
    }

    /**
     * Runnable which handles the transport phase with a remote peer. It listens
     * for incoming messages.
     * 
     */
    private class Receive implements Runnable {

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {

                    transporter.receive(socketWrapper);
                    ByteBuffer tnccsData = transporter.getTnccsData();

                    if (tnccsData != null) {
                        listener.receive(tnccsData);
                    }

                }
            } catch (SerializationException | ConnectionException
                    | ListenerClosedException e) {

                LOGGER.error(
                        "Connection has thrown an error and will be closed.",
                        e);

            } catch (ValidationException e) {

                try {
                    // try to close gracefully
                    TransportMessage m = socketWrapper
                            .createValidationErrorMessage(e);

                    socketWrapper.writeToStream(m);

                } catch (ValidationException | ConnectionException
                        | SerializationException e1) {

                    // ignore and just close

                }

            } finally {
                close();

            }

        }

    }
}
