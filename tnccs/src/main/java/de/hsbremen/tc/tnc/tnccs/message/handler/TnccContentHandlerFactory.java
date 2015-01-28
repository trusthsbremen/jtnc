package de.hsbremen.tc.tnc.tnccs.message.handler;


public interface TnccContentHandlerFactory {

	public abstract TnccContentHandler createHandler(ImcHandler imHandler,
			TnccHandler tncsHandler,
			TnccsValidationExceptionHandler exceptionHandler);

}