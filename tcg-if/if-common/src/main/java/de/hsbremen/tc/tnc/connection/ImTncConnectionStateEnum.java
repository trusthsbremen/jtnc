package de.hsbremen.tc.tnc.connection;


public enum ImTncConnectionStateEnum implements ImConnectionState{
 
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
    
    private ImTncConnectionStateEnum(long state){
        this.state = state;
    }

    public long state(){
        return this.state;
    }
    
}
