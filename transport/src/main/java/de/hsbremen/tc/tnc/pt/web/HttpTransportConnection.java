package de.hsbremen.tc.tnc.pt.web;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.transport.TnccsValueListener;
import de.hsbremen.tc.tnc.transport.TransportAddress;
import de.hsbremen.tc.tnc.transport.TransportConnection;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class HttpTransportConnection implements TransportConnection {

	@Override
	public TransportAddress getAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSelfInititated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void open(TnccsValueListener listener) throws ConnectionException {
		// TODO Auto-generated method stub

	}

	@Override
	public void send(ByteBuffer buffer) throws ConnectionException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public Attributed getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

}
