package de.hsbremen.tc.tnc.im.adapter.connection.enums;



public enum ImConnectionStateEnum {
 
	// TNC (TNCConstants could be used but for reusablility this enum was created
	/**
	 * Network connection created.
	 */

	TNC_CONNECTION_STATE_CREATE(0),

	/**
	 * Handshake about to start.
	 */

	TNC_CONNECTION_STATE_HANDSHAKE(1),

	/**
	 * Handshake completed. TNC Server recommended that requested access be
	 * allowed.
	 */

	TNC_CONNECTION_STATE_ACCESS_ALLOWED(2),

	/**
	 * 
	 * Handshake completed. TNC Server recommended that isolated access be
	 * allowed.
	 */

	TNC_CONNECTION_STATE_ACCESS_ISOLATED(3),

	/**
	 * Handshake completed. TNCS Server recommended that no network access be
	 * allowed.
	 */

	TNC_CONNECTION_STATE_ACCESS_NONE(4),

	/**
	 * About to delete network connection. Remove all associated state.
	 */

	TNC_CONNECTION_STATE_DELETE(5),

	// HSB (not specified in RFC)
	/**
	 * Not information about network connection available. 
	 * Only internally used, if TNCC does not support information
	 * about the network connection. 
	 */
	HSB_CONNECTION_STATE_UNKNOWN(-1L);
    
    private long state;
    
    private ImConnectionStateEnum(long state){
        this.state = state;
    }

    public long state(){
        return this.state;
    }
	
    public static ImConnectionStateEnum fromState(long state){
    	
    	if(state == TNC_CONNECTION_STATE_ACCESS_ALLOWED.state()){
    		return TNC_CONNECTION_STATE_ACCESS_ALLOWED;
    	}
    	
    	if(state == TNC_CONNECTION_STATE_ACCESS_ISOLATED.state()){
    		return TNC_CONNECTION_STATE_ACCESS_ISOLATED;
    	}
    	
    	if(state == TNC_CONNECTION_STATE_ACCESS_NONE.state()){
    		return TNC_CONNECTION_STATE_ACCESS_NONE;
    	}
    	
    	if(state == TNC_CONNECTION_STATE_CREATE.state()){
    		return TNC_CONNECTION_STATE_CREATE;
    	}
    	
    	if(state == TNC_CONNECTION_STATE_HANDSHAKE.state()){
    		return TNC_CONNECTION_STATE_HANDSHAKE;
    	}
    	
    	if(state == TNC_CONNECTION_STATE_DELETE.state()){
    		return TNC_CONNECTION_STATE_DELETE;
    	}
    	
    	return HSB_CONNECTION_STATE_UNKNOWN;
    }
    
}
