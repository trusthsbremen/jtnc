package org.ietf.nea.pa.attribute;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLength;
import org.ietf.nea.pa.attribute.util.PortFilterEntry;
import org.ietf.nea.pa.validate.rules.MinEntryCount;
import org.ietf.nea.pa.validate.rules.OpposingPortFilterEntries;

public class PaAttributeValuePortFilterBuilderIetf implements
	PaAttributeValuePortFilterBuilder {
	
	
	private long length;
	private List<PortFilterEntry> filterEntries;
	
	public PaAttributeValuePortFilterBuilderIetf(){
		this.length = 0;
		this.filterEntries = new LinkedList<>();
	}

	@Override
	public PaAttributeValuePortFilterBuilder addEntries(PortFilterEntry entry, PortFilterEntry... entries) throws RuleException {
		
		List<PortFilterEntry> temp = new ArrayList<>();
		
		if(entry != null){
			temp.add(entry);
		}
		
		if(entries != null){
			for (PortFilterEntry pEntry : entries) {
				if(pEntry != null){
					temp.add(pEntry);
				}
			}
		}
	
		OpposingPortFilterEntries.check(temp);
		
		this.filterEntries.addAll(temp);
		this.length = (PaAttributeTlvFixedLength.PORT_FT.length() * this.filterEntries.size());

		MinEntryCount.check((byte)1, this.filterEntries);
		
		return this;
	}

	@Override
	public PaAttributeValuePortFilter toValue(){
		
		return new PaAttributeValuePortFilter(this.length, this.filterEntries);
	}

	@Override
	public PaAttributeValuePortFilterBuilder clear() {
		return new PaAttributeValuePortFilterBuilderIetf();
	}

}
