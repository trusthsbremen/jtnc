package de.hsbremen.tc.tnc.imhandler.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public class ImConnectionMessageQueueIetf implements ImConnectionMessageQueue {

	private List<TnccsMessageValue> values;
	
	ImConnectionMessageQueueIetf(){
		this.values = new ArrayList<>();
	}
	
	public void addMessage(TnccsMessageValue value){
		this.values.add(value);
	}
	public List<TnccsMessageValue> getMessages(){
		return Collections.unmodifiableList(values);
	}
	public void clear(){
		this.values = new ArrayList<>();
	}
}
