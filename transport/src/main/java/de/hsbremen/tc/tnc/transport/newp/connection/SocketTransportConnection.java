package de.hsbremen.tc.tnc.transport.newp.connection;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pt.message.PtTlsMessage;
import org.ietf.nea.pt.message.PtTlsMessageFactoryIetf;
import org.ietf.nea.pt.message.PtTlsMessageHeader;
import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.value.PtTlsMessageValuePbBatch;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanisms;
import org.ietf.nea.pt.value.PtTlsMessageValueVersionRequest;
import org.ietf.nea.pt.value.PtTlsMessageValueVersionResponse;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.message.t.serialize.TransportMessageContainer;
import de.hsbremen.tc.tnc.message.t.serialize.TransportReader;
import de.hsbremen.tc.tnc.message.t.serialize.TransportWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.message.util.DefaultByteBuffer;
import de.hsbremen.tc.tnc.message.util.StreamedReadOnlyBuffer;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class SocketTransportConnection implements TransportConnection{
	private static final Logger LOGGER = LoggerFactory.getLogger(SocketTransportConnection.class);
	
	private static final int DEFAULT_CHUNK_SIZE = 8192;
	private static final short MIN_VERSION = 1;
	private static final short MAX_VERSION = 1;
	private static final short PREF_VERSION = 1;
	
	private final Socket socket;
	private final TransportAddress address;
	private final boolean selfInitiated;
	private final boolean server;
	private final Attributed attributes;
	private final TransportWriter<TransportMessage> writer;
	private final TransportReader<TransportMessageContainer> reader;
	private final ExecutorService runner;
	private OutputStream out;
	private InputStream in;
	private TnccsValueListener listener;
	private long messageIdentifier;
	
	public SocketTransportConnection(boolean selfInitiated, boolean server, Socket socket, 
			Attributed attributes, TransportAddress address, 
			TransportWriter<TransportMessage> writer,
			TransportReader<TransportMessageContainer> reader,
			ExecutorService runner){
		this.socket = socket;
		this.selfInitiated = selfInitiated;
		this.server = server;
		this.attributes = attributes;
		this.address = address;
		this.reader = reader;
		this.writer = writer;
		this.runner = runner;
		this.messageIdentifier = 0;
	}
	
	@Override
	public TransportAddress getAddress() {
		return this.address;
	}

	@Override
	public boolean isSelfInititated() {
		return selfInitiated;
	}

	@Override
	public boolean isOpen() {
		if(socket != null && this.in != null && this.out != null){
			if(!socket.isClosed() && !socket.isInputShutdown() && !socket.isOutputShutdown()){
				return true;
			}
		}
		return false;
	}

	@Override
	public void open(TnccsValueListener listener) throws ConnectionException {
		LOGGER.debug("Open transport connection.");
		
		try{
			this.in = new BufferedInputStream(socket.getInputStream());
			this.out = new BufferedOutputStream(socket.getOutputStream());
			this.listener = listener;
			
			
			try {
				
				this.initialize();
				
			} catch (SerializationException | ConnectionException e) {
				LOGGER.error("Error occured, while initializing the connection. Connection will be closed.",e);
				this.close();
				throw new ConnectionException("Fatal exception has occured and connection was closed. " + e.getMessage());
			
			} catch(ValidationException e){
				LOGGER.error("Error occured, while initializing the connection. Will try to close connection gracefully.",e);
				try {
					// try to close gracefully
					TransportMessage m = this.createValidationErrorMessage(e);
					this.writeToStream(m);
				
				}catch (ValidationException | SerializationException e1) {
					LOGGER.error("Gracefull close was not successfull. Connection will be closed.",e);
					this.close();
					throw new ConnectionException("Fatal exception has occured and connection was closed. " + e1.getMessage());
					
				}
				
			}
			
			
		}catch(IOException e){
			throw new ConnectionException("Socket stream is not accessible.", e);
		}		
	}

	@Override
	public void close() {
		LOGGER.debug("Connection close() called. Closing connection...");
		runner.shutdown();
		try {
			this.socket.shutdownInput();
		} catch (IOException e) {
			LOGGER.warn("Socket InputStream could not be closed.", e);
		}finally{
			try {
				this.socket.close();
			} catch (IOException e) {
				LOGGER.warn("Socket could not be closed.", e);
			}
		}
		runner.shutdownNow();
	}
	
	@Override
	public void send(ByteBuffer buf) throws ConnectionException{
		if(buf != null && buf.bytesWritten() > buf.bytesRead()){
			if(isOpen()){
				try {
					
					TransportMessage m = PtTlsMessageFactoryIetf.createPbBatch(this.getIdentifier(), buf);
					this.writeToStream(m);

				} catch (SerializationException | ValidationException e) {
					throw new ConnectionException("Data could not be written to stream.", e);
				}
			}else{
				throw new ConnectionException("Connection seems not open.");
			}
		}
		
	}

	@Override
	public Attributed getAttributes() {
		return this.attributes;
	}

	private long getIdentifier(){
		if(this.messageIdentifier < 0xFFFFFFFFL){
			return messageIdentifier ++;
		}else{
			this.messageIdentifier = 0;
			return this.messageIdentifier;
		}
	}
	
	private void initialize() throws SerializationException, ValidationException, ConnectionException {
		this.negotiateVersion();
		this.makeAuthentication();
		this.runner.execute(new TransportPhase());
		
	}
	
	private void negotiateVersion() throws SerializationException, ValidationException, ConnectionException{
		if(this.isSelfInititated()){
			TransportMessage m = PtTlsMessageFactoryIetf.createVersionRequest(this.getIdentifier(),MIN_VERSION, MAX_VERSION, PREF_VERSION);
			this.writeToStream(m);
			
			TransportMessageContainer ct = null;
			while(ct == null){
				try{
					
					ct = this.readFromStream();
					LOGGER.debug("Message received.");
					
					if(ct != null && ct.getResult() != null && !(ct.getResult().getValue() instanceof PtTlsMessageValueVersionResponse)){
						throw new ValidationException("Unexpected message received.", 
								new RuleException("Message of type " +ct.getResult().getValue().getClass().getCanonicalName()+ " was not expected.", 
										true, PtTlsMessageErrorCodeEnum.IETF_INVALID_MESSAGE.code(),
										PtTlsErrorCauseEnum.MESSAGE_TYPE_UNEXPECTED.number()),
								0, ct.getResult().getHeader());
					}
					
				}catch(ValidationException e){
					if(e.getCause().isFatal()){
						throw e;
					}else{
						ct = null;
						LOGGER.debug("Minor error occured. An error message will be send.");
						m = this.createValidationErrorMessage(e);
						this.writeToStream(m);
					}
				}
			}
			
			if(!(PREF_VERSION >= ((PtTlsMessageValueVersionResponse)ct.getResult().getValue()).getSelectedVersion() 
					&& PREF_VERSION >= ((PtTlsMessageValueVersionResponse)ct.getResult().getValue()).getSelectedVersion())){
				
				throw new ValidationException("Version not supported.", 
						new RuleException("Version " +((PtTlsMessageValueVersionResponse)ct.getResult().getValue()).getSelectedVersion()+ " not in supported version range.", 
								true, PtTlsMessageErrorCodeEnum.IETF_UNSUPPORTED_VERSION.code(),
								PtTlsErrorCauseEnum.UNSUPPORTED_VERSION.number()),
						0, ct.getResult().getHeader());
			}	
			
		}else{
			
			TransportMessageContainer ct = null;
			
			while(ct == null){
				try{
					
					ct = this.readFromStream();
					LOGGER.debug("Message received.");
					if(ct != null && ct.getResult() != null && !(ct.getResult().getValue() instanceof PtTlsMessageValueVersionRequest)){
						throw new ValidationException("Unexpected message received.", 
								new RuleException("Message of type " +ct.getResult().getValue().getClass().getCanonicalName()+ " was not expected.", 
										true, PtTlsMessageErrorCodeEnum.IETF_INVALID_MESSAGE.code(),
										PtTlsErrorCauseEnum.MESSAGE_TYPE_UNEXPECTED.number()),
								0, ct.getResult().getHeader());
					}
					
				}catch(ValidationException e){
					if(e.getCause().isFatal()){
						throw e;
					}else{
						LOGGER.debug("Minor error occured. An error message will be send.");
						ct = null;
						TransportMessage m = this.createValidationErrorMessage(e);
						this.writeToStream(m);
					}
				}
			}
			
			if(PREF_VERSION >= ((PtTlsMessageValueVersionRequest)ct.getResult().getValue()).getMinVersion() 
					&& PREF_VERSION >= ((PtTlsMessageValueVersionRequest)ct.getResult().getValue()).getMaxVersion()){
				TransportMessage m = PtTlsMessageFactoryIetf.createVersionResponse(this.getIdentifier(),PREF_VERSION);
				this.writeToStream(m);
			}else{
				throw new ValidationException("Version not supported.", 
						new RuleException("Version " +PREF_VERSION+ " not in supported version range.", 
								true, PtTlsMessageErrorCodeEnum.IETF_UNSUPPORTED_VERSION.code(),
								PtTlsErrorCauseEnum.UNSUPPORTED_VERSION.number()),
						0, ct.getResult().getHeader());
			}
			
		}
	}
	
	private PtTlsMessage createValidationErrorMessage(ValidationException e) throws ValidationException {
		
		if(e.getReasons() != null || e.getReasons().size() >= 0){
		
			Object firstReason = e.getReasons().get(0);
			if(firstReason instanceof byte[]){
				return PtTlsMessageFactoryIetf.createError(this.getIdentifier(), IETFConstants.IETF_PEN_VENDORID, e.getCause().getErrorCode(),(byte[]) firstReason );
			}
			
			if(firstReason instanceof PtTlsMessageHeader){
				return PtTlsMessageFactoryIetf.createError(this.getIdentifier(), IETFConstants.IETF_PEN_VENDORID, e.getCause().getErrorCode(),(PtTlsMessageHeader) firstReason );	
			}
		
		}
		
		return PtTlsMessageFactoryIetf.createError(this.getIdentifier(), IETFConstants.IETF_PEN_VENDORID, e.getCause().getErrorCode(),new byte[0]);
		
	}

	private void makeAuthentication() throws ValidationException, ConnectionException, SerializationException {
		if(this.server){
			// empty for no extra authentication wanted and move on to transport phase
			TransportMessage m = PtTlsMessageFactoryIetf.createSaslMechanisms(this.getIdentifier());
			this.writeToStream(m);	
		}else{
		
			TransportMessageContainer ct = null;
			while(ct == null){
				try{
					ct = this.readFromStream();
					LOGGER.debug("Message received.");
					if(ct != null && ct.getResult() != null && !(ct.getResult().getValue() instanceof PtTlsMessageValueSaslMechanisms)){
					
						throw new ValidationException("Unexpected message received.", 
								new RuleException("Message of type " +ct.getResult().getValue().getClass().getCanonicalName()+ " was not expected.", 
										true, PtTlsMessageErrorCodeEnum.IETF_INVALID_MESSAGE.code(),
										PtTlsErrorCauseEnum.MESSAGE_TYPE_UNEXPECTED.number()),
								0, ct.getResult().getHeader());
					}
				
				}catch(ValidationException e){
					if(e.getCause().isFatal()){
						throw e;
					}else{
						LOGGER.debug("Minor error occured. An error message will be send.");
						ct = null;
						TransportMessage m = this.createValidationErrorMessage(e);
						this.writeToStream(m);
					}
				}
			}
			
			if(((PtTlsMessageValueSaslMechanisms)ct.getResult().getValue()).getMechanisms().size() > 0){
				throw new ValidationException("Extra SASL authentication not supported.", 
						new RuleException("Extra SASL authentication not supported.", true, PtTlsMessageErrorCodeEnum.IETF_SASL_MECHANISM_ERROR.code(), PtTlsErrorCauseEnum.ADDITIONAL_SASL_NOT_SUPPORTED.number()),
						0, ct.getResult().getHeader());
			}
		}
	}

	private void writeToStream(TransportMessage m) throws ConnectionException, SerializationException{
		ByteBuffer buf = new DefaultByteBuffer(((PtTlsMessage)m).getHeader().getLength());
		this.writer.write(m, buf);
		
		if(buf != null && buf.bytesWritten() > buf.bytesRead()){
			if(isOpen()){
				try {
					
					while(buf.bytesWritten() > buf.bytesRead()){
						if((buf.bytesWritten() - buf.bytesRead()) > DEFAULT_CHUNK_SIZE){
							this.out.write(buf.read(DEFAULT_CHUNK_SIZE));
						}else{
							this.out.write(buf.read((int)(buf.bytesWritten() - buf.bytesRead())));
						}
					}
					
					this.out.flush();

				} catch (IOException e) {
					throw new ConnectionException("Data could not be written to stream.", e);
				}finally{
					buf.clear();
				}
			}else{
				buf.clear();
				throw new ConnectionException("Connection seems not open.");
			}
		}
		buf.clear();
		
	}
	
	private TransportMessageContainer readFromStream() throws SerializationException, ValidationException, ConnectionException{
		if(isOpen()){
			try{
				
				ByteBuffer b = new StreamedReadOnlyBuffer(socket.getInputStream());
				TransportMessageContainer ct = this.reader.read(b, -1);
				b.clear();
				return ct;
				
			} catch (IOException e) {
				throw new ConnectionException("Socket InputStream is not accessible.", e);
			}
		}
		
		throw new ConnectionException("The socket seems not open.");
	}
	
	private class TransportPhase implements Runnable {
		@Override
		public void run() {
			TransportMessageContainer ct = null;
			
			try{
				while(true){
					try{
						ct = readFromStream();
						LOGGER.debug("Message received.");
						if(ct != null && ct.getResult() != null){
							if(ct.getResult().getValue() instanceof PtTlsMessageValuePbBatch){
								
								listener.receive(((PtTlsMessageValuePbBatch)ct.getResult().getValue()).getTnccsData());
							
							}else{
								
								throw new ValidationException("Unexpected message received.", 
										new RuleException("Message of type " +ct.getResult().getValue().getClass().getCanonicalName()+ " was not expected.", 
												true, PtTlsMessageErrorCodeEnum.IETF_INVALID_MESSAGE.code(),
												PtTlsErrorCauseEnum.MESSAGE_TYPE_UNEXPECTED.number()),
										0, ct.getResult().getHeader());
							}
						}
					}catch(ValidationException e){
						if(e.getCause().isFatal()){
							throw e;
						}else{
							LOGGER.debug("Minor error occured. An error message will be send.");
							ct = null;
							TransportMessage m = createValidationErrorMessage(e);
							writeToStream(m);
						}
					}
				}
			} catch (SerializationException | ConnectionException | ListenerClosedException e) {
				
				// ignore and just close

			} catch(ValidationException e){
				
				try {
					// try to close gracefully
					TransportMessage m = createValidationErrorMessage(e);
					writeToStream(m);
				
				}catch (ValidationException | ConnectionException |SerializationException e1) {
					
					// ignore and just close
					
				}
				
			}finally{
				close();
			
			}
				
		}
		
	}
	
}
