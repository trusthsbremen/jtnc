package de.hsbremen.tc.tnc.session.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.trustedcomputinggroup.tnc.TNCConstants;
import org.trustedcomputinggroup.tnc.TNCException;
import org.trustedcomputinggroup.tnc.tncc.IMCConnection;

import de.hsbremen.jtnc.ar.exception.tncc.ImcMessageBufferMandatoryException;
import de.hsbremen.jtnc.ar.message.enums.TnccsBatchDirectionalityEnum;
import de.hsbremen.jtnc.ar.message.enums.TnccsBatchTypeEnum;
import de.hsbremen.jtnc.ar.message.tnccs.TnccsAbstractMessage;
import de.hsbremen.jtnc.ar.message.tnccs.TnccsBatch;
import de.hsbremen.jtnc.ar.message.tnccs.ietf.TnccsMessageIm;
import de.hsbremen.jtnc.ar.message.validator.TnccsHeaderValidatorFactory;
import de.hsbremen.jtnc.ar.tncc.imc.ImcContainer;
import de.hsbremen.jtnc.ar.tncc.imc.connection.ImcMessageBuffer;
import de.hsbremen.jtnc.ar.tncc.session.TnccsSession;
import de.hsbremen.jtnc.ar.tncc.session.timed.AbstractTimedImcFunctionCall;
import de.hsbremen.jtnc.ar.tncc.session.timed.BeginHandshake;
import de.hsbremen.tc.tnc.session.context.SessionContext;

public class StateInit implements SessionState {

	@Override
	public void handle(SessionContext context) {
		context.initSession();
		
	}

}
