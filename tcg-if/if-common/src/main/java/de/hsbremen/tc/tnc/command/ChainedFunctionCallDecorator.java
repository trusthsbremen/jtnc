package de.hsbremen.tc.tnc.command;

import org.trustedcomputinggroup.tnc.ifimc.TNCException;

public class ChainedFunctionCallDecorator implements FunctionCall{

	private final FunctionCall successor;
	private final FunctionCall present;
	
	public ChainedFunctionCallDecorator(FunctionCall present, FunctionCall successor){
		this.present = present;
		this.successor = successor;
	}
	
	@Override
	public void call() throws TNCException {
		if(this.present != null){
			this.present.call();
		}
		if(this.successor != null){
			this.successor.call();
		}
	}
	
	
	
}
