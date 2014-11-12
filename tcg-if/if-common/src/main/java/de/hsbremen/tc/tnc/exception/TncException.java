package de.hsbremen.tc.tnc.exception;

import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;

public class TncException extends ComprehensibleException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 230606945788180582L;

	/**
	 * Result code.
	 */
    private final TncExceptionCodeEnum mResultCode;

    /**
     * Constructs a fully-specified <code>TNCException</code> 
     * from a given client exception object.  
     */
    public TncException(org.trustedcomputinggroup.tnc.ifimc.TNCException exception) {
    	this((exception.getMessage() == null || exception.getMessage().isEmpty()) ? "No message was specified." : exception.getMessage(),exception, TncExceptionCodeEnum.fromResult(exception.getResultCode()));
    }
    
    /**
     * Constructs a fully-specified <code>TNCException</code> 
     * from a given server exception object.  
     */
    public TncException(org.trustedcomputinggroup.tnc.ifimv.TNCException exception) {
        this((exception.getMessage() == null || exception.getMessage().isEmpty()) ? "No message was specified." : exception.getMessage(),exception, TncExceptionCodeEnum.fromResult(exception.getResultCode()));
    }
    
    /**
     * Constructs a fully-specified <code>TNCException</code> 
     * object.  
     */
    public TncException(String s ,TncExceptionCodeEnum resultCode, Object... reasons) {
        super(s,reasons);
        this.mResultCode = resultCode;
    }
    
    /**
     * Constructs a fully-specified <code>TNCException</code> 
     * object.  
     *
     */
    public TncException(String s,Throwable e, TncExceptionCodeEnum resultCode, Object... reasons) {
        super(s,e,reasons);
        this.mResultCode = resultCode;
    }
 
    /**
     * Retrieves the TNC result code
     * for this <code>TNCException</code> object.
     *
     * @return the TNC result code
     */
    public TncExceptionCodeEnum getResultCode() { 
    	return mResultCode; 
    }
}
