package de.hsbremen.tc.tnc.im.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;
import org.trustedcomputinggroup.tnc.ifimc.IMC;

import de.hsbremen.tc.tnc.ifimc.Attributed;

public class ImcContainer implements Attributed{

    private final IMC im;
    
    private final long primaryId;
    
    private boolean tncsFirstSupport;
    
    // TODO IMC ID has a maximum size of unsigned short why is it long ?
    private final List<Long> imIds;
   
    private List<SupportedMessageType> supportedMessageTypes;

    public ImcContainer(final long primaryImId, final IMC im) {
    	this.primaryId = primaryImId;
    	this.imIds = new ArrayList<>();
    	this.imIds.add(primaryImId);
    	this.im = im;
    }
    

	public IMC getIm(){
    	return this.im;
    }
    

	public long getPrimaryId(){
    	return primaryId;
    }
    

	public List<SupportedMessageType> getSupportedMessageTypes(){
	   return Collections.unmodifiableList(supportedMessageTypes);
    }

	/**
	 * @return the tncsFirstSupport
	 */
	public boolean hasTncsFirstSupport() {
		return this.tncsFirstSupport;
	}

	public List<Long> getImIds(){
    	return Collections.unmodifiableList(imIds);
    }
    
	public void addImId(long imId){
    	if(imId == 0){
    		throw new IllegalArgumentException("ID cannot be 0.");
    	}
    	this.imIds.add(imId);
    }
	
	public void setSupportedMessageTypes(final List<SupportedMessageType> supportedTypes){
    	if(supportedTypes != null){
    		this.supportedMessageTypes = supportedTypes;
    	}else{
    		this.supportedMessageTypes = new LinkedList<>();
    	}
    }


	@Override
	public Object getAttribute(Long id) {
		Object o = null;
		if(id == AttributeSupport.TNC_ATTRIBUTEID_PRIMARY_IMC_ID){
			o = new Long(this.primaryId);
		}
		
		if(id == AttributeSupport.TNC_ATTRIBUTEID_IMC_SPTS_TNCS1){
			o = (this.tncsFirstSupport) ? Boolean.TRUE : Boolean.FALSE;
		}
		
		return o;
	}


	@Override
	public Map<Long, Object> getAttributes() {
		Map<Long, Object> attributes = new HashMap<>();
		
		attributes.put(AttributeSupport.TNC_ATTRIBUTEID_PRIMARY_IMC_ID, new Long(this.primaryId));
		attributes.put(AttributeSupport.TNC_ATTRIBUTEID_IMC_SPTS_TNCS1, (this.tncsFirstSupport) ? Boolean.TRUE : Boolean.FALSE);
		
		return Collections.unmodifiableMap(attributes);
	}


	@Override
	public boolean setAttribute(Long id, Object value) {
		boolean success = false;
		if(id == AttributeSupport.TNC_ATTRIBUTEID_IMC_SPTS_TNCS1){
			if(value instanceof Boolean){
				this.tncsFirstSupport = ((Boolean)value) ? true : false;
				success = true;
			}
		}
		return success;
	}
    
	
}
