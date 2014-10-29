package de.hsbremen.tc.tnc.session.state;

import java.util.ArrayList;
import java.util.List;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.batch.PbBatchFactoryIetf;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;
import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.session.context.SessionContext;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public abstract class AbstractPbState implements SessionState {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPbState.class);
	
	@Override
	public SessionState handle(SessionContext context){
		SessionState next = null;
		try{
			if(context.isServerSession()){
				next = handleAsServer(context);
			}else{
				next = handleAsClient(context);
			}
		}catch(ConnectionException | SerializationException e){
			this.handleException(e, context);
		}
		
		return next;
	}

	protected abstract SessionState handleAsServer(SessionContext context) throws ConnectionException, SerializationException;
	
	protected abstract SessionState handleAsClient(SessionContext context) throws ConnectionException, SerializationException;
		
	private SessionState handleException(ComprehensibleException e, SessionContext context) {
		LOGGER.error("Exception of type " + e.getClass().getCanonicalName() + " occured: " + e.getMessage());
		LOGGER.debug("Exception stack trace: \n", e);
		if( 
				this.getClass().getSimpleName().equals(StateInit.class.getSimpleName()) 
				
				||
				
				(context.isServerSession() && 
						 this.getClass().getSimpleName().equals(StateServerWorking.class.getSimpleName()))
				||
				
				(!context.isServerSession() &&
						this.getClass().getSimpleName().equals(StateClientWorking.class.getSimpleName()))
		){
			
			LOGGER.error("Because the state does allow it, try to send a close batch, containing an error of type local error.");
			
			TnccsBatch b = null;
			try{
				List<PbMessage> messages = new ArrayList<>();
				PbMessageFactoryIetf.createError(new PbMessageErrorFlagsEnum[]{PbMessageErrorFlagsEnum.FATAL}, PbMessageErrorCodeEnum.IETF_LOCAL,new byte[0]);
	
				if(context.isServerSession()){
					b = PbBatchFactoryIetf.createServerClose(messages);
					
				}else{
					b = PbBatchFactoryIetf.createClientClose(messages);
				}
			}catch(RuleException e){
				
			}
			
			try {
				context.sendBatch(b);
			} catch (SerializationException | ConnectionException e1) {
				LOGGER.error("Exception of type " + e.getClass().getCanonicalName() + " occured: " + e.getMessage() + "\n"
						+ "Close batch could not be send.");
				LOGGER.debug("Exception stack trace: \n", e);
			}
		}
		
		LOGGER.error("Transitioning to end state, closing the session and the connection.");
		
		return new StateEnd();
	}

}
