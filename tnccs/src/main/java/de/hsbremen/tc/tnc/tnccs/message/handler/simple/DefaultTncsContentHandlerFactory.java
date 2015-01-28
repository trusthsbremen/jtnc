package de.hsbremen.tc.tnc.tnccs.message.handler.simple;

import de.hsbremen.tc.tnc.tnccs.message.handler.ImvHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsValidationExceptionHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TncsContentHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TncsContentHandlerFactory;
import de.hsbremen.tc.tnc.tnccs.message.handler.TncsHandler;

public class DefaultTncsContentHandlerFactory implements TncsContentHandlerFactory {

	@Override
	public TncsContentHandler createHandler(ImvHandler imHandler, TncsHandler tncsHandler, TnccsValidationExceptionHandler exceptionHandler){
		
		TncsContentHandler contentHandler = new DefaultTncsContentHandler(imHandler, tncsHandler, exceptionHandler) ;
		
		return contentHandler;
	}
	
}
