package de.hsbremen.tc.tnc.tnccs.message.handler.simple;

import de.hsbremen.tc.tnc.tnccs.message.handler.ImcHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccContentHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccContentHandlerFactory;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsValidationExceptionHandler;

public class DefaultTnccContentHandlerFactory implements TnccContentHandlerFactory {

	@Override
	public TnccContentHandler createHandler(ImcHandler imHandler, TnccHandler tncsHandler, TnccsValidationExceptionHandler exceptionHandler){
		
		TnccContentHandler contentHandler = new DefaultTnccContentHandler(imHandler, tncsHandler, exceptionHandler) ;
		
		return contentHandler;
	}
	
}
