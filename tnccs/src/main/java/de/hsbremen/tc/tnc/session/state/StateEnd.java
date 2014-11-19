package de.hsbremen.tc.tnc.session.state;

import java.util.List;
import java.util.Map;

import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;
import org.trustedcomputinggroup.tnc.tncc.IMCConnection;

import de.hsbremen.jtnc.ar.message.tnccs.TnccsAbstractMessage;
import de.hsbremen.jtnc.ar.message.tnccs.TnccsBatch;
import de.hsbremen.jtnc.ar.tncc.imc.ImcContainer;
import de.hsbremen.jtnc.ar.tncc.imc.connection.ImcMessageBuffer;
import de.hsbremen.jtnc.ar.tncc.session.TnccsSession;
import de.hsbremen.jtnc.ar.tncc.session.timed.AbstractTimedImcFunctionCall;
import de.hsbremen.jtnc.ar.tncc.session.timed.BatchEnding;
import de.hsbremen.tc.tnc.session.context.SessionContext;

public class StateEnd implements SessionState {

	@Override
	public SessionState handle(SessionContext context) {
		// TODO Auto-generated method stub
		return null;
	}
 
}
