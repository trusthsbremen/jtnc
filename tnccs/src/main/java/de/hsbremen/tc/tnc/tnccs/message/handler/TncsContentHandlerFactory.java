package de.hsbremen.tc.tnc.tnccs.message.handler;


public interface TncsContentHandlerFactory {

	public abstract TncsContentHandler createHandler(ImvHandler imHandler,
			TncsHandler tncsHandler,
			TnccsValidationExceptionHandler exceptionHandler);

}