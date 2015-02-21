package org.ietf.nea.pa.validate.rules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributePortFilterStatus;
import org.ietf.nea.pa.attribute.util.PortFilterEntry;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

public class OpposingPortFilterEntries {

	public static void check(final List<PortFilterEntry> entries) throws RuleException{
	
		if(entries != null){
		
			Map<Short,Map<Integer,PaAttributePortFilterStatus>> sorted = new HashMap<>();
			
			for (PortFilterEntry entry : entries) {
				
				if(sorted.containsKey(entry.getProtocolNumber())){
					if(sorted.get(entry.getProtocolNumber()).containsKey(entry.getPortNumber())){
						if(sorted.get(entry.getProtocolNumber()).values().iterator().next() != entry.getFilterStatus()){
							throw new RuleException("The port filter contains entries for the protocol " + entry.getProtocolNumber() + " with differnt blocking status.", false, PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(), PaErrorCauseEnum.MIXED_PROTOCOL_BLOCKING.number(), 
									 entry.getProtocolNumber(), entry.getPortNumber(), entry.getFilterStatus().toString() ,entries.indexOf(entry));

						}
						if(sorted.get(entry.getProtocolNumber()).get(entry.getPortNumber()) != entry.getFilterStatus()){
							throw new RuleException("The port filter contains duplicate entries for the tupel " + entry.getProtocolNumber() + ":" +entry.getPortNumber() + " with differnt blocking status.", false, PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(), PaErrorCauseEnum.DUPLICATE_PORT_ENTRY.number(), 
									 entry.getProtocolNumber(), entry.getPortNumber(), entry.getFilterStatus().toString(), sorted.get(entry.getProtocolNumber()).get(entry.getPortNumber()).toString() ,entries.indexOf(entry));
						}else{
							throw new RuleException("The port filter contains duplicate entries for the tupel " + entry.getProtocolNumber() + ":" +entry.getPortNumber() + ".", false, PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(), PaErrorCauseEnum.DUPLICATE_PORT_ENTRY.number(), 
									 entry.getProtocolNumber(), entry.getPortNumber(), entry.getFilterStatus().toString() ,entries.indexOf(entry));
						}
					}else{
						sorted.get(entry.getProtocolNumber()).put(entry.getPortNumber(), entry.getFilterStatus());
					}
				}else{
					sorted.put(entry.getProtocolNumber(), new HashMap<Integer,PaAttributePortFilterStatus>());
					sorted.get(entry.getProtocolNumber()).put(entry.getPortNumber(), entry.getFilterStatus());
				}
			}
		}
	}
}
