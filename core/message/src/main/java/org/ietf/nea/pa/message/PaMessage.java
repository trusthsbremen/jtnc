package org.ietf.nea.pa.message;

import java.util.Collections;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttribute;

import de.hsbremen.tc.tnc.message.m.message.ImMessage;
import de.hsbremen.tc.tnc.util.NotNull;

public class PaMessage implements ImMessage {

	 private final PaMessageHeader header;                        
	 private final List<PaAttribute> attributes;
	 
	 public PaMessage(final PaMessageHeader header, List<PaAttribute> attributes) {
			NotNull.check("Message header cannot be null.", header);
			NotNull.check("Attributes cannot be null.", attributes);

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
