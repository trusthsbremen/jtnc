package de.hsbremen.tc.tnc.im.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;

public class DefaultImContainer<T> implements ImContainer<T>{

    private final T im;
    
    private final long primaryId;
    
    private boolean tncsFirstSupport;
    
    // TODO IMC ID has a maximum size of unsigned short why is it long ?
    private final List<Long> imIds;
   
    private List<SupportedMessageType> supportedMessageTypes;

    public DefaultImContainer(final long primaryImId, final T im) {
    	this.primaryId = primaryImId;
    	this.imIds = new ArrayList<>();
    	this.imIds.add(primaryImId);
    	this.im = im;
    }
    

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.container.ImConttainerIf#getIm()
	 */
	@Override
	public T getIm(){
    	return this.im;
    }
    

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.container.ImConttainerIf#getPrimaryId()
	 */
	@Override
	public long getPrimaryId(){
    	return primaryId;
    }
    

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.container.ImConttainerIf#getSupportedMessageTypes()
	 */
	@Override
	public List<SupportedMessageType> getSupportedMessageTypes(){
	   return Collections.unmodifiableList(supportedMessageTypes);
    }

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.container.ImConttainerIf#hasTncsFirstSupport()
	 */
	@Override
	public boolean supportsTncsFirst() {
		return this.tncsFirstSupport;
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.container.ImConttainerIf#getImIds()
	 */
	@Override
	public List<Long> getAllImIds(){
    	return Collections.unmodifiableList(imIds);
    }
    
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.container.ImConttainerIf#addImId(long)
	 */
	@Override
	public void addImId(long imId){
    	if(imId == 0){
    		throw new IllegalArgumentException("ID cannot be 0.");
    	}
    	this.imIds.add(imId);
    }
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.container.ImConttainerIf#setSupportedMessageTypes(java.util.List)
	 */
	@Override
	public void setSupportedMessageTypes(final List<SupportedMessageType> supportedTypes){
    	if(supportedTypes != null){
    		this.supportedMessageTypes = supportedTypes;
    	}else{
    		this.supportedMessageTypes = new LinkedList<>();
    	}
    }


	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.container.ImConttainerIf#getAttribute(java.lang.Long)
	 */
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


	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.container.ImConttainerIf#getAttributes()
	 */
	@Override
	public Map<Long, Object> getAttributes() {
		Map<Long, Object> attributes = new HashMap<>();
		
		attributes.put(AttributeSupport.TNC_ATTRIBUTEID_PRIMARY_IMC_ID, new Long(this.primaryId));
		attributes.put(AttributeSupport.TNC_ATTRIBUTEID_IMC_SPTS_TNCS1, (this.tncsFirstSupport) ? Boolean.TRUE : Boolean.FALSE);
		
		return Collections.unmodifiableMap(attributes);
	}


	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.container.ImConttainerIf#setAttribute(java.lang.Long, java.lang.Object)
	 */
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
