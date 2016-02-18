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
import java.util.concurrent.ExecutorService;

import org.ietf.nea.pt.message.PtTlsMessageFactoryIetf;
import org.ietf.nea.pt.message.PtTlsMessageHeader;
import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.value.PtTlsMessageValuePbBatch;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslAuthenticationData;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanismSelection;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanisms;
import org.ietf.nea.pt.value.PtTlsMessageValueVersionRequest;
import org.ietf.nea.pt.value.PtTlsMessageValueVersionResponse;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;
import org.ietf.nea.pt.value.util.SaslMechanismEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.message.t.serialize.TransportMessageContainer;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportReader;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.message.util.DefaultByteBuffer;
import de.hsbremen.tc.tnc.message.util.StreamedReadOnlyByteBuffer;
import de.hsbremen.tc.tnc.transport.TransportListener;
import de.hsbremen.tc.tnc.transport.TransportAttributes;
import de.hsbremen.tc.tnc.transport.TransportConnection;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;
import de.hsbremen.tc.tnc.transport.exception.ListenerClosedException;

/**
 * Transport connection with an underlying Socket.
 *
 */
public class SocketTransportConnectionSasl implements TransportConnection {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(SocketTransportConnectionSasl.class);

    private static final int DEFAULT_CHUNK_SIZE = 8192;
    private static final short MIN_VERSION = 1;
    private static final short MAX_VERSION = 1;
    private static final short PREF_VERSION = 1;

    private final Socket socket;
    private final boolean selfInitiated;
    private final boolean server;
    private final TransportAttributes attributes;

    private final ExecutorService runner;

    private final TransportWriter<TransportMessage> writer;
    private final TransportReader<TransportMessageContainer> reader;
    
    private final SaslHandler sasl;
    
    private OutputStream out;
    private InputStream in;

    private TransportListener listener;

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
     * @param sasl the handler to handle SASL authentication
     */
    public SocketTransportConnectionSasl(final boolean selfInitiated,
            final boolean server, final Socket socket,
            final TransportAttributes attributes,
            final TransportWriter<TransportMessage> writer,
            final TransportReader<TransportMessageContainer> reader,
            final ExecutorService runner,
            final SaslHandler sasl) {
        this.socket = socket;
        this.selfInitiated = selfInitiated;
        this.server = server;
        this.attributes = attributes;
        this.reader = reader;
        this.writer = writer;
        this.runner = runner;
        this.sasl = sasl;
        this.messageIdentifier = 0;
    }

