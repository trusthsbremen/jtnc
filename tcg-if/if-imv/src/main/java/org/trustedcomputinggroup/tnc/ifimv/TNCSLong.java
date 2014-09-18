package org.trustedcomputinggroup.tnc.ifimv;

import org.trustedcomputinggroup.tnc.TNCException;
/**
 * This interface can be implemented by an object that already supports 
 * the TNCS interface to indicate that this object is a TNCS that 
 * supports reportMessageTypesLong.  To check whether an object 
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
public interface TNCSLong {

	/**
	 * A call to this method is used to inform a TNCS about the set of
	 * message types that the IMV wishes to receive. This function 
	 * supports long message types, unlike reportMessageTypes.
	 * <p>
	 * Often, the IMV will call this method from the IMV's initialize 
	 * method. A list of Vendor IDs is contained in the supportedVendorIDs
	 * parameter and a list of message subtypes is contained in the 
	 * supportedSubtypes parameter. Both lists must have exactly the 
	 * same number of entries. Either parameter may be null to represent
	 * no message types, which is equivalent to a zero length list. 
	 * The values in the supportedVendorIDs list and the 
	 * supportedSubtypes list are matched pairs that represent the 
	 * (Vendor ID, Message Subtype) pairs that the IMV is able to receive.
	 * <p>
	 * This method is optional and is not supported by all TNCSs. IMVs 
	 * can easily determine which TNCSs support this method by checking
	 * if they implement the TNCSLong interface. IMVs should recognize 
	 * that many TNCSs do not support long types and therefore will 
	 * not support this method. In those cases, IMVs should be prepared
	 * to use the reportMessageTypes function. Simple IMVs that do not 
	 * wish to send messages with long subtypes need not implement 
	 * this function.
	 * <p>
	 * The TNC Server MUST NOT ever modify the list of message types 
	 * and MUST NOT access this list after the reportMessageTypesLong() 
	 * method has returned. Generally, the TNC Server will copy the 
	 * contents of this before returning from this method. TNC Servers 
	 * MUST support any message type. 
	 * <p>
	 * If an IMV requests a message type whose vendor ID is 
	 * TNC_VENDORID_ANY and whose subtype is TNC_SUBTYPE_ANY it will 
	 * receive all messages with any message type. If an IMC requests 
	 * a message type whose vendor ID is not TNC_VENDORID_ANY and 
	 * whose subtype is TNC_SUBTYPE_ANY, it will receive all messages 
	 * with the specified vendor ID and any subtype. If an IMV calls 
	 * the TNCS's reportMessageTypes or reportMessageTypesLong method 
	 * more than once, the message type list supplied in the latest 
	 * call supplants the message type lists supplied in earlier calls. 
	 * <p>
	 * @param imv the IMV reporting its message types
	 * @param supportedVendorIDs the list of Vendor IDs the IMV wishes 
	 *		  to receive
	 * @param supportedSubtypes the list of message subtypes the IMV 
	 *		  wishes to receive
	 * 
	 * @throws TNCException if a TNC error occurs
	 */
	void reportMessageTypesLong(IMV	imv,
								long[] supportedVendorIDs,
								long[] supportedSubtypes) throws TNCException;

	/**
	 * An IMV calls this method to reserve an additional IMV ID for 
	 * itself. This method is optional. The TNCS is not required to 
	 * implement it but if it implements the TNCSLong interface then 
	 * it MUST implement this method. Since this method was not 
	 * included in IF-IMV 1.2, many TNCSs do not support it. IMVs MUST 
	 * work properly if a TNCS does not implement this method.
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
	 * have multiple IMV IDs so that an IMC can send follow-up messages
	 * to the right IMV.
	 * <p>
	 * An IMV can call this function as many times it wishes. The TNCS
	 * SHOULD return a unique value every time. However, the TNCS may 
	 * have a maximum number of additional IMV IDs that it supports. 
	 * In that case the TNCS SHOULD return a TNC_RESULT_OTHER error if
	 * an IMV attempts to exceed this maximum. The TNCS is not 
	 * required to reserve IMV IDs in a specific order.
	 * <p>
	 * @param imv the IMV reporting its message types
	 * 
	 * @throws TNCException if a TNC error occurs
	 */
	long reserveAdditionalIMVID(IMV	imv) throws TNCException;
}
