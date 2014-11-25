package de.hsbremen.tc.tnc.session;

import java.util.Deque;

import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.connection.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.session.context.SessionConnectionContext;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsReader;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsWriter;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;
import de.hsbremen.tc.tnc.transport.exception.ConnectionAttributeNotFoundException;

public class Session2 implements SessionConnectionContext{

	private final TransportConnection connection;
	private final TnccsWriter<? extends TnccsBatch> writer;
	private final TnccsReader<? extends TnccsBatch> reader;
	private final Deque<TnccsMessage> messageQueue;
	
	
	
	@Override
	public void requestHandshakeRetry(ImHandshakeRetryReasonEnum reason)
			throws TncException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMessage(TnccsMessage message) throws TncException {
		if(message != null){
			if(!connection.isOpen()){
				throw new TncException("Underlying connection is closed.", TncExceptionCodeEnum.TNC_RESULT_ILLEGAL_OPERATION);
			}
			// TODO check message size, check round trips if any
			this.messageQueue.add(message);
		}
	}

	@Override
	public Object getAttribute(TncAttributeType type) throws TncException {
		try {
			return this.connection.getAttribute(type);
		} catch (ConnectionAttributeNotFoundException e) {
			throw new TncException(e.getMessage(), TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
		}
	}

}