    @Override
    public boolean isSelfInititated() {
        return selfInitiated;
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
    public void open(final TransportListener listener) throws ConnectionException {
        LOGGER.debug("Open transport connection.");

        try {
            this.in = new BufferedInputStream(socket.getInputStream());
            this.out = new BufferedOutputStream(socket.getOutputStream());
            this.listener = listener;

            try {

                this.initialize();

            } catch (SerializationException | ConnectionException e) {
                LOGGER.error(
                        "Error occured, while initializing the connection. "
                        + "Connection will be closed.", e);

                this.close();

                throw new ConnectionException("Fatal exception has occured "
                        + "and connection was closed.", e);

            } catch (ValidationException e) {

                LOGGER.error(
                        "Error occured, while initializing the connection. "
                        + "Will try to close connection gracefully.", e);

                try {
                    // try to close gracefully
                    TransportMessage m = this.createValidationErrorMessage(e);

                    this.writeToStream(m);

                } catch (ValidationException | SerializationException e1) {

                    LOGGER.error(
                            "Gracefull close was not successfull. "
                            + "Connection will be closed.", e);

                    this.close();

                    throw new ConnectionException("Fatal exception has occured "
                            + "and connection was closed.", e);

                }

            }

        } catch (IOException e) {
            throw new ConnectionException(
                    "Socket stream is not accessible.", e);
        }
    }

    @Override
    public void close() {
        LOGGER.debug("Connection close() called. Closing connection...");

        runner.shutdown();

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

        runner.shutdownNow();

        this.listener.notifyClose();
    }

    @Override
    public void send(final ByteBuffer buf) throws ConnectionException {
        if (buf != null && buf.bytesWritten() > buf.bytesRead()) {
            if (isOpen()) {
                try {

                    TransportMessage m = PtTlsMessageFactoryIetf.createPbBatch(
                            this.getIdentifier(), buf);
                    this.writeToStream(m);
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
    public Attributed getAttributes() {
        return this.attributes;
    }

    /**
     * Returns a linear growing identifier for a transport message.
     * If the identifier reaches a value >= 4294967295 it restarts at zero.
     *
     * @return the growing identifier
     */
    private long getIdentifier() {
        if (this.messageIdentifier
                < HSBConstants.TCG_MAX_MESSAGE_IDENTIFIER) {
            return messageIdentifier++;
        } else {
            this.messageIdentifier = 0;
            return this.messageIdentifier;
        }
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
    private void initialize() throws SerializationException,
            ValidationException, ConnectionException {
        this.negotiateVersion();
        this.makeAuthentication();

        // reset rx/tx counter to measure only the integrity transports
        long roundTrips = Math.max(this.rxCounter, this.txCounter);
        LOGGER.debug("Negotiation phase completed ( Messages received:"
                + rxCounter + ", Messages send:" + txCounter + ", Rounds:"
                + roundTrips + ")");
        this.rxCounter = 0;
        this.txCounter = 0;

        // start transport phase
        this.runner.execute(new TransportPhase());

    }

    /**
     * Handles the transport protocol version negotiation phase
     * with a remote peer.
     *
     * @throws SerializationException if an error occurred at message
     * serialization
     * @throws ValidationException if an error occurred at message parsing
     * @throws ConnectionException if the connection is broken
     */
    private void negotiateVersion() throws SerializationException,
            ValidationException, ConnectionException {
        if (this.isSelfInititated()) {
            TransportMessage m = PtTlsMessageFactoryIetf.createVersionRequest(
                    this.getIdentifier(), MIN_VERSION, MAX_VERSION,
                    PREF_VERSION);
            this.writeToStream(m);

            TransportMessageContainer ct = null;
            while (ct == null) {
                try {

                    ct = this.readFromStream();

                    if (ct != null
                            && ct.getResult() != null
                            && !(ct.getResult().getValue()
                                instanceof PtTlsMessageValueVersionResponse)) {

                        throw new ValidationException(
                                "Unexpected message received.",
                                new RuleException(
                                        "Message of type "
                                                + ct.getResult().getValue()
                                                        .getClass()
                                                        .getCanonicalName()
                                                + " was not expected.",
                                        true,
                                        PtTlsMessageErrorCodeEnum.
                                        IETF_INVALID_MESSAGE.code(),
                                        PtTlsErrorCauseEnum.
                                        MESSAGE_TYPE_UNEXPECTED.id()),
                                        0, ct.getResult().getHeader());
                    }

                } catch (ValidationException e) {
                    if (e.getCause().isFatal()) {

                        throw e;

                    } else {

                        ct = null;
                        LOGGER.debug("Minor error occured. "
                                + "An error message will be send.");
                        m = this.createValidationErrorMessage(e);
                        this.writeToStream(m);
                    }
                }
            }

            if (!(PREF_VERSION >= ((PtTlsMessageValueVersionResponse)
                    ct.getResult().getValue()).getSelectedVersion()
                    && PREF_VERSION >= ((PtTlsMessageValueVersionResponse)
                    ct.getResult().getValue()).getSelectedVersion())) {

                throw new ValidationException(
                        "Version not supported.",
                        new RuleException(
                                "Version "
                                        + ((PtTlsMessageValueVersionResponse) ct
                                                .getResult().getValue())
                                                .getSelectedVersion()
                                        + " not in supported version range.",
                                true,
                                PtTlsMessageErrorCodeEnum.
                                IETF_UNSUPPORTED_VERSION.code(),
                                PtTlsErrorCauseEnum.TRANSPORT_VERSION_NOT_SUPPORTED.id()),
                                0, ct.getResult().getHeader());
            }

        } else {

            TransportMessageContainer ct = null;

            while (ct == null) {
                try {

                    ct = this.readFromStream();

                    if (ct != null
                            && ct.getResult() != null
                            && !(ct.getResult().getValue()
                                  instanceof PtTlsMessageValueVersionRequest)) {

                        throw new ValidationException(
                                "Unexpected message received.",
                                new RuleException(
                                        "Message of type "
                                                + ct.getResult().getValue()
                                                        .getClass()
                                                        .getCanonicalName()
                                                + " was not expected.",
                                        true,
                                        PtTlsMessageErrorCodeEnum.
                                        IETF_INVALID_MESSAGE.code(),
                                        PtTlsErrorCauseEnum.
                                        MESSAGE_TYPE_UNEXPECTED.id()),
                                        0, ct.getResult().getHeader());
                    }

                } catch (ValidationException e) {
                    if (e.getCause().isFatal()) {

                        throw e;

                    } else {

                        LOGGER.debug("Minor error occured."
                                + " An error message will be send.");
                        ct = null;
                        TransportMessage m = this
                                .createValidationErrorMessage(e);
                        this.writeToStream(m);
                    }
                }
            }

            if (PREF_VERSION >= ((PtTlsMessageValueVersionRequest) ct
                    .getResult().getValue()).getMinVersion()
                    && PREF_VERSION >= ((PtTlsMessageValueVersionRequest) ct
                            .getResult().getValue()).getMaxVersion()) {

                TransportMessage m = PtTlsMessageFactoryIetf
                        .createVersionResponse(this.getIdentifier(),
                                PREF_VERSION);

                this.writeToStream(m);

            } else {

                throw new ValidationException(
                        "Version not supported.",
                        new RuleException(
                                "Version " + PREF_VERSION
                                        + " not in supported version range.",
                                true,
                                PtTlsMessageErrorCodeEnum.
                                IETF_UNSUPPORTED_VERSION.code(),
                                PtTlsErrorCauseEnum.TRANSPORT_VERSION_NOT_SUPPORTED.id()),
                                0, ct.getResult().getHeader());
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
    private TransportMessage createValidationErrorMessage(
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

    /**
     * Handles the authentication phase with a remote peer.
     * This implementation does not support additional authentication.
     * If the remote peer requests additional authentication,
     * a transport message containing an error is send.
     *
     * @throws SerializationException if an error occurred at message
     * serialization
     * @throws ValidationException if an error occurred at message parsing
     * @throws ConnectionException if the connection is broken
     */
    private void makeAuthentication() throws ValidationException,
            ConnectionException, SerializationException {
        if (this.server) {

            TransportMessage m = PtTlsMessageFactoryIetf
                    .createSaslMechanisms(this.getIdentifier(), this.sasl.getSupportedMechanisms().toArray(new SaslMechanismEntry[0]));

            this.writeToStream(m);

        }
        
        if (!this.server || !this.sasl.getSupportedMechanisms().isEmpty()){

           
            while (!this.sasl.isFinished()) {
                
                TransportMessageContainer ct = null;
                
                while(ct != null){
                try {
                    ct = this.readFromStream();

                    if (ct != null
                            && ct.getResult() != null
                            && !(ct.getResult().getValue()
                                 instanceof PtTlsMessageValueSaslMechanisms)
                            && !(ct.getResult().getValue()
                                 instanceof PtTlsMessageValueSaslMechanismSelection
                            && !(ct.getResult().getValue()
                                 instanceof PtTlsMessageValueSaslAuthenticationData
                            && !(ct.getResult().getValue()
                                 instanceof PtTlsMessageValueSasl)))) {

                        throw new ValidationException(
                                "Unexpected message received.",
                                new RuleException(
                                        "Message of type "
                                                + ct.getResult().getValue()
                                                        .getClass()
                                                        .getCanonicalName()
                                                + " was not expected.",
                                        true,
                                        PtTlsMessageErrorCodeEnum.
                                        IETF_INVALID_MESSAGE.code(),
                                        PtTlsErrorCauseEnum.
                                        MESSAGE_TYPE_UNEXPECTED.id()),
                                        0, ct.getResult().getHeader());
                    }

                } catch (ValidationException e) {
                    if (e.getCause().isFatal()) {

                        throw e;

                    } else {

                        LOGGER.debug("Minor error occured. "
                                + "An error message will be send.");

                        ct = null;
                        TransportMessage m = this
                                .createValidationErrorMessage(e);

                        this.writeToStream(m);
                    }
                }
            }

            if (((PtTlsMessageValueSaslMechanisms) ct.getResult().getValue())
                    .getMechanisms().size() > 0) {
                //TODO add SASL connector here to enable SASL support
                throw new ValidationException(
                        "Extra SASL authentication not supported.",
                        new RuleException(
                                "Extra SASL authentication not supported.",
                                true,
                                PtTlsMessageErrorCodeEnum.
                                IETF_SASL_MECHANISM_ERROR.code(),
                                PtTlsErrorCauseEnum.
                                ADDITIONAL_SASL_NOT_SUPPORTED.id()),
                                0, ct.getResult().getHeader());
            }
        }
        }
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
    private void writeToStream(final TransportMessage message)
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
    private TransportMessageContainer readFromStream()
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
        if (this.attributes.getMaxMessageLength()
                != HSBConstants.HSB_TRSPT_MAX_MESSAGE_SIZE_UNKNOWN
                && this.attributes.getMaxMessageLength()
                != HSBConstants.HSB_TRSPT_MAX_MESSAGE_SIZE_UNLIMITED) {

            if (length > this.attributes.getMaxMessageLength()) {

                throw new SerializationException("Message is to large.", true,
                        length, this.attributes.getMaxMessageLength());

            }
        }

    }

    /**
     * Checks the round trip counter if the maximum round trips are exceeded.
     */
    private void checkRoundTrips() {
        long maxRoundTrips = this.attributes.getMaxRoundTrips();
        if (HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNKNOWN < maxRoundTrips
                && maxRoundTrips
                < HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNLIMITED) {

            long roundTrips = Math.min(this.rxCounter, this.txCounter);

            if (roundTrips >= maxRoundTrips) {
                LOGGER.debug("Round trip limit exceeded: ( Messages received:"
                        + rxCounter + ", Messages send:" + txCounter
                        + ", Rounds:" + roundTrips + ")");

                this.close();
            }
        }
    }

    /**
     * Runnable which handles the transport phase with a remote peer.
     * It listens for incoming messages.
     *
     *
     */
    private class TransportPhase implements Runnable {
        @Override
        public void run() {
            TransportMessageContainer ct = null;

            try {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        ct = readFromStream();
                        
                        if (ct != null && ct.getResult() != null) {
                            if (ct.getResult().getValue()
                                    instanceof PtTlsMessageValuePbBatch) {

                                listener.receive(((PtTlsMessageValuePbBatch)
                                        ct.getResult().getValue())
                                        .getTnccsData());

                            } else {

                                throw new ValidationException(
                                        "Unexpected message received.",
                                        new RuleException(
                                                "Message of type "
                                                        + ct.getResult()
                                                             .getValue()
                                                             .getClass()
                                                             .getCanonicalName()
                                                        + " was not expected.",
                                                true,
                                                PtTlsMessageErrorCodeEnum
                                                .IETF_INVALID_MESSAGE.code(),
                                                PtTlsErrorCauseEnum
                                                .MESSAGE_TYPE_UNEXPECTED.id()),
                                                0, ct.getResult().getHeader());
                            }
                        }
                    } catch (ValidationException e) {

                        if (e.getCause().isFatal()) {

                            throw e;

                        } else {

                            LOGGER.debug("Minor error occured. "
                                    + "An error message will be send.");
                            ct = null;
                            TransportMessage m =
                                    createValidationErrorMessage(e);

                            writeToStream(m);

                        }
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
                    TransportMessage m = createValidationErrorMessage(e);

                    writeToStream(m);

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
