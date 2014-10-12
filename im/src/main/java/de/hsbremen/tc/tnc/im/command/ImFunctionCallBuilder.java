package de.hsbremen.tc.tnc.im.command;

import org.ietf.nea.pb.message.PbMessageValueIm;

import de.hsbremen.tc.tnc.command.FunctionCall;

interface ImFunctionCallBuilder<S,T> {
	public FunctionCall buildReceiveMessageCall(final S im, final T connection, final PbMessageValueIm message);
	public FunctionCall buildBeginHandshakeCall(final S im, final T connection);
	public FunctionCall buildBatchEndingCall(final S im, final T connection);
}
