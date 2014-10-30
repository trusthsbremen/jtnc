package org.ietf.nea.pa.message;

import java.util.Collections;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttribute;

import de.hsbremen.tc.tnc.m.message.ImMessage;

public class PaMessage implements ImMessage {

	 private final PaMessageHeader header;                        
	 private final List<PaAttribute> attributes;

	 public PaMessage(final PaMessageHeader header, List<PaAttribute> attributes) {
			if(header == null){
				throw new NullPointerException("Message header cannot be null.");
			}
			if(attributes == null){
				throw new NullPointerException("Attributes cannot be null.");
			}
			this.header = header;
			this.attributes = attributes;
		}

	
	@Override
	public PaMessageHeader getHeader() {
		return this.header;
	}

	@Override
	public List<PaAttribute> getAttributes() {
		return Collections.unmodifiableList(this.attributes);
	}

}
