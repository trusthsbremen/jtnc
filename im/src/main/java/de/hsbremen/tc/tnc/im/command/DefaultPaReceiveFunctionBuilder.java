package de.hsbremen.tc.tnc.im.command;

import org.ietf.nea.pb.message.PbMessageValueIm;

import de.hsbremen.tc.tnc.command.FunctionCall;
import de.hsbremen.tc.tnc.command.TimedFunctionCallDecorator;

public class DefaultPaReceiveFunctionBuilder implements PaReceiveFunctionBuilder{

	@Override
	public FunctionCall build(PbMessageValueIm message) {
		return new TimedFunctionCallDecorator(new ReceiveMessageFunctionCallAdapter(im, imConnection, message));
	}

}
