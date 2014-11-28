package de.hsbremen.tc.tnc.newp.enums;

public enum DefaultImHandlerStateEnum implements ImHandlerState{

		/**
		 * Handshake running but not decided.
		 */
	
		HSB_SESSION_STATE_HANDSHAKE_RUNNING(0),
	
		/**
		 * Handshake about to start.
		 */

		HSB_SESSION_STATE_HANDSHAKE_START(1),
		
		/**
		 * Handshake completed. TNC Server recommended that requested access be
		 * allowed.
		 */

		HSB_SESSION_STATE_DECIDED_ALLOWED(2),

		/**
		 * 
		 * Handshake completed. TNC Server recommended that isolated access be
		 * allowed.
		 */

		HSB_SESSION_STATE_DECIDED_ISOLATED(3),

		/**
		 * Handshake completed. TNCS Server recommended that no network access be
		 * allowed.
		 */

		HSB_SESSION_STATE_DECIDED_DENIED(4),

		/**
		 * About to delete network connection. Remove all associated state.
		 */

		HSB_SESSION_STATE_DELETE(5),
		

		// HSB (not specified in RFC)
		/**
		 * Not information about network connection available. 
		 * Only internally used, if TNCC does not support information
		 * about the network connection. 
		 */
		HSB_SESSION_STATE_UNKNOWN(-1L);
	
		private long state;
		    
	    private DefaultImHandlerStateEnum(long state){
	        this.state = state;
	    }

	    public long state(){
	        return this.state;
	    }
}
