package de.hsbremen.tc.tnc.tnccs.client;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.tnccs.im.GlobalHandshakeRetryListener;

public class GlobalHandshakeRetryProxy implements GlobalHandshakeRetryListener{

	private GlobalHandshakeRetryListener listener;
	
	public void register(GlobalHandshakeRetryListener listener){
		if(this.listener == null){
			this.listener = listener;
		}else{
			throw new IllegalStateException("Listener already registered.");
		}
	}
	
	@Override
	public void requestGlobalHandshakeRetry(ImHandshakeRetryReasonEnum reason)
			throws TncException {
		if(this.listener != null){
			this.listener.requestGlobalHandshakeRetry(reason);
		}else{
			throw new TncException("Global handshake retry is not supported.", TncExceptionCodeEnum.TNC_RESULT_CANT_RETRY);
		}
		
	}

}
