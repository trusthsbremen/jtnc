package org.trustedcomputinggroup.tnc.ifimc;

import org.trustedcomputinggroup.tnc.TNCException;
/**
 * These methods can be implemented by an object in the IMC package to
 * get or set attributes.  This interface also includes a collection 
 * of common IMCConnection attributes to be used by the IMC package. 
 * To check whether an object in the IMC package implements this 
 * interface use the <code>instanceof</code> operator. If so, cast the
 * object to the interface type and use the new methods and fields.
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
public interface AttributeSupport {

    /**
     * Preferred human-readable language for a TNCS (type String, 
	 * may get from a IMCConnection) 
     */
    public static final long TNC_ATTRIBUTEID_PREFERRED_LANGUAGE = 1;
    
    /**
     * Maximum round trips supported by the underlying protocol 
	 * (type Integer, may get from an IMCConnection)
     */
    public static final long TNC_ATTRIBUTEID_MAX_ROUND_TRIPS = 0x00559700;

    /**
     * Maximum message size supported by the underlying protocol 
	 * (type Integer, may get from an IMCConnection)
     */
    public static final long TNC_ATTRIBUTEID_MAX_MESSAGE_SIZE = 0x00559701;

    /**
     * Diffie-Hellman Pre-Negotiation value provided by the underlying 
	 * protocol (type byte[], may get from an IMCConnection)
     */
    public static final long TNC_ATTRIBUTEID_DHPN_VALUE = 0x00559702;

    /**
     * Flag indicating if the connection supports long message types 
	 * (type boolean, may get from an IMCConnection) 
     */
    public static final long TNC_ATTRIBUTEID_HAS_LONG_TYPES = 0x00559703;

    /**
     * Flag indicating if the connection supports exclusive delivery 
	 * of messages (type boolean, may get from an IMCConnection) 
     */
    public static final long TNC_ATTRIBUTEID_HAS_EXCLUSIVE = 0x00559704;

    /**
     * Flag indicating if the connection supports SOH functions 
	 * (type boolean, may get from an IMCConnection) 
     */
    public static final long TNC_ATTRIBUTEID_HAS_SOH = 0x00559705;

    /**
     * Contents of SOHR (type byte [], may get from an IMCConnection)
     */
    public static final long TNC_ATTRIBUTEID_SOHR = 0x00559708;

    /**
     * Contents of SSOHR (type byte [], may get from an IMCConnection)
     */
    public static final long TNC_ATTRIBUTEID_SSOHR = 0x00559709;

    /**
     * IF-TNCCS Protocol Name (type String, may get from 
	 * an IMCConnection)
     */
    public static final long TNC_ATTRIBUTEID_IFTNCCS_PROTOCOL = 0x0055970A;

    /**
     * IF-TNCCS Protocol Version (type String, may get from 
	 * an IMCConnection)
     */
    public static final long TNC_ATTRIBUTEID_IFTNCCS_VERSION = 0x0055970B;

    /**
     * IF-T Protocol Name (type String, may get from 
	 * an IMCConnection) 
     */
    public static final long TNC_ATTRIBUTEID_IFT_PROTOCOL = 0x0055970C;

    /**
     * IF-T Protocol Version (type String, may get from 
	 * an IMCConnection)
     */
    public static final long TNC_ATTRIBUTEID_IFT_VERSION = 0x0055970D;

    /**
     * TLS-Unique value provided by the underlying protocol (type 
	 * byte[], may get from a IMCConnection)
     */
    public static final long TNC_ATTRIBUTEID_TLS_UNIQUE = 0x0055970E;

    /**
     * Flag to indicate if IMC supports TNCS sending first message 
	 * (type boolean, may get from a IMC) 
     */
    public static final long TNC_ATTRIBUTEID_IMC_SPTS_TNCS1 = 0x0055970F;

    /**
     * IMC identifier assigned by the TNCC when the TNCC loaded 
	 * this IMC
     */
    public static final long TNC_ATTRIBUTEID_PRIMARY_IMC_ID = 0x00559711;

    /**
	 * A call to this method gets the value of the attribute 
	 * identified by attributeID for an object in the IMC package 
	 * which implements this interface. 
	 * <p>
	 * This function is optional. The objects in the IMC package are 
	 * not required to implement it this function. If this function is
	 * not implemented it MUST throw an UnsupportedOperationException.
	 * Objects in the IMC package calling this function MUST work 
	 * properly if this function is not implemented. 
	 * <p>
	 * Objects of the IMC package MUST pass a standard or 
	 * vendor-specific attribute ID as the attributeID parameter when 
	 * calling this method.  If the called object does not recognize 
	 * the attribute ID, it SHOULD throw a TNCException with the 
	 * TNC_RESULT_INVALID_PARAMETER result code. If the called object 
	 * recognizes the attribute ID but does not have an attribute 
	 * value for the requested attribute ID, it SHOULD also throw a 
	 * TNCException with the TNC_RESULT_INVALID_PARAMETER result code. 
	 * <p>
	 * The return value is an Object that represents the attribute 
	 * value requested. The calling object must cast this Object to 
	 * the class documented in the description of that specific 
	 * attribute to get the desired value. All Objects returned by 
	 * this method SHOULD be immutable. 
	 * <p>
     * @param attributeID the attribute ID of the desired attribute
	 * @return the attribute value
     *
     * @exception TNCException if a TNC error occurs
     */
	public Object getAttribute(long attributeID) throws TNCException;

    /**
	 * An IMC calls this method to set the value of the attribute 
	 * identified by attributeID an object in the IMC package which 
	 * implements this interface. 
	 * <p>
	 * This function is optional. The objects in the IMC package are 
	 * not required to implement it this function. If this function is
	 * not implemented it MUST throw an UnsupportedOperationException.
	 * Objects in the IMC package calling this function MUST work 
	 * properly if this function is not implemented. 
	 * <p>
	 * Objects of the IMC package MUST pass a standard or vendor-specific
	 * attribute ID as the attributeID parameter when calling this 
	 * method.  If the called object does not recognize the attribute 
	 * ID, it SHOULD throw a TNCException with the 
	 * TNC_RESULT_INVALID_PARAMETER result code. If the called object
	 * recognizes the attribute ID but does not have an attribute 
	 * value for the requested attribute ID , it SHOULD also throw a 
	 * TNCException with the TNC_RESULT_INVALID_PARAMETER result code. 
	 * <p>
	 * For the attributeValue parameter, the caller MUST pass an 
	 * Object that represents the new attribute value (or null if 
	 * permitted for the specified attribute). This Object must 
	 * actually be an instance of the class documented in the 
	 * description of the specified attribute. The Object SHOULD be 
	 * immutable. If the called object has any uncertainty about it 
	 * SHOULD copy the object. The called object MAY check the Object 
	 * and throw a TNCException if it is not a valid value for the 
	 * specified attribute. 
	 * <p>
     * @param attributeID the attribute ID of the attribute to be set
     * @param attributeValue the new value to be set for this attribute
     *
     * @exception TNCException if a TNC error occurs
     */
	public void setAttribute(long attributeID, 
							 Object attributeValue) throws TNCException;
}
