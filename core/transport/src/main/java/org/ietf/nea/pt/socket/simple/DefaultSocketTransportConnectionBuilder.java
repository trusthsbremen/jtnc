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

import java.net.Socket;
import java.util.concurrent.Executors;

import org.ietf.nea.pt.socket.Authenticator;
import org.ietf.nea.pt.socket.Negotiator;
import org.ietf.nea.pt.socket.Receiver;
import org.ietf.nea.pt.socket.sasl.SaslClientMechansims;
import org.ietf.nea.pt.socket.sasl.SaslMechanismSelection;
import org.ietf.nea.pt.socket.sasl.SaslServerMechansims;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.message.TcgProtocolBindingIdentifier;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportReader;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportWriter;
import de.hsbremen.tc.tnc.transport.DefaultTransportAttributes;
import de.hsbremen.tc.tnc.transport.TransportConnection;
import de.hsbremen.tc.tnc.transport.TransportConnectionBuilder;
import de.hsbremen.tc.tnc.util.NotNull;

//TODO change to add authenticator and negotiator
/**
 * Builder to create a TransportConnection based on an underlying socket.
 * 
 * @param <T> the SaslMechanismSelection type, that is used by the builder
 * according to which connection type is build (NAA (server) or NAR (client))
 */
public class DefaultSocketTransportConnectionBuilder
    <T extends SaslMechanismSelection> implements
        TransportConnectionBuilder<Socket> {

    private static final int DEFAULT_IM_COUNT = 10;

    private final TcgProtocolBindingIdentifier tProtocol;

    private final TransportWriter<TransportMessage> writer;
    private final TransportReader<TransportMessage> reader;
    
    private final long memoryBoarder;

    private final boolean server;
    
    private long messageLength;
    private long imMessageLength;
    private long maxRoundTrips;
    
    private T mechanismSelection;

    /**
     * Creates a builder for a TransportConnection object using the specified
     * reader and writer for serialization.
     *
     * @param server the flag to determin if the build creates
     * connections for an NAA or NAR
     * @param tProtocol the transport protocol identifier
     * @param writer the transport protocol serializer
     * @param reader the transport protocol parser
     */
    public DefaultSocketTransportConnectionBuilder(final boolean server,
            final TcgProtocolBindingIdentifier tProtocol,
            final TransportWriter<TransportMessage> writer,
            final TransportReader<TransportMessage> reader) {

        NotNull.check("Protocol identifier cannot be null.", tProtocol);
        NotNull.check("Writer cannot be null.", writer);
        NotNull.check("Reader cannot be null.", reader);
        
        this.server = server;
        
        this.tProtocol = tProtocol;

        this.writer = writer;
        this.reader = reader;

        this.memoryBoarder = Runtime.getRuntime().totalMemory();

        this.messageLength = memoryBoarder / 2;
        this.imMessageLength = memoryBoarder / DEFAULT_IM_COUNT;
        this.maxRoundTrips = HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNKNOWN;
        
        this.mechanismSelection = null;
    }

    /**
     * Returns whether the builder is building connections for an NAA or NAR.
     *
     * @return the connection type to build (true = NAA, false = NAR)
     */
    public boolean isServerConnectionBuilder() {
        return this.server;
    }

    
    /**
     * Returns the transport protocol identifier.
     *
     * @return the transport protocol identifier
     */
    public TcgProtocolBindingIdentifier getTransportProtocolIdentifier() {
        return this.tProtocol;
    }

    /**
     * Returns the transport protocol writer.
     *
     * @return the protocol writer
     */
    public TransportWriter<TransportMessage> getWriter() {
        return this.writer;
    }

    /**
     * Returns the transport protocol reader.
     *
     * @return the protocol reader
     */
    public TransportReader<TransportMessage> getReader() {
        return this.reader;
    }

    /**
     * Returns the maximum full message length.
     *
     * @return the maximum full message length
     */
    public long getMessageLength() {
        return this.messageLength;
    }

    /**
     * Returns the maximum IF-M message length.
     *
     * @return the maximum IF-M message length
     */
    public long getImMessageLength() {
        return this.imMessageLength;
    }

    /**
     * Returns the maximum round trips.
     *
     * @return the maximum round trips
     */
    public long getMaxRoundTrips() {
        return this.maxRoundTrips;
    }

    /**
     * Sets the maximum full message length.
     *
     * @param maxMessageLength the maximum message length
     * @throws IllegalArgumentException if message length <= 0
     * or length > available heap space
     * @return the SocketTransportConnectionBuilder for fluent use
     */
    public DefaultSocketTransportConnectionBuilder<T> setMessageLength(
            final long maxMessageLength) {
        if (maxMessageLength <= 0 || maxMessageLength > this.memoryBoarder) {
            throw new IllegalArgumentException(
                    "Message length is not acceptable, "
                            + "it must be between 1 and " + this.memoryBoarder
                            + " bytes.");
        }
        this.messageLength = maxMessageLength;

        return this;
    }

    /**
     * Sets the maximum IF-M message length.
     *
     * @param maxImMessageLength the maximum IF-M message length
     * @throws IllegalArgumentException if message length <= 0
     * or length > available heap space
     * @return the SocketTransportConnectionBuilder for fluent use
     */
    public DefaultSocketTransportConnectionBuilder<T> setImMessageLength(
            final long maxImMessageLength) {
        if (maxImMessageLength <= 0
                || maxImMessageLength > this.memoryBoarder) {

            throw new IllegalArgumentException(
                    "Message length is not acceptable, "
                            + "it must be between 1 and " + this.memoryBoarder
                            + " bytes.");
        }

        this.imMessageLength = maxImMessageLength;

        return this;
    }

    /**
     * Sets the maximum round trips.
     *
     * @param maxRounds the maximum round trips
     * @throws IllegalArgumentException if maximum round trips <= 0
     * @return the SocketTransportConnectionBuilder for fluent use
     */
    public DefaultSocketTransportConnectionBuilder<T> setMaxRoundTrips(
            final long maxRounds) {

        if (maxRounds <= 0) {
            throw new IllegalArgumentException("Round trips cannot be null.");
        }
        this.maxRoundTrips = maxRounds;

        return this;
    }
    
    /**
     * Adds SASL authentication mechanisms for the next connection to be build.
     * All added SASL authentication mechanisms must not be completed or
     * the connection establishment will fail at a later point!
     * Therefore new SASL mechanisms must be added for every connection
     * that is build with this builder, if an authentication is needed.
     * 
     * @param mechanisms the SASL mechanisms to use for
     * the next connection
     * @throws IllegalArgumentException if mechanism selection does not match
     * the connection type (server or client)
     * @return the SocketTransportConnectionBuilder for fluent use
     */
    public DefaultSocketTransportConnectionBuilder<T>
        addAuthenticationMechanisms(T mechanisms) {

        if (mechanisms == null
                || (this.server && mechanisms instanceof SaslServerMechansims)
                || (!this.server && mechanisms instanceof SaslClientMechansims))
        {
            
            this.mechanismSelection = mechanisms;
            
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Type of mechanism selection not expected. ");

            if (this.server) {
                sb.append("Builder is configured to build server connections.");
            } else {
                sb.append("Builder is configured to build client connections.");
            }
            
            throw new IllegalArgumentException(sb.toString());
        }
        
        return this;
        
    }
    
    @Override
    public TransportConnection toConnection(final String id,
            final boolean selfInitiated, final Socket underlying) {

        NotNull.check("Underlying cannot be null.", underlying);

        Socket socket = underlying;

        DefaultTransportAttributes attributes = new DefaultTransportAttributes(
                id, this.tProtocol, this.messageLength,
                this.imMessageLength, this.maxRoundTrips);
        
        DefaultSocketWrapper socketWrapper = new DefaultSocketWrapper(
                this.messageLength, socket, writer, reader);

        Negotiator negotiator = null;
        if (selfInitiated) {
            negotiator = new DefaultNegotiationInitiator(
                    (short) 1, (short) 1, (short) 1);
        } else {
            negotiator = new DefaultNegotiationResponder(
                    (short) 1, (short) 1, (short) 1);
        }
        
        Authenticator authenticator = null;
        if (this.server) {
            authenticator  = new DefaultAuthenticationServer(
                    (SaslServerMechansims) this.mechanismSelection);
        } else {
            authenticator  = new DefaultAuthenticationClient(
                    (SaslClientMechansims) this.mechanismSelection);
        }
        
        //remove side effects;
        this.mechanismSelection = null; 
        
        Receiver receiver = new DefaultBatchReceiver();
        
        DefaultSocketTransportConnection t =
                new DefaultSocketTransportConnection(
                selfInitiated, attributes, socketWrapper, negotiator,
                authenticator, receiver, Executors.newSingleThreadExecutor());

        return t;
    }

    @Override
    public TransportConnection toConnection(final boolean selfInitiated,
            final Socket underlying) {

        NotNull.check("Underlying cannot be null.", underlying);

        if (!(underlying instanceof Socket)) {
            throw new IllegalArgumentException("Underlying must be of type "
                    + Socket.class.getCanonicalName() + ".");
        }

        Socket socket = underlying;

        String id = socket.getInetAddress().getHostAddress();

        return this.toConnection(id, selfInitiated, underlying);

    }

}
