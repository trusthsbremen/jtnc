package de.hsbremen.tc.tnc.session;

import java.util.List;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;

import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;

import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.session.state.SessionState;
import de.hsbremen.tc.tnc.session.state.StateEnd;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsReader;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsWriter;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;
import de.hsbremen.tc.tnc.transport.exception.ConnectionAttributeNotFoundException;

public class SimpleSession implements Runnable{

//	private final SessionParameter parameter;
	private final TransportConnection connection;
	private final TnccsWriter<TnccsBatch> writer;
	private final TnccsReader<TnccsBatchContainer> reader;
	private SessionState sessionState;
	private final SessionObserver observer;
	
//	private final List<TnccsMessage> messageQueue;
	
	public SimpleSession(/* SessionParameter parameter , */ SessionState initState, TransportConnection connection,
			TnccsWriter<TnccsBatch> writer,
			TnccsReader<TnccsBatchContainer> reader, SessionObserver observer) {
	
//		this.parameter = parameter;
		this.sessionState = initState;
		this.connection = connection;
		this.observer = observer;
		this.writer = writer;
		this.reader = reader;
		
		
//		this.messageQueue = new LinkedList<>();
	}

	public TnccsBatchContainer receiveBatch() throws SerializationException, ValidationException{
		if(this.connection != null && this.connection.isOpen()){
			InputStream in = this.connection.getInputStream();
			return this.reader.read(in, -1);
		}
		
		throw new SerializationException("Stream seems closed.", true);
	}
	
	public void sendBatch(TnccsBatch batch) throws SerializationException{
		if(this.connection != null && this.connection.isOpen()){
			OutputStream out = this.connection.getOutputStream();
			this.writer.write(batch, out);
		}
	}

//  Needed for ImConnection	
//	public Object getAttribute(TncAttributeType type) throws TncException{
//		if(type == TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_IFTNCCS_PROTOCOL){
//			return this.parameter.getTnccsProtocol();
//		}
//		
//		if(type == TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_IFTNCCS_VERSION){
//			return this.parameter.getTnccsVersion();
//		}
//		
//		if(type == TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_PREFERRED_LANGUAGE){
//			return this.parameter.getPreferredLanguage();
//		}
//
//		try {
//			return this.connection.getAttribute(type);
//		} catch (ConnectionAttributeNotFoundException e) {
//			throw new TncException(e.getMessage(), TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
//		}
//	}

//  Needed for States	
//	public List<TnccsMessage> collectMessages(){
//		List<TnccsMessage> messages = new LinkedList<>(this.messageQueue);
//		this.messageQueue.clear();
//		
//		return messages;
//	}

	@Override
	public void run() {
		while(!(this.sessionState instanceof StateEnd)){
			this.sessionState.handle(this);
		}
		this.connection.close();
		this.observer.notifyClose(this.connection);
	}
}
