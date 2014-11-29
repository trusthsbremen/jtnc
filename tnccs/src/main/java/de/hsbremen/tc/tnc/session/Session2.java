package de.hsbremen.tc.tnc.session;

import java.io.IOException;
import java.rmi.ConnectException;
import java.util.Deque;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.HandlingException;
import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.newp.handler.ImcHandler;
import de.hsbremen.tc.tnc.newp.handler.StateChangePermit;
import de.hsbremen.tc.tnc.newp.handler.TnccHandler;
import de.hsbremen.tc.tnc.newp.handler.TnccsSessionContext;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.session.context.SessionConnectionContext;
import de.hsbremen.tc.tnc.session.state.SessionState;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsReader;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsWriter;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;
import de.hsbremen.tc.tnc.transport.exception.ConnectionAttributeNotFoundException;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class Session2 implements SessionStateContext, SessionConnectionContext, TnccsSessionContext, StateChangePermit{

	protected static final Logger LOGGER = LoggerFactory.getLogger(Session2.class);
	
	private final TransportConnection connection;
	private final TnccsWriter<? super TnccsBatch> writer;
	private final TnccsReader<? extends TnccsBatchContainer> reader;
	private final Deque<TnccsMessage> messageQueue;
	
	private final SessionAttributes attributes;
	
	// TODO maybe better to make list and make this more dynamic?
	private final ImcHandler imHandler;
	private final TnccHandler tnccHandler;
	
	private SessionState sessionState;
	private TncConnectionState connectionState;
	
	/* SessionStateContext */
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.session.SessionStateContext#receiveBatch()
	 */
	@Override
	public TnccsBatchContainer receiveBatch() throws SerializationException,
			ValidationException, ConnectionException {
		this.checkConnection();
		TnccsBatchContainer result = this.reader.read(this.connection.getInputStream(), -1);
		return result;
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.session.SessionStateContext#sendBatch(de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch)
	 */
	@Override
	public void sendBatch(TnccsBatch b) throws SerializationException,
			ConnectionException {
		this.checkConnection();
		this.writer.write(b, this.connection.getOutputStream());
		try {
			this.connection.getOutputStream().flush();
		} catch (IOException e) {
			throw new ConnectionException(e.getMessage(),e);
		}
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.session.SessionStateContext#handleBatch(de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch)
	 */
	@Override
	public void handleBatch(TnccsBatch b) throws HandlingException {
		List<? extends TnccsMessage> m = b.getMessages();
		for (TnccsMessage tnccsMessage : m) {
			// TODO make a better filter here, only bring those message to a handler who can handle it.
			this.imHandler.forwardMessage(this, tnccsMessage.getValue());
			this.tnccHandler.forwardMessage(this, tnccsMessage.getValue());
		}
	}
	
	/* TnccsSessionContext */
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.newp.handler.TnccsSessionContext#setConnectionState(de.hsbremen.tc.tnc.connection.TncConnectionState, de.hsbremen.tc.tnc.newp.handler.StateChangePermit)
	 */
	@Override
	public synchronized void setConnectionState(TncConnectionState state,
			StateChangePermit permit) {
		LOGGER.debug("Connection state has changed to: " + connectionState.toString());
		if(permit != null){
			this.connectionState = state;
			
			// careful here, this could easily end up in a loop so.
			// although this is only a shallow test, it is better than nothing
			if(!this.tnccHandler.getClass().isInstance(permit)){
				this.tnccHandler.setConnectionState(state);
			}
			if(!this.imHandler.getClass().isInstance(permit)){
				this.imHandler.setConnectionState(state);
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.newp.handler.TnccsSessionContext#getTnccsAttribute(de.hsbremen.tc.tnc.attribute.TncAttributeType)
	 */
	@Override
	public Object getAttribute(TncAttributeType type) throws TncException {
	
			Object o = null;
			try{
				o = this.connection.getAttribute(type);
			}catch(ConnectionAttributeNotFoundException e){
				o = this.attributes.getAttribute(type);
			}
			
			return o;
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.newp.handler.TnccsSessionContext#setTnccsAttribute(de.hsbremen.tc.tnc.attribute.TncAttributeType, java.lang.Object)
	 */
	@Override
	public void setAttribute(TncAttributeType type, Object value) throws TncException {
		
		this.attributes.setAttribute(type, value);
	
	}
	
	/* SessionConnectionContext */
	
	@Override
	public void requestHandshakeRetry(ImHandshakeRetryReasonEnum reason)
			throws TncException {
		
		this.setConnectionState(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_HANDSHAKE, this);	
		
	}

	@Override
	public void addMessage(TnccsMessage message) throws TncException {
		if(message != null){
			try{
				this.checkConnection();
			}catch(ConnectionException e){
				throw new TncException(e.getMessage(),e,TncExceptionCodeEnum.TNC_RESULT_ILLEGAL_OPERATION);
			}
			// TODO check message size, check round trips if any
			this.messageQueue.add(message);
		}
	}

	private void checkConnection() throws ConnectionException{
		if(!connection.isOpen()){
			throw new ConnectionException("Underlying connection is closed.");
		}
	}
}
