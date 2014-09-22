package org.trustedcomputinggroup.tnc.ifimc;

/**
 * This interface can be implemented by an object that already supports 
 * the TNCC interface to indicate that this object is a TNCC that 
 * supports reportMethodTypesLong.  To check whether an object 
 * implements this interface use the <code>instanceof</code> operator.
 * If so, cast the object to the interface type and use the methods in
 * this interface.
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
public interface TNCCLong {

    /**
     * A call to this method is used to inform a TNCC about the set of
	 * message types that the IMC wishes to receive. This function 
	 * supports long message types, unlike reportMessageTypes.
     * <p>
     * Often, the IMC will call this method from the IMC's initialize
	 * method. A list of Vendor IDs is contained in the 
	 * supportedVendorIDs parameter and a list of message subtypes is 
	 * contained in the supportedSubtypes parameter. Both lists must 
	 * have exactly the same number of entries. Either parameter may 
	 * be null to represent no message types, which is equivalent to 
	 * a zero length list. The values in the supportedVendorIDs list 
	 * and the supportedSubtypes list are matched pairs that represent 
	 * the (Vendor ID, Message Subtype) pairs that the IMC is able to 
	 * receive.
     * <p>
     * This method is optional and is not supported by all TNCCs. IMCs
	 * can easily determine which TNCCs support this method by checking
	 * if they implement the TNCCLong interface. IMCs should recognize
	 * that many TNCCs do not support long types and therefore will 
	 * not support this method. In those cases, IMCs should be prepared
	 * to use the reportMessageTypes function. Simple IMCs that do not
	 * receive messages need not call this function.
     * <p>
     * The TNC Client MUST NOT ever modify the list of message types 
	 * and MUST NOT access this list after the reportMessageTypesLong()
	 * method has returned. Generally, the TNC Client will copy the 
	 * contents of this list before returning from this method. TNC 
	 * Clients MUST support any message type. 
     * <p>
     * If an IMC requests a message type whose vendor ID is 
	 * TNC_VENDORID_ANY and whose subtype is TNC_SUBTYPE_ANY it will 
	 * receive all messages with any message type, except for messages
	 * marked for exclusive delivery to another IMC. If an IMC requests
	 * a message type whose vendor ID is not TNC_VENDORID_ANY and whose
	 * subtype is TNC_SUBTYPE_ANY, it will receive all messages with 
	 * the specified vendor ID and any subtype, except for messages 
	 * marked for exclusive delivery to another IMC. If an IMC calls 
	 * the TNCC's reportMessageTypes or reportMessageTypesLong method 
	 * more than once, the message type list supplied in the latest 
	 * call supplants the message type lists supplied in earlier calls. 
     * <p>
     * @param imc the IMC reporting it message types
     * @param supportedVendorIDs the list of Vendor IDs the IMC wishes 
	 *		  to receive
     * @param supportedSubtypes the list of message subtypes the IMC 
	 *		  wishes to receive
     *
     * @exception TNCException if a TNC error occurs
     */
	public void reportMessageTypesLong(IMC imc,
									   long[] supportedVendorIDs,
									   long[] supportedSubtypes) throws TNCException;

    /**
     * An IMC calls this method to reserve an additional IMC ID for 
	 * itself. This method is optional. The TNCC is not required to 
	 * implement it but if it implements the TNCCLong interface then 
	 * it MUST implement this method. Since this method was not 
	 * included in IF-IMC 1.2, many TNCCs do not support it. IMCs MUST
	 * work properly if a TNCC does not implement this method.
     * <p>
     * When the IF-TNCCS 2.0 [10] (and PB-TNC [17]) protocols are 
	 * carrying an IF-M message, the IF-TNCCS protocol includes a 
	 * header (TNCCS-IF-M-Message) housing several fields important to
	 * the processing of a received IF-M message. The IF-M Vendor ID 
	 * and IF-M Subtype described in the IF-TNCCS specification are 
	 * used by the TNCC and TNCS to route messages to IMC and IMV that
	 * have registered an interest in receiving messages for a 
	 * particular type of component. Also present in the 
	 * TNCCS-IF-M-Message header is a pair of fields that identify the
	 * IMC and IMV involved in the message exchange. The IMC and IMV 
	 * Identifier fields are used for performing exclusive delivery of
	 * messages and as an indicator for correlation of received 
	 * attributes. See the IF-TNCCS 2.0 protocol specification for 
	 * more information on these fields.
     * <p>
     * Correlation of attributes is necessary when an IMC sends 
	 * attributes describing multiple different implementations of a 
	 * single type of component during an assessment, so the recipient
	 * IMV(s) need to be able to determine which attributes are 
	 * describing the same implementation.
     * <p>
     * For example, a single IMC might report attributes about two 
	 * installed VPN implementations on the endpoint. Because the 
	 * individual attributes (except the Product Information attribute)
	 * do not include an indication of which VPN product they are 
	 * describing, the recipient IMV needs something to perform this 
	 * correlation.Therefore, for this example, the single VPN IMC 
	 * would need to obtain two IMC Identifiers from the TNC Client 
	 * and consistently use one with each of the VPN implementations 
	 * reported during an assessment. The VPN IMC would group all the 
	 * attributes associated with a particular VPN implementation into
	 * a single IF-M message and send the message using the IMC 
	 * Identifier it designates as going with the particular 
	 * implementation. This approach allows the recipient IMV to 
	 * recognize when attributes in future assessment messages also 
	 * describe the same VPN implementation and to direct follow-up 
	 * messages to the right IMC. Similarly, a single IMV may need to 
	 * have multiple IMV IDs so that an IMC can send follow-up 
	 * messages to the right IMV.
     * <p>
     * An IMC can call this function as many times it wishes. The TNCC
	 * SHOULD return a unique value every time. However, the TNCC may 
	 * have a maximum number of additional IMC IDs that it supports. 
	 * In that case the TNCC SHOULD throw a TNCException with result 
	 * code TNC_RESULT_OTHER if an IMC attempts to exceed this maximum.
	 * The TNCC is not required to reserve IMC IDs in a specific order.
     * <p>
     * @param imc the IMC requesting additional IMC IDs
     *
     * @exception TNCException if a TNC error occurs
     */
	public long reserveAdditionalIMCID(IMC imc) throws TNCException;
}
