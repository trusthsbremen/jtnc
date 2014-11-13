package de.hsbremen.tc.tnc.im.session;

import de.hsbremen.tc.tnc.im.adapter.connection.ImConnectionAdapter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImvConnectionAdapter;
import de.hsbremen.tc.tnc.im.adapter.connection.enums.ImConnectionStateEnum;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorManager;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluatorManager;

public class DefaultImvSessionFactory implements ImSessionFactory<ImvSession>{
	
	private static class Singleton {
		private static final ImSessionFactory<ImvSession> INSTANCE = new DefaultImvSessionFactory();
	}
	
	public static ImSessionFactory<ImvSession> getInstance(){
		return Singleton.INSTANCE;
	}

	@Override
	public <S extends ImConnectionAdapter, T extends ImEvaluatorManager> ImvSession createSession(
			S connection, T evaluator) {
		
			if(connection == null){
				throw new NullPointerException("Connection cannot be null.");
			}
			
			if(!(connection instanceof ImvConnectionAdapter)){
				throw new IllegalArgumentException("Connection must be instance of " + ImvConnectionAdapter.class.getCanonicalName() + ".");
			}
			
			if(evaluator == null){
				throw new NullPointerException("Evaluators cannot be null.");
			}
			
			if(!(evaluator instanceof ImvEvaluatorManager)){
				throw new IllegalArgumentException("Evaluator manager must be instance of " + ImvEvaluatorManager.class.getCanonicalName() + ".");
			}
			
			ImvSession session = new DefaultImvSession((ImvConnectionAdapter)connection, ImConnectionStateEnum.HSB_CONNECTION_STATE_UNKNOWN, (ImvEvaluatorManager)evaluator);
			
			return session;
	}

	

}
