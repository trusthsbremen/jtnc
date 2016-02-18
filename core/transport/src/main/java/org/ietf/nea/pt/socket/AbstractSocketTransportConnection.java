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
package org.ietf.nea.pt.socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.ietf.nea.pt.message.PtTlsMessageFactoryIetf;
import org.ietf.nea.pt.message.PtTlsMessageHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.message.t.serialize.TransportMessageContainer;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportReader;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.message.util.DefaultByteBuffer;
import de.hsbremen.tc.tnc.message.util.StreamedReadOnlyByteBuffer;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

/**
 * Transport connection with an underlying Socket.
 *
 */
public abstract class AbstractSocketTransportConnection {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractSocketTransportConnection.class);

    private static final int DEFAULT_CHUNK_SIZE = 8192;

    private final Socket socket;

    private final TransportWriter<TransportMessage> writer;
    private final TransportReader<TransportMessageContainer> reader;
    
    private final long maxMessageLength;
    private final boolean selfInitiated;

    private OutputStream out;
    private InputStream in;

    private long messageIdentifier;

    private long rxCounter;
    private long txCounter;

    /**
     * Creates a SocketTransportConnection with corresponding transport
     * attributes and serializer.
     *
     * @param selfInitiated true if connection was initiated
     * by this side and false if not
     * @param server true if this is the server and false if not
     * @param socket the underlying socket
     * @param attributes the transport connection attributes
     * @param writer the protocol reader
     * @param reader the protocol writer
     * @param runner the connection thread executor
     * @throws ConnectionException if socket is not accessible
     */
    protected AbstractSocketTransportConnection(final long maxMessageLength, final boolean selfInitiated, final Socket socket,
            final TransportWriter<TransportMessage> writer,
            final TransportReader<TransportMessageContainer> reader){
        
        this.maxMessageLength = this.maxMessageLength >= 0 ? maxMessageLength :
            HSBConstants.HSB_TRSPT_MAX_MESSAGE_SIZE_UNKNOWN;
        this.selfInitiated = selfInitiated;
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
        this.messageIdentifier = 0;
    }


    protected boolean isOpen() {
        if (socket != null && this.in != null && this.out != null) {
            if (!socket.isClosed() && !socket.isInputShutdown()
                    && !socket.isOutputShutdown()) {
                return true;
            }
        }
        return false;
    }

    protected boolean isSelfInititated() {
        return selfInitiated;
    }

    protected void initialize() throws ConnectionException{
        try{
            this.in = new BufferedInputStream(socket.getInputStream());
            this.out = new BufferedOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new ConnectionException("Socket stream is not accessible.", e);
        }
        
    }
    
    protected void close() {
        try {

            this.socket.shutdownInput();

        } catch (IOException | UnsupportedOperationException e) {

            LOGGER.warn("Socket InputStream could not be closed.", e);

        } finally {

            try {
                this.socket.close();
            } catch (IOException e) {
                LOGGER.warn("Socket could not be closed.", e);
            }

        }
    }

    /**
     * Returns a linear growing identifier for a transport message.
     * If the identifier reaches a value >= 4294967295 it restarts at zero.
     *
     * @return the growing identifier
     */
     protected long getIdentifier() {
        if (this.messageIdentifier
                < HSBConstants.TCG_MAX_MESSAGE_IDENTIFIER) {
            return messageIdentifier++;
        } else {
            this.messageIdentifier = 0;
            return this.messageIdentifier;
        }
    }

    protected long getRxCounter(){
       return this.rxCounter;
    }
    
    protected long getTxCounter(){
        return this.txCounter;
     }
     
    /**
     * Initializes the transport connection by going thru the following phases.
     * <ul>
     * <li>negotiation phase</li>
     * <li>authentication phase (optional)</li>
     * <li>transport phase (final)</li>
     * </ul>
     *
     * @throws SerializationException if an error occurred at message
     * serialization
     * @throws ValidationException if an error occurred at message parsing
     * @throws ConnectionException if the connection is broken
     */
    protected void resetCounters(){

        // reset rx/tx counter to measure only the integrity transports
        long roundTrips = Math.max(this.rxCounter, this.txCounter);
        LOGGER.debug("Counters will be reset, current counts ( Messages received:"
                + rxCounter + ", Messages send:" + txCounter + ", Rounds:"
                + roundTrips + ")");
        this.rxCounter = 0;
        this.txCounter = 0;

    }

    /**
     * Write a transport message to an output stream
     * using a writer for serialization.
     *
     * @param message the transport message
     * @throws SerializationException if an error occurred at message
     * serialization
     * @throws ConnectionException if the connection is broken
     */
    protected void writeToStream(final TransportMessage message)
            throws ConnectionException, SerializationException {

        this.checkMessageLength(message.getHeader().getLength());

        ByteBuffer buf = new DefaultByteBuffer(message.getHeader().getLength());
        this.writer.write(message, buf);

        if (buf != null && buf.bytesWritten() > buf.bytesRead()) {
            if (isOpen()) {
                try {

                    while (buf.bytesWritten() > buf.bytesRead()) {
                        if ((buf.bytesWritten() - buf.bytesRead())
                                > DEFAULT_CHUNK_SIZE) {

                            this.out.write(buf.read(DEFAULT_CHUNK_SIZE));

                        } else {

                            this.out.write(buf.read((int)
                                    (buf.bytesWritten() - buf.bytesRead())));
                        }
                    }
                    
                    if(LOGGER.isDebugEnabled()){
                        LOGGER.debug("Message bytes written: " + buf.bytesRead());
                    }
                    
                    this.out.flush();
                    this.txCounter++;
                    
                    if(LOGGER.isDebugEnabled()){
                        if (message != null){
                            
                            LOGGER.debug("Message send: "
                                + message.toString());
                        }
                    }
                    
                } catch (IOException e) {
                    throw new ConnectionException(
                            "Data could not be written to stream.", e);
                } finally {
                    buf.clear();
                }
            } else {
                buf.clear();
                throw new ConnectionException("Connection seems not open.");
            }
        }
        buf.clear();

    }

    /**
     * Reads a transport message from an input stream
     * using a reader for parsing.
     *
     * @return the container containing a transport message
     * and minor parsing exceptions if any
     * @throws SerializationException if an error occurred at message
     * serialization
     * @throws ValidationException if an error occurred at message parsing
     * @throws ConnectionException if the connection is broken
     */
    protected TransportMessageContainer readFromStream()
            throws SerializationException, ValidationException,
            ConnectionException {
        if (isOpen()) {
            try {

                ByteBuffer buf = new StreamedReadOnlyByteBuffer(
                        socket.getInputStream());
             
                TransportMessageContainer ct = this.reader.read(buf, -1);
                if(LOGGER.isDebugEnabled()){
                    LOGGER.debug("Message bytes read: " + buf.bytesRead());
                }
                buf.clear();

                this.rxCounter++;

                if(LOGGER.isDebugEnabled()){
                    if (ct != null
                            && ct.getResult() != null){
                        LOGGER.debug("Message received: "
                            + ct.getResult().toString());
                    }
                }
                
                return ct;

            } catch (IOException e) {
                throw new ConnectionException(
                        "Socket InputStream is not accessible.", e);
            }
        }

        throw new ConnectionException("The socket seems not open.");
    }

    /**
     * Checks the message length according to the maximum
     * full message length attribute.
     *
     * @param length the length of the current message
     * @throws SerializationException if message is to large
     */
    private void checkMessageLength(final long length)
            throws SerializationException {
        if (this.maxMessageLength
                != HSBConstants.HSB_TRSPT_MAX_MESSAGE_SIZE_UNKNOWN
                && this.maxMessageLength
                != HSBConstants.HSB_TRSPT_MAX_MESSAGE_SIZE_UNLIMITED) {

            if (length > this.maxMessageLength) {

                throw new SerializationException("Message is to large.", true,
                        length, this.maxMessageLength);

            }
        }
    }
    
    /**
     * Creates a transport message containing a validation exception.
     *
     * @param exception the validation exception
     * @return the transport message
     * @throws ValidationException if message creation fails
     */
    protected TransportMessage createValidationErrorMessage(
            final ValidationException exception) throws ValidationException {

        if (exception.getReasons() != null
                || exception.getReasons().size() >= 0) {

            Object firstReason = exception.getReasons().get(0);
            if (firstReason instanceof byte[]) {
                return PtTlsMessageFactoryIetf.createError(
                        this.getIdentifier(), IETFConstants.IETF_PEN_VENDORID,
                        exception.getCause().getErrorCode(),
                        (byte[]) firstReason);
            }

            if (firstReason instanceof PtTlsMessageHeader) {
                return PtTlsMessageFactoryIetf.createError(
                        this.getIdentifier(), IETFConstants.IETF_PEN_VENDORID,
                        exception.getCause().getErrorCode(),
                        (PtTlsMessageHeader) firstReason);
            }

        }

        return PtTlsMessageFactoryIetf.createError(this.getIdentifier(),
                IETFConstants.IETF_PEN_VENDORID,
                exception.getCause().getErrorCode(),
                new byte[0]);

    }
}
