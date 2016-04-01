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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.ietf.nea.pt.message.PtTlsMessageFactoryIetf;
import org.ietf.nea.pt.message.PtTlsMessageHeader;
import org.ietf.nea.pt.socket.SocketHandler;
import org.ietf.nea.pt.socket.TransportMessenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportReader;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.message.util.DefaultByteBuffer;
import de.hsbremen.tc.tnc.message.util.StreamedReadOnlyByteBuffer;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

/**
 * Wrapper to encapsulate a socket and mask its functions
 * for the transmission and receipt of TNC messages thru the
 * contained socket and for the management of the contained socket.
 */
public class DefaultSocketWrapper implements TransportMessenger, SocketHandler {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultSocketWrapper.class);

    private static final int DEFAULT_CHUNK_SIZE = 8192;

    private final Socket socket;

    private final TransportWriter<TransportMessage> writer;
    private final TransportReader<TransportMessage> reader;
    
    private final long maxMessageLength;

    private OutputStream out;
    private InputStream in;

    private long messageIdentifier;

    private long rxCounter;
    private long txCounter;

    /**
     * Creates a wrapper around the given socket using the provided attributes.
     *
     * @param maxMessageLength the maximum supported message length of
     * the transport connection
     * @param socket the socket to wrap
     * @param writer the writer to encode a transport message
     * as byte stream for transmission
     * @param reader the reader to decode a byte stream
     * into a transport message after reception
     */
    public DefaultSocketWrapper(final long maxMessageLength,
            final Socket socket,
            final TransportWriter<TransportMessage> writer,
            final TransportReader<TransportMessage> reader) {
        
        this.maxMessageLength = this.maxMessageLength >= 0 ? maxMessageLength
                : HSBConstants.HSB_TRSPT_MAX_MESSAGE_SIZE_UNKNOWN;

        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
        this.messageIdentifier = 0;
    }


    @Override
    public boolean isOpen() {
        if (socket != null && this.in != null && this.out != null) {
            if (!socket.isClosed() && !socket.isInputShutdown()
                    && !socket.isOutputShutdown()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void initialize() throws ConnectionException {
        try {
            this.in = new BufferedInputStream(socket.getInputStream());
            this.out = new BufferedOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new ConnectionException(
                    "Socket stream is not accessible.", e);
        }
        
    }

    @Override
    public void close() {
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

    @Override
    public long getIdentifier() {
        if (this.messageIdentifier
                < HSBConstants.TCG_MAX_MESSAGE_IDENTIFIER) {
            return messageIdentifier++;
        } else {
            this.messageIdentifier = 0;
            return this.messageIdentifier;
        }
    }

    @Override
    public long getRxCounter() {
        return this.rxCounter;
    }
    
    @Override
    public long getTxCounter() {
        return this.txCounter;
    }
     
    @Override
    public void resetCounters() {

        // reset rx/tx counter to measure only the integrity transports
        long roundTrips = Math.max(this.rxCounter, this.txCounter);
        LOGGER.debug(
                "Counters will be reset, current counts ( Messages received:"
                + rxCounter + ", Messages send:" + txCounter + ", Rounds:"
                + roundTrips + ")");
        this.rxCounter = 0;
        this.txCounter = 0;

    }

    @Override
    public void writeToStream(final TransportMessage message)
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
                    
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Message bytes written: "
                                + buf.bytesRead());
                    }
                    
                    this.out.flush();
                    this.txCounter++;
                    
                    if (LOGGER.isDebugEnabled()) {
                        if (message != null) {
                            
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

    @Override
    public TransportMessage readFromStream()
            throws SerializationException, ValidationException,
            ConnectionException {
        if (isOpen()) {
            try {

                ByteBuffer buf = new StreamedReadOnlyByteBuffer(
                        socket.getInputStream());
             
                TransportMessage message = this.reader.read(buf, -1);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Message bytes read: " + buf.bytesRead());
                }
                buf.clear();

                this.rxCounter++;

                if (LOGGER.isDebugEnabled()) {
                    if (message != null) {
                        LOGGER.debug("Message received: "
                            + message.toString());
                    }
                }
                
                return message;

            } catch (IOException e) {
                throw new ConnectionException(
                        "Socket InputStream is not accessible.", e);
            }
        }

        throw new ConnectionException("The socket seems not open.");
    }

    /**
     * Checks the message length according to the {@link #maxMessageLength}
     * class member.
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

    @Override
    public TransportMessage createValidationErrorMessage(
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
