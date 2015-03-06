/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Carl-Heinz Genzel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
package org.ietf.nea.pt.socket;

import java.net.Socket;
import java.util.concurrent.Executors;

import org.ietf.nea.pt.DefaultTransportAttributes;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.message.TcgProtocolBindingIdentifier;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.message.t.serialize.TransportMessageContainer;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportReader;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportWriter;
import de.hsbremen.tc.tnc.transport.TransportConnection;
import de.hsbremen.tc.tnc.transport.TransportConnectionBuilder;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Builder to create a TransportConnection based on an underlying socket.
 *
 */
public class SocketTransportConnectionBuilder implements
        TransportConnectionBuilder<Socket> {

    private static final int DEFAULT_IM_COUNT = 10;

    private final TcgProtocolBindingIdentifier tProtocol;

    private final TransportWriter<TransportMessage> writer;
    private final TransportReader<TransportMessageContainer> reader;

    private final long memoryBoarder;

    private long messageLength;
    private long imMessageLength;
    private long maxRoundTrips;

    /**
     * Creates a builder for a TransportConnection objekt using the specified
     * reader and writer for serialization.
     *
     * @param tProtocol the transport protocol identifier
     * @param writer the transport protocol serializer
     * @param reader the transport protocol parser
     */
    public SocketTransportConnectionBuilder(
            final TcgProtocolBindingIdentifier tProtocol,
            final TransportWriter<TransportMessage> writer,
            final TransportReader<TransportMessageContainer> reader) {

        NotNull.check("Protocol identifier cannot be null.", tProtocol);
        NotNull.check("Writer cannot be null.", writer);
        NotNull.check("Reader cannot be null.", reader);

        this.tProtocol = tProtocol;

        this.writer = writer;
        this.reader = reader;

        this.memoryBoarder = Runtime.getRuntime().totalMemory();

        this.messageLength = (memoryBoarder / 2);
        this.imMessageLength = (memoryBoarder / DEFAULT_IM_COUNT);
        this.maxRoundTrips = HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNKNOWN;
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
    public TransportReader<TransportMessageContainer> getReader() {
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
     * @param messageLength the maximum message length
     * @throws IllegalArgumentException if message length <= 0
     * or length > available heap space
     * @return the SocketTransportConnectionBuilder for fluent use
     */
    public SocketTransportConnectionBuilder setMessageLength(
            final long messageLength) {
        if (messageLength <= 0 || messageLength > this.memoryBoarder) {
            throw new IllegalArgumentException(
                    "Message length is not acceptable, "
                            + "it must be between 1 and " + this.memoryBoarder
                            + " bytes.");
        }
        this.messageLength = messageLength;

        return this;
    }

    /**
     * Sets the maximum IF-M message length.
     *
     * @param imMessageLength the maximum IF-M message length
     * @throws IllegalArgumentException if message length <= 0
     * or length > available heap space
     * @return the SocketTransportConnectionBuilder for fluent use
     */
    public SocketTransportConnectionBuilder setImMessageLength(
            final long imMessageLength) {
        if (imMessageLength <= 0 || imMessageLength > this.memoryBoarder) {
            throw new IllegalArgumentException(
                    "Message length is not acceptable, "
                            + "it must be between 1 and " + this.memoryBoarder
                            + " bytes.");
        }
        this.imMessageLength = imMessageLength;

        return this;
    }

    /**
     * Sets the maximum round trips.
     *
     * @param maxRoundTrips the maximum round trips
     * @throws IllegalArgumentException if maximum round trips <= 0
     * @return the SocketTransportConnectionBuilder for fluent use
     */
    public SocketTransportConnectionBuilder setMaxRoundTrips(
            final long maxRoundTrips) {
        if (maxRoundTrips <= 0) {
            throw new IllegalArgumentException("Round trips cannot be null.");
        }
        this.maxRoundTrips = maxRoundTrips;

        return this;
    }

    @Override
    public TransportConnection toConnection(final String id,
            final boolean selfInitiated, final boolean server,
            final Socket underlying) {

        NotNull.check("Underlying cannot be null.", underlying);

        Socket socket = underlying;

        DefaultTransportAttributes attributes = new DefaultTransportAttributes(
                id, this.tProtocol, this.messageLength,
                this.imMessageLength, this.maxRoundTrips);

        SocketTransportConnection t = new SocketTransportConnection(
                selfInitiated, server, socket, attributes, writer, reader,
                Executors.newSingleThreadExecutor());

        return t;
    }

    @Override
    public TransportConnection toConnection(final boolean selfInitiated,
            final boolean server, final Socket underlying) {

        NotNull.check("Underlying cannot be null.", underlying);

        if (!(underlying instanceof Socket)) {
            throw new IllegalArgumentException("Underlying must be of type "
                    + Socket.class.getCanonicalName() + ".");
        }

        Socket socket = underlying;

        String id = socket.getInetAddress().getHostAddress();

        return this.toConnection(id, selfInitiated, server, underlying);

    }

}
