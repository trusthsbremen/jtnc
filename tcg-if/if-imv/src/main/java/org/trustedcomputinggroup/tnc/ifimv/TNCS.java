package org.trustedcomputinggroup.tnc.ifimv;

import org.trustedcomputinggroup.tnc.TNCException;
/**
 * These methods are implemented by the TNC Server and 
 * called by the IMV.
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
public interface TNCS {

	/**
	 * An IMV calls this method to inform a TNCS about 
	 * the set of message types the IMV is able to receive. 
	 * Often, the IMV will call this method from the IMV's
	 * initialize method. 
	 * <p>
	 * A list of message types is contained in the 
	 * supportedTypes parameter. The supportedTypes 
	 * parameter may be null to represent no message types.
	 * <p>
	 * All TNC Servers MUST implement this method. 
	 * The TNC Server MUST NOT ever modify the list of 
	 * message types and MUST NOT access this list after the
	 * TNCS' reportMessageTypes method has returned. 
	 * Generally, the TNC Server will copy the contents of 
	 * this list before returning from this method. 
	 * TNC Servers MUST support any message type.
	 * <p>
	 * Note that although all TNC Servers must implement 
	 * this method, some IMVs may never call it if 
	 * they don't support receiving any message types. 
	 * This is acceptable. In such a case, the TNC 
	 * Server MUST NOT deliver any messages to the IMV.
	 * <p>
	 * If an IMV requests a message type whose 
	 * vendor ID is TNC_VENDORID_ANY and whose subtype 
	 * is TNC_SUBTYPE_ANY it will receive all messages 
	 * with any message type. This message type is 
	 * 0xffffffff. If an IMV requests a message type 
	 * whose vendor ID is NOT TNC_VENDORID_ANY and 
	 * whose subtype is TNC_SUBTYPE_ANY, it will 
	 * receive all messages with the specified vendor 
	 * ID and any subtype. If an IMV calls 
	 * the TNCS' reportMessageTypes method more than 
	 * once, the message type list supplied in the 
	 * latest call supplants the message type lists 
	 * supplied in earlier calls.
	 * <p>
	 * @param imv the IMV reporting its message types
	 * @param supportedTypes the message types the IMV wishes to receive
	 * 
	 * @throws TNCException
	 */
    public void reportMessageTypes(IMV imv, long [] supportedTypes) throws TNCException;
    
    /**
     * IMVs can call this method to ask a TNCS to 
     * retry an Integrity Check Handshake 
     * for all current network connections. The IMV MUST 
     * pass itself as the imv parameter and 
     * one of the handshake retry reasons listed in
     * IMVConnection as the reason parameter. 
     * <p>
     * TNCSs MAY check the parameters to make sure 
     * they are valid and throw an exception if not, 
     * but TNCSs are not required to make these 
     * checks. The reason parameter explains why the 
     * IMV is requesting a handshake retry. The TNCS MAY 
     * use this in deciding whether to attempt the 
     * handshake retry. As noted in the Abstract API, 
     * TNCSs are not required to honor IMV requests 
     * for handshake retry (especially since 
     * handshake retry may not be possible or 
     * may interrupt network connectivity). An 
     * IMV MAY call this method at any time, 
     * even if an Integrity Check Handshake is 
     * currently underway. This is useful if the 
     * IMV suddenly gets important information but 
     * has already finished its dialog with the 
     * IMC, for instance. As always, the TNCS 
     * is not required to honor the request for 
     * handshake retry.
     * <p>
     * If the TNCS cannot attempt the handshake retry,
     * it SHOULD throw a TNCException with result code
     * TNC_RESULT_CANT_RETRY. If the TNCS could attempt to retry 
     * the handshake but chooses not to, it SHOULD 
     * throw the TNC_RESULT_WONT_RETRY exception. If 
     * the TNCS intends to retry the handshake, it 
     * SHOULD throw a TNCException with result code
     * TNC_RESULT_WONT_RETRY.
     * The IMV MAY use this information in 
     * displaying diagnostic and progress messages.
     * <p>
     * @param imv IMV object
     * @param reason reason for retry handshake request
     * 
     * @throws TNCException
     */
    public void requestHandshakeRetry(IMV imv, long reason) throws TNCException;

    /**
     * An IMV calls this method to get the value of the attribute
     * identified by <code>attributeID</code> for this
     * <code>TNCS</code>.
     * <p>
     * This function is optional. The TNCS is not required to implement
     * it. If it is not implemented for this TNCS, it MUST
     * throw an UnsupportedOperationException. IMVs MUST work properly
     * if a TNCS does not implement this function.
     * <p>
     * The IMV MUST pass a standard or vendor-specific attribute ID as
     * the attributeID parameter. If the TNCS does not recognize the
     * attribute ID, it SHOULD throw a TNCException with the
     * TNC_RESULT_INVALID_PARAMETER result code. If the TNCS
     * recognizes the attribute ID but does not have an attribute
     * value for the requested attribute ID for this
     * <code>TNCS</code>, it SHOULD also throw a TNCException
     * with the TNC_RESULT_INVALID_PARAMETER result code.
     * <p>
     * The return value is an <code>Object</code> that represents the
     * attribute value requested. The IMV must cast this <code>Object</code>
     * to the class documented in the description of that specific
     * attribute to get the desired value. All <code>Object</code>s
     * returned by this method SHOULD be immutable.
     *
     * @param attributeID the attribute ID of the desired attribute
     * @return the attribute value
     * @throws TNCException
     */
    
    public Object getAttribute(long attributeID) throws TNCException;
    
    /**
     * An IMV calls this method to set the value of the attribute
     * identified by <code>attributeID</code> for this
     * <code>TNCS</code>.
     * <p>
     * This function is optional. The TNCS is not required to implement
     * it. If it is not implemented for this TNCS, it MUST
     * throw an UnsupportedOperationException. IMVs MUST work properly
     * if a TNCS does not implement this function.
     * <p>
     * The IMV MUST pass a standard or vendor-specific attribute ID as
     * the attributeID parameter. If the TNCS does not recognize the
     * attribute ID, it SHOULD throw a TNCException with the
     * TNC_RESULT_INVALID_PARAMETER result code. If the TNCS
     * recognizes the attribute ID but does not support setting
     * an attribute value for the requested attribute ID for this
     * <code>TNCS</code>, it SHOULD also throw a TNCException
     * with the TNC_RESULT_INVALID_PARAMETER result code.
     * <p>
     * For the <code>attributeValue</code> parameter, the IMV MUST pass
     * an <code>Object</code> that represents the new attribute value
     * (or <code>null</code> if permitted for the specified attribute).
     * This <code>Object</code> must actually be an instance of the
     * class documented in the description of the specified attribute.
     * The <code>Object</code> SHOULD be immutable. If the TNCS has
     * any uncertainty about it SHOULD copy the object. The TNCS MAY
     * check the <code>Object</code> and throw a <code>TNCException</code>
     * if it is not a valid value for the specified attribute.
     *
     * @param attributeID the attribute ID of the attribute to be set
     * @param attributeValue the new value to be set for this attribute
     * @throws TNCException
     */
    public void setAttribute(long attributeID, Object attributeValue) throws TNCException;
}
