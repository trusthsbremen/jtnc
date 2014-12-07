package de.hsbremen.tc.tnc.tnccs.message.handler;

import java.util.List;

import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;

public interface TncsContentHandler extends TnccsContentHandler{

	public abstract List<TnccsMessage> solicitRecommendation();
	
}
