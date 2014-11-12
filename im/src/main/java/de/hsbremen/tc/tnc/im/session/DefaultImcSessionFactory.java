package de.hsbremen.tc.tnc.im.session;

import de.hsbremen.tc.tnc.im.adapter.ImConnectionStateEnum;
import de.hsbremen.tc.tnc.im.adapter.connection.ImConnectionAdapter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapter;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorManager;

public class DefaultImcSessionFactory implements ImSessionFactory<ImcSession>{
	
	private static class Singleton {
		private static final ImSessionFactory<ImcSession> INSTANCE = new DefaultImcSessionFactory();
	}
	
	public static ImSessionFactory<ImcSession> getInstance(){
		return Singleton.INSTANCE;
	}

	@Override
	public <S extends ImConnectionAdapter, E extends ImEvaluatorManager> ImcSession createSession(final S connection,
			final E evaluator) {
		if(connection == null){
			throw new NullPointerException("Connection cannot be null.");
		}
		
		if(!(connection instanceof ImcConnectionAdapter)){
			throw new IllegalArgumentException("Connection must be instance of " + ImcConnectionAdapter.class.getCanonicalName() + ".");
		}
		
		if(evaluator == null){
			throw new NullPointerException("Evaluators cannot be null.");
		}
		
		if(!(evaluator instanceof ImEvaluatorManager)){
			throw new IllegalArgumentException("Evaluator manager must be instance of " + ImEvaluatorManager.class.getCanonicalName() + ".");
		}
			
		ImcSession session = new DefaultImcSession((ImcConnectionAdapter)connection, ImConnectionStateEnum.HSB_CONNECTION_STATE_UNKNOWN, evaluator);
			
		return session;
	}

}
