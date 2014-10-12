package de.hsbremen.tc.tnc.im.command;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.IMCLong;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.command.FunctionCall;
import de.hsbremen.tc.tnc.command.TimedFunctionCallDecorator;

public class ImcFunctionCallBuilderIetf implements
		ImcFunctionCallBuilder {

	private long timeout;
	
	public ImcFunctionCallBuilderIetf(long timeout){
		this.timeout = timeout;
	}
	
	@Override
	public FunctionCall buildReceiveMessageCall(final IMC im, final IMCConnection connection,
			final PbMessageValueIm message) {
		FunctionCall receive = new FunctionCall() {
			
			@Override
			public void call() throws TNCException {
				byte bFlags = 0;
				for (PbMessageImFlagsEnum pbMessageImFlagsEnum : message.getImFlags()) {
					bFlags |= pbMessageImFlagsEnum.bit();
				}

				if(im instanceof IMCLong){
		
					((IMCLong)im).receiveMessageLong(connection,
							bFlags, message.getSubVendorId(), message.getSubType(), message.getMessage(), message.getValidatorId(), message.getCollectorId());
				}else{
					
					long messageType = message.getSubVendorId() << 8 | (message.getSubType() & 0xFF);
					im.receiveMessage(connection, messageType, message.getMessage());
				}
				// TODO add SOH
			}
		};
		
		return new TimedFunctionCallDecorator(receive, timeout);
	}

	@Override
	public FunctionCall buildBeginHandshakeCall(final IMC im, final IMCConnection connection) {
		FunctionCall begin = new FunctionCall() {
			@Override
			public void call() throws TNCException {
				im.beginHandshake(connection);
			}
		};
		return new TimedFunctionCallDecorator(begin, timeout);
	}

	@Override
	public FunctionCall buildBatchEndingCall(final IMC im, final IMCConnection connection) {
		FunctionCall end = new FunctionCall() {
			@Override
			public void call() throws TNCException {
				im.batchEnding(connection);
			}
		};
		return new TimedFunctionCallDecorator(end, timeout);
	}

}
