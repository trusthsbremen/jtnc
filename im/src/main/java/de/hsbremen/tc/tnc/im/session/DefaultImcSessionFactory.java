package de.hsbremen.tc.tnc.im.session;

import java.util.Map;

import de.hsbremen.tc.tnc.im.adapter.ImConnectionStateEnum;
import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapter;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluator;

public class DefaultImcSessionFactory implements ImcSessionFactory{

	@Override
	public ImSession createSession(ImcConnectionAdapter connection,
			Map<Long, ImEvaluator> evaluators) {
		if(connection == null){
			throw new NullPointerException("Connection cannot be null.");
		}
		
		if(evaluators == null){
			throw new NullPointerException("Evaluators cannot be null.");
		}
		
		ImSession session = new DefaultImcSession(connection, ImConnectionStateEnum.HSB_CONNECTION_STATE_UNKNOWN, evaluators);
		
		return session;
	}

}
