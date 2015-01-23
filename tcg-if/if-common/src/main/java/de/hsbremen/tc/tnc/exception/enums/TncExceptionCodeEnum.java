package de.hsbremen.tc.tnc.exception.enums;

public enum TncExceptionCodeEnum {

	/**
     * The IMC or IMV's <code>initialize</code> method has not been called.
     */
    
    TNC_RESULT_NOT_INITIALIZED      (1L),

    /**
     * The IMC or IMV's <code>initialize</code> method was called twice
     * without a call to the IMC or IMV's <code>terminate</code> method.
     */
     
    TNC_RESULT_ALREADY_INITIALIZED  (2L),
    
    /**
     * TNCC or TNCS cannot attempt handshake retry.
     */
    TNC_RESULT_CANT_RETRY           (4L),
    
    /**
     * TNCC or TNCS refuses to attempt handshake retry.
     */
    
    TNC_RESULT_WONT_RETRY           (5L),
    
    /**
     * Method parameter is not valid.
     */
    
    TNC_RESULT_INVALID_PARAMETER    (6L),
    
    /**
     * IMC or IMV cannot respond now.
     */
    
    TNC_RESULT_CANT_RESPOND         (7L),
    
    /**
     * Illegal operation attempted.
     */
    TNC_RESULT_ILLEGAL_OPERATION    (8L),
    
    /**
     * Unspecified error.
     */
    TNC_RESULT_OTHER                (9L),
    
    /**
     * Unspecified fatal error.
     */
    TNC_RESULT_FATAL                (10L),

    /**
     * Exceeded maximum round trips supported by the underlying protocol.
     */
    TNC_RESULT_EXCEEDED_MAX_ROUND_TRIPS (0x00559700L),

    /**
     * Exceeded maximum message size supported by the underlying protocol.
     */
    TNC_RESULT_EXCEEDED_MAX_MESSAGE_SIZE (0x00559701L),

    /**
     * Connection does not support long message types.
     */
    TNC_RESULT_NO_LONG_MESSAGE_TYPES (0x00559702L),

    /**
     * Connection does not support SOH.
     */
    TNC_RESULT_NO_SOH_SUPPORT (0x00559703L);

    private final long result;
    
    private TncExceptionCodeEnum(long result){
    	this.result = result;
    }

	public long result() {
		return result;
	}
    
    public static TncExceptionCodeEnum fromResult(long result){
        
        if( result == TNC_RESULT_NOT_INITIALIZED.result ) {
        	return TNC_RESULT_NOT_INITIALIZED; 
        }
         
        if( result == TNC_RESULT_ALREADY_INITIALIZED.result ) {
        	return TNC_RESULT_ALREADY_INITIALIZED; 
        }

        if( result == TNC_RESULT_CANT_RETRY.result ) {
        	return TNC_RESULT_CANT_RETRY; 
        }
        
        if( result == TNC_RESULT_WONT_RETRY.result ) {
        	return TNC_RESULT_WONT_RETRY; 
        }
        
        if( result == TNC_RESULT_INVALID_PARAMETER.result ) {
        	return TNC_RESULT_INVALID_PARAMETER; 
        }
        
        if( result == TNC_RESULT_CANT_RESPOND.result ) {
        	return TNC_RESULT_CANT_RESPOND; 
        }
        
        if( result == TNC_RESULT_ILLEGAL_OPERATION.result ) {
        	return TNC_RESULT_ILLEGAL_OPERATION; 
        }
        
        if( result == TNC_RESULT_FATAL.result ) {
        	return TNC_RESULT_FATAL; 
        }

        if( result == TNC_RESULT_EXCEEDED_MAX_ROUND_TRIPS.result ) {
        	return TNC_RESULT_EXCEEDED_MAX_ROUND_TRIPS; 
        }

        if( result == TNC_RESULT_EXCEEDED_MAX_MESSAGE_SIZE.result ) {
        	return TNC_RESULT_EXCEEDED_MAX_MESSAGE_SIZE;
        }

        if( result == TNC_RESULT_NO_LONG_MESSAGE_TYPES.result ) {
        	return TNC_RESULT_NO_LONG_MESSAGE_TYPES; 
        }

        if( result == TNC_RESULT_NO_SOH_SUPPORT.result ) {
        	return TNC_RESULT_NO_SOH_SUPPORT;
        }
    	
    	return TNC_RESULT_OTHER;
    }
    
    
}
