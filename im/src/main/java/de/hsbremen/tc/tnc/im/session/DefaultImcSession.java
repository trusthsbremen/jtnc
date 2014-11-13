package de.hsbremen.tc.tnc.im.session;

import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapter;
import de.hsbremen.tc.tnc.im.adapter.connection.enums.ImConnectionStateEnum;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorManager;

public class DefaultImcSession extends AbstractDefaultImSession<ImcConnectionAdapter> implements ImcSession{ 
	
	DefaultImcSession(ImcConnectionAdapter connection, ImConnectionStateEnum connectionState, ImEvaluatorManager evaluator){
		super(connection, connectionState,evaluator);
	}
}
