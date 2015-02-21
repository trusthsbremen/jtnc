package org.ietf.nea.pa.attribute;

import java.util.Collections;
import java.util.List;

import org.ietf.nea.pa.attribute.util.PortFilterEntry;

public class PaAttributeValuePortFilter extends AbstractPaAttributeValue {
	
	private final List<PortFilterEntry> filterEntries; // 32 bit(s) value must have at minimum one entry
	
	PaAttributeValuePortFilter(long length, List<PortFilterEntry> filterEntries) {
		super(length);
		
		this.filterEntries = filterEntries;
	}

	/**
	 * @return the references
	 */
	public List<PortFilterEntry> getFilterEntries() {
		return Collections.unmodifiableList(this.filterEntries);
	}

	
}
