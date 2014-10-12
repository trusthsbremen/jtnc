package de.hsbremen.tc.tnc.im.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;

import de.hsbremen.tc.tnc.exception.AttributeException;
import de.hsbremen.tc.tnc.im.exception.AttributeTypeUnexpected;
import de.hsbremen.tc.tnc.im.exception.AttributeUnknownException;
import de.hsbremen.tc.tnc.im.module.ImModule;
import de.hsbremen.tc.tnc.im.module.SupportedMessageType;

public class DefaultImModule<T> implements ImModule<T>{

    private final T im;
    
    private final long primaryId;
    
    private boolean tncsFirstSupport;
    
    // TODO IMC ID has a maximum size of unsigned short why is it long ?
    private final List<Long> imIds;
   
    private List<SupportedMessageType> supportedMessageTypes;

    public DefaultImModule(final long primaryImId, final T im) {
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
		List<SupportedMessageType> sMessageTypes = null;
		synchronized (this.supportedMessageTypes) {
			sMessageTypes = Collections.unmodifiableList(this.supportedMessageTypes);
		}
	    return sMessageTypes;
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
		List<Long> currentIds = null;
		synchronized (this.im) {
			currentIds = Collections.unmodifiableList(imIds);
		}
		
    	return currentIds;
    }
    
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.container.ImConttainerIf#addImId(long)
	 */
	@Override
	public void addImId(long imId){
    	if(imId <= 0){
    		throw new IllegalArgumentException("ID cannot be 0 or less than 0.");
    	}
    	synchronized (this.im) {
    		this.imIds.add(imId);
		}
    	
    }
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.container.ImConttainerIf#setSupportedMessageTypes(java.util.List)
	 */
	@Override
	public void setSupportedMessageTypes(final List<SupportedMessageType> supportedTypes){
    	synchronized (this.supportedMessageTypes) {
			if(supportedTypes != null){
	    		this.supportedMessageTypes = supportedTypes;
	    	}else{
	    		this.supportedMessageTypes = new LinkedList<>();
	    	}
		}
    }


	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.container.ImConttainerIf#getAttribute(java.lang.Long)
	 */
	@Override
	public Object getAttribute(Long id) throws AttributeUnknownException {
		if(id == AttributeSupport.TNC_ATTRIBUTEID_PRIMARY_IMC_ID){
			return new Long(this.primaryId);
		}
		
		if(id == AttributeSupport.TNC_ATTRIBUTEID_IMC_SPTS_TNCS1){
			return (this.tncsFirstSupport) ? Boolean.TRUE : Boolean.FALSE;
		}
		
		throw new AttributeUnknownException("The attribute with ID " + id + " is unknown.", Long.toString(id));
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
	public void setAttribute(Long id, Object value) throws AttributeException{
		
		if(id == AttributeSupport.TNC_ATTRIBUTEID_IMC_SPTS_TNCS1){
			if(value instanceof Boolean){
				this.tncsFirstSupport = ((Boolean)value) ? true : false;
			}else{
				throw new AttributeTypeUnexpected("The attribute type " + value.getClass().getCanonicalName() +" was not expected, use the type " + 
						Boolean.class.getCanonicalName() +".", value.getClass().getCanonicalName(), Boolean.class.getCanonicalName());
			}
		}else{
			throw new AttributeUnknownException("The attribute with ID " + id + " is unknown.", Long.toString(id));
		}
		
	}
    
	
}
