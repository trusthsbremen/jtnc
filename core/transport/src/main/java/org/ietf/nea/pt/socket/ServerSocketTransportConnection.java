package org.ietf.nea.pt.socket;

import java.net.Socket;
import java.util.concurrent.ExecutorService;

import org.ietf.nea.pt.message.PtTlsMessageFactoryIetf;
import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.value.PtTlsMessageValuePbBatch;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.message.t.serialize.TransportMessageContainer;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportReader;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.transport.TransportAttributes;
import de.hsbremen.tc.tnc.transport.TransportConnection;
import de.hsbremen.tc.tnc.transport.TransportConnectionPhase;
import de.hsbremen.tc.tnc.transport.TransportListener;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;
import de.hsbremen.tc.tnc.transport.exception.ListenerClosedException;

public class ServerSocketTransportConnection extends
        AbstractSocketTransportConnection implements TransportConnection {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ServerSocketTransportConnection.class);
    
    private final TransportAttributes attributes;

    private final ExecutorService runner;

    private TransportListener listener;
    
    private TransportConnectionPhase phase;

    
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
     * @throws ConnectionException 
     */
     ServerSocketTransportConnection(final boolean selfInitiated, final Socket socket,
            final TransportAttributes attributes,
            final TransportWriter<TransportMessage> writer,
            final TransportReader<TransportMessageContainer> reader,
            final ExecutorService runner){
     
        super(attributes.getMaxMessageLength(), selfInitiated, socket, writer, reader);

        this.attributes = attributes;
        this.runner = runner;
        
        this.phase = SocketTransportConnectionPhaseEnum
                .TRSPT_CONNECTION_PHASE_PENDING;
    }

    @Override
    public boolean isSelfInititated() {
        return super.isSelfInititated();
    }

    @Override
    public boolean isOpen() {
       return super.isOpen();
    }
    
    public TransportConnectionPhase getPhase(){
        return this.phase;
    }

    public void initialize(VersionNegotiator negotiator, Authenticator authenticator) throws ConnectionException{
        if(this.phase.equals(SocketTransportConnectionPhaseEnum
                .TRSPT_CONNECTION_PHASE_PENDING)){
            
            super.initialize();
            
            try{
                
                this.phase = SocketTransportConnectionPhaseEnum
                        .TRSPT_CONNECTION_PHASE_NEGOTIATE_VERSION;
                
                negotiator.negotiate(this);
                
                this.phase = SocketTransportConnectionPhaseEnum
                        .TRSPT_CONNECTION_PHASE_AUTHENTICATE;
        
                authenticator.authenticate(this);
                
                this.phase = SocketTransportConnectionPhaseEnum
                        .TRSPT_CONNECTION_PHASE_TRANSPORT;
                
                
            } catch (SerializationException | ConnectionException e) {
                LOGGER.error(
                        "Error occured in phase "
                        + this.phase.toString()
                        + " , while initializing the connection. "
                        + "Connection will be closed.", e);
    
                this.close();
    
                throw new ConnectionException("Fatal exception has occured "
                        + "in phase " + this.phase.toString()
                        + " and connection was closed.", e);
    
            } catch (ValidationException e) {
    
                LOGGER.error("Error occured in phase "
                         + this.phase.toString()
                         + " , while initializing the connection. "
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
                            + "in phase " + this.phase.toString()
                            + "and connection was closed.", e);
    
                }
            }
        }else{
            if(this.phase.equals(SocketTransportConnectionPhaseEnum.TRSPT_CONNECTION_PHASE_TRANSPORT)){
                LOGGER.warn(
                        "Initialize was already called. "
                        + "Connection is in phase " + this.phase.toString()
                        + " Ignoring this call.");
            }else{
                
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
    }
    
    @Override
    public void open(TransportListener listener) throws ConnectionException {
       if(!this.phase.equals(SocketTransportConnectionPhaseEnum
               .TRSPT_CONNECTION_PHASE_TRANSPORT)){
           
           throw new ConnectionException(
                   new StringBuilder("Connection is not ready. Connection is in ")
                   .append("phase ").append(this.phase.toString())
                   .append(". It has to be in phase ")
                   .append(SocketTransportConnectionPhaseEnum
                           .TRSPT_CONNECTION_PHASE_TRANSPORT.toString())
                   .append(" to receive messages for the upper layers. ")
                   .append("Initialize must be called first.")
                   .toString(), this.phase);
       }
       
       this.listener = listener;
       
       // reset rx/tx counter to measure only the integrity transports
       long roundTrips = Math.max(this.getRxCounter(), this.getTxCounter());
       LOGGER.debug("Negotiation phase completed ( Messages received:"
               + this.getRxCounter() + ", Messages send:" + this.getTxCounter() + ", Rounds:"
               + roundTrips + ")");
       this.resetCounters();

       // start transport phase
       this.runner.execute(new TransportPhase());

    }

    @Override
    public void close() {
        LOGGER.debug("Connection close() called. Closing connection...");

        runner.shutdown();
        super.close();
        runner.shutdownNow();

        this.listener.notifyClose();
    }
    
    @Override
    public void send(ByteBuffer buffer) throws ConnectionException {
        if (buffer != null && buffer.bytesWritten() > buffer.bytesRead()) {
            if (isOpen()) {
                try {

                    TransportMessage m = PtTlsMessageFactoryIetf.createPbBatch(
                            this.getIdentifier(), buffer);
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
     * Checks the round trip counter if the maximum round trips are exceeded.
     */
    private void checkRoundTrips() {
        long maxRoundTrips = this.attributes.getMaxRoundTrips();
        if (HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNKNOWN < maxRoundTrips
                && maxRoundTrips
                < HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNLIMITED) {

            long roundTrips = Math.min(super.getRxCounter(), super.getTxCounter());

            if (roundTrips >= maxRoundTrips) {
                LOGGER.debug("Round trip limit exceeded: ( Messages received:"
                        + super.getRxCounter() + ", Messages send:" + super.getTxCounter()
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
