package org.trustedcomputinggroup.tnc.ifimv;
/**
 * An exception that provides information on IF-IMC/IF-IMV errors.  This
 * exception class which wraps the result codes defined in the IF-IMC
 * and IF-IMV Abstract API MUST be implemented by all TNCCs and TNCSs.
 * <p>
 * Each method in the IF-IMC/IF-IMV API throws an exception to 
 * indicate reason for failure. IMCs, IMVs, TNCCs and TNCSs MUST
 * be prepared for any method to throw an TNCException.
 * <p>
 * This class defines a set of standard result codes. Vendor-specific
 * result codes may be used but must be constructed as described in
 * the abstract API. Any unknown result code SHOULD be treated as
 * equivalent to TNC_RESULT_OTHER. 
 * <p>
 * If an IMC or IMV method returns TNC_RESULT_FATAL, then the IMC
 * or IMV has encountered a permanent error. The TNCC or TNCS SHOULD call
 * the IMC or IMV's <code>terminate</code> method as soon as possible.
 * The TNCC or TNCS MAY then try to reinitialize the IMC or IMV with the
 * IMC or IMV's <code>initialize</code> method or try other measures.
 * <p>
 * If a TNCC or TNCS method returns TNC_RESULT_FATAL, then the
 * TNCC or TNCS has encountered a permanent error.
 */
/*
 * Copyright(c) 2005-2012, Trusted Computing Group, Inc. All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the  
 *   distribution.
 * - Neither the name of the Trusted Computing Group nor the names of
 *   its contributors may be used to endorse or promote products 
 *   derived from this software without specific prior written 
 *   permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS 
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * Contact the Trusted Computing Group at 
 * admin@trustedcomputinggroup.org for information on specification 
 * licensing through membership agreements.
 *
 * Any marks and brands contained herein are the property of their 
 * respective owners.
 *
 */
public class TNCException extends Exception {
	
    /**
	 *
	 */
	private static final long serialVersionUID = -1701914844092560832L;

	/**
     * The IMC or IMV's <code>initialize</code> method has not been called.
     */
    
    public static final long TNC_RESULT_NOT_INITIALIZED      = 1;

    /**
     * The IMC or IMV's <code>initialize</code> method was called twice
     * without a call to the IMC or IMV's <code>terminate</code> method.
     */
     
    public static final long TNC_RESULT_ALREADY_INITIALIZED  = 2;
    
    /**
     * TNCC or TNCS cannot attempt handshake retry.
     */
    public static final long TNC_RESULT_CANT_RETRY           = 4;
    
    /**
     * TNCC or TNCS refuses to attempt handshake retry.
     */
    
    public static final long TNC_RESULT_WONT_RETRY           = 5;
    
    /**
     * Method parameter is not valid.
     */
    
    public static final long TNC_RESULT_INVALID_PARAMETER    = 6;
    
    /**
     * IMC or IMV cannot respond now.
     */
    
    public static final long TNC_RESULT_CANT_RESPOND         = 7;
    
    /**
     * Illegal operation attempted.
     */
    public static final long TNC_RESULT_ILLEGAL_OPERATION    = 8;
    
    /**
     * Unspecified error.
     */
    public static final long TNC_RESULT_OTHER                = 9;
    
    /**
     * Unspecified fatal error.
     */
    public static final long TNC_RESULT_FATAL                = 10;

    /**
     * Exceeded maximum round trips supported by the underlying protocol.
     */
    public static final long TNC_RESULT_EXCEEDED_MAX_ROUND_TRIPS = 0x00559700;

    /**
     * Exceeded maximum message size supported by the underlying protocol.
     */
    public static final long TNC_RESULT_EXCEEDED_MAX_MESSAGE_SIZE = 0x00559701;

    /**
     * Connection does not support long message types.
     */
    public static final long TNC_RESULT_NO_LONG_MESSAGE_TYPES = 0x00559702;

    /**
     * Connection does not support SOH.
     */
    public static final long TNC_RESULT_NO_SOH_SUPPORT = 0x00559703;


    /**
     * Constructs a <code>TNCException</code> object;
     * the <code>resultCode</code> field defaults to TNC_RESULT_OTHER. */

    public TNCException() {
        super();
    }

    /**
     * Constructs a fully-specified <code>TNCException</code> 
     * object.  
     *
     * @param s a description of the exception 
     * @param resultCode TNC result code
     */

    public TNCException(String s, long resultCode) {
        super(s);
        mResultCode = resultCode;
    }
    /**
     * Retrieves the TNC result code
     * for this <code>TNCException</code> object.
     *
     * @return the TNC result code
     */

    public long getResultCode() { 
    	return mResultCode; 
    }

	/**
	 * @serial
	 */
    private long mResultCode = TNC_RESULT_OTHER;

}
