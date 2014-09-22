package de.hsbremen.tc.tnc.im.command;

import org.ietf.nea.pb.message.PbMessageValueIm;

import de.hsbremen.tc.tnc.command.FunctionCall;

public interface PaReceiveFunctionBuilder {

	public FunctionCall build(PbMessageValueIm message);
	
}
