package de.hsbremen.tc.tnc.im.adapter.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import de.hsbremen.tc.tnc.im.adapter.data.enums.ImComponentFlagsEnum;
import de.hsbremen.tc.tnc.m.attribute.ImAttribute;

public class ImObjectComponent extends AbstractImComponent{
	
	protected final EnumSet<ImComponentFlagsEnum> imFlags;
    private final List<? extends ImAttribute> attributes; 

    ImObjectComponent(final ImComponentFlagsEnum[] flags, final long subVendorId, final long subType,
			final long collectorId, final long validatorId, final List<? extends ImAttribute> attributes) {
    	super(subVendorId, subType, collectorId, validatorId);
		
		if(flags != null && flags.length > 0){
        	this.imFlags = EnumSet.copyOf(Arrays.asList(flags));
		}else{
			this.imFlags = EnumSet.noneOf(ImComponentFlagsEnum.class);
		}
    	
		this.attributes = (attributes != null) ? attributes : new ArrayList<ImAttribute>(0);
	}
    
    /**
	 * @return the flags
	 */
	public Set<ImComponentFlagsEnum> getImFlags() {
	    return Collections.unmodifiableSet(imFlags);
	}
    
	/**
     * @return the message
     */
    public List<? extends ImAttribute> getAttributes() {
        return Collections.unmodifiableList(this.attributes);
    }

}
