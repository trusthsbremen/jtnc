package de.hsbremen.tc.tnc.tnccs.adapter.connection.simple;


import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.AbstractImConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionContext;
import de.hsbremen.tc.tnc.tnccs.session.base.HandshakeRetryListener;

//FIXME This is a tradeoff, because I could not figure out a way to fix the 
//circular dependency between session and IMC/VConnection.
public class DefaultImcConnectionContext extends AbstractImConnectionContext implements ImcConnectionContext{


	public DefaultImcConnectionContext(Attributed attributes, HandshakeRetryListener listener) {
		super(attributes,listener);
	}
	
}
