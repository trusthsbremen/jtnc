package org.trustedcomputinggroup.tnc.ifimv;

import org.trustedcomputinggroup.tnc.TNCException;
/**
 * This interface can be implemented by an object that already supports
 * the IMV interface to indicate that this object is an IMV that 
 * supports receiveMessageLong.  To check whether an object implements
 * this interface use the <code>instanceof</code> operator. If so, 
 * cast the object to the interface type and use the methods in this 
 * interface.
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
public interface IMVLong {

	/**
	 * The TNC Server calls this method to deliver a message to the 
	 * IMV. This method provides several features that receiveMessage 
	 * does not: longer (32 bit) vendor ID and message subtype fields,
	 * a messageFlags field, and the ability to specify a source IMC 
	 * ID and destination IMV ID.
	 * <p>
	 * The message is contained in the buffer referenced by message. 
	 * This parameter may be null to represent an empty message. The 
	 * type of the message is indicated by the messageVendorID and 
	 * messageSubtype parameters. The message must be from an IMC (or 
	 * a TNCC or other party acting as an IMC). Any flags associated 
	 * with the message are included in the messageFlags parameter. 
	 * The sourceIMCID and destinationIMVID parameters indicate the 
	 * IMC ID of the IMC that sent this message (if available) and the
	 * IMV ID of the intended recipient (if the EXCL flag is set) or 
	 * the IMV ID in response to whose message or messages this message
	 * was sent. If the EXCL flag is set, destinationIMVID MUST be 
	 * either the primary IMV ID for this IMV (matching the value of 
	 * the TNC_AttributeID_PRIMARY_IMV_ID attribute) or an additional 
	 * IMV ID reserved when the IMV requested the TNCS to do so by 
	 * calling reserveAdditionalIMVID. If the EXCL flag is not set, 
	 * then destinationIMVID MAY be set to the wild card TNC_IMVID_ANY.
	 * <p>
	 * If an IF-TNCCS protocol that support long types or exclusive 
	 * delivery is used for a connection and the IMV and TNCS implement
	 * receiveMessageLong, the TNCS SHOULD use this method to deliver
	 * messages instead of using receiveMessage. However, if the IMV 
	 * does not implement receiveMessageLong, the TNCS SHOULD use 
	 * receiveMessage instead. Messages whose vendor ID or message 
	 * subtype is too long to be represented in the parameters 
	 * supported by receiveMessage MUST NOT be delivered to an IMV 
	 * that does not support receiveMessageLong. This should be fine 
	 * since the IMV in question wouldn't have the ability to process 
	 * such messages. Also, the IMV probably calls reportMessageTypes 
	 * instead of reportMessageTypesLong so it couldn't have expressed
	 * an interest in messages with long types except via wild cards. 
	 * Messages with flags or source IMC IDs can be handled fine using
	 * the receiveMessage method.
	 * <p>
	 * The IMV SHOULD send any IMC-IMV messages it wants to send as 
	 * soon as possible after this method is called and then return 
	 * from this method to indicate that it is finished sending 
	 * messages in response to this message.
	 * <p>
	 * As with all IMV methods, the IMV SHOULD NOT wait a long time 
	 * before returning from receiveMessageLong. To do otherwise would
	 * risk delaying the handshake indefinitely. A long delay might 
	 * frustrate users or exceed network timeouts (PDP, PEP or 
	 * otherwise).
	 * <p>
	 * IMVs are not required to implement this method but any IMV that
	 * implements the IMVLong interface MUST implement this method. 
	 * An IMV should implement this method if it wants to receive 
	 * messages with long types or flags or source IMC IDs. Simple 
	 * IMVs need not implement this method. Since this method was not 
	 * included in IF-IMV 1.2, many IMVs do not implement it. TNCSs
	 * MUST work properly if an IMV does not implement this method. 
	 * Likewise, many TNCSs do not implement this method so IMVs MUST
	 * work properly if a TNCS does not implement this method.
	 * <p>
	 * The IMV MUST NOT ever modify the buffer contents and MUST NOT 
	 * access the buffer after receiveMessageLong has returned. If the
	 * IMV wants to retain the message, it should copy it before 
	 * returning from receiveMessageLong.
	 * <p>
	 * In the messageVendorID and messageSubtype parameters, the TNCS 
	 * MUST pass the vendor ID and message subtype of the message. 
	 * These values MUST match one of the values previously supplied 
	 * by the IMV to the TNCS in the IMV’s most recent call to 
	 * reportMessageTypes or reportMessageTypesLong. The TNCS MUST NOT
	 * specify a message type whose vendor ID is 0xffffff or whose 
	 * subtype is 0xff. These values are reserved for use as wild 
	 * cards, as described in section 3.9.1. IMVs MAY check these 
	 * parameters to make sure they are valid and throw an exception 
	 * if not, but IMVs are not required to make these checks.
	 * <p>
	 * Any flags associated with the message are included in the 
	 * messageFlags parameter. This may include the EXCL flag but the
	 * TNCS MUST process that flag itself to ensure that a message 
	 * with this flag set is only delivered to the intended recipient.
	 * <p>
	 * @param c the IMV connection object
	 * @param messageFlags flags associated with the message
	 * @param messageVendorID vendor ID associated with the message
	 * @param messageSubtype message subtype associated with the message
	 * @param message the message to be delivered
	 * @param sourceIMCID source IMC ID for the message
	 * @param destinationIMVID destination IMV ID for message
	 * 
	 * @throws TNCException if a TNC error occurs
	 */
	void receiveMessageLong(IMVConnectionLong c,
							long messageFlags,
							long messageVendorID,
							long messageSubtype,
							byte[] message,
							long sourceIMCID,
							long destinationIMVID) throws TNCException;
}
