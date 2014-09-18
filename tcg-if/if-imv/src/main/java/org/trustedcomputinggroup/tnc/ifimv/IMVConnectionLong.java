package org.trustedcomputinggroup.tnc.ifimv;

import org.trustedcomputinggroup.tnc.TNCException;
/**
 * This interface can be implemented by an object that already supports 
 * the IMVConnection interface to indicate that this object is an 
 * IMVConnection that supports sendMessageLong.  To check whether an 
 * object implements this interface use the <code>instanceof</code>
 * operator. If so, cast the object to the interface type and use the 
 * methods in this interface.
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
public interface IMVConnectionLong {

	/**
	 * An IMV calls this method to give a message to the TNCS for 
	 * delivery. This method provides several features that sendMessage
	 * does not: longer 32 bit vendor ID and message subtype fields, 
	 * a messageFlags field, and the ability to specify a source IMV 
	 * ID and a desired or exclusive destination IMC ID.
	 * <p>
	 * The message is contained in the buffer referenced by message. 
	 * This parameter may be null to represent an empty message. The 
	 * type of the message is indicated by the messageVendorID and 
	 * messageSubtype parameters. TNCSs MAY check these values to make
	 * sure they are valid and throw an exception if not, but TNCSs 
	 * are not required to make these checks.
	 * <p>
	 * The messageFlags parameter supports up to 32 flags that may be
	 * set pertaining to the message. At this time, only one flag has 
	 * been defined. The value 0x80000000 is known as the
	 * TNC_MESSAGE_FLAGS_EXCLUSIVE flag ("the EXCL flag", for short). 
	 * If this bit is set, the sending IMV is requesting that the 
	 * message only be delivered to the IMC designated by the 
	 * destinationIMCID parameter. If the bit is cleared, the message 
	 * is to be delivered to any IMC that has indicated an interest in
	 * the message type. Other flags may be defined in future versions
	 * of this specification. Until that time, IMVs MUST leave all 
	 * these flags cleared. Some connections and/or TNCSs may not 
	 * support the EXCL flag. To determine whether a particular TNCS 
	 * and connection supports this flag, the IMV should get the Has 
	 * Exclusive attribute for that connection. If the IMV sets a flag
	 * that is not supported by the TNCS or the connection, the TNCS 
	 * SHOULD throw a TNCException with a TNC_RESULT_INVALID_PARAMETER
	 * result.
	 * <p>
	 * The IMV MUST set the sourceIMVID parameter to either the primary
	 * IMV ID queried by IMV using the TNC_AttributeID_PRIMARY_IMV_ID
	 * attribute or an additional IMV ID reserved when IMV requests 
	 * the TNCS to do so by calling reserveAdditionalIMVID. This IMV ID
	 * will be used as the sourceIMVID for protocols that support 
	 * sending this field along with the message.
	 * <p>
	 * If the EXCL flag is set, the IMV MUST set the destinationIMCID 
	 * parameter to indicate which IMC should receive the message being
	 * sent. The IMV should place into this parameter a value which it
	 * has obtained using the sourceIMCID parameter for messages 
	 * previously delivered to the IMV on this connection. The IMV MAY
	 * set the destinationIMCID parameter to a specific IMC ID even 
	 * when the EXCL flag is not set. This indicates that the message 
	 * being sent is in response to a message received from the IMC 
	 * indicated in the destinationIMCID parameter. If the IMV does 
	 * not know the IMC ID of the recipient IMC, the IMV MUST use the 
	 * TNC_IMCID_ANY wild card for the destinationIMCID parameter and 
	 * MUST NOT set the EXCL flag. In such case, the message is 
	 * delivered to all IMCs that have expressed interest in receiving
	 * such messages. This may happen when the TNCS starts the 
	 * integrity handshake and the IMV does not know the IMC IDs of 
	 * the recipient IMCs.
	 * <p>
	 * TNCSs are not required to implement this method but any TNCS 
	 * that implements the IMVConnectionLong interface MUST implement 
	 * this method. Since this method was not included in IF-IMV 1.2, 
	 * many TNCSs do not implement it. IMVs MUST work properly if a 
	 * TNCS does not implement this method. The IMV is never required 
	 * to call this method. The TNCS MUST work with IMVs that don’t 
	 * call this method. The IMV also SHOULD check the Has Long 
	 * attribute for a given connection to determine whether this 
	 * connection supports long types before calling this method 
	 * for that connection. If the connection does not support long 
	 * types, the IMV MAY call this method (if implemented) but MUST 
	 * NOT use a messageVendorID greater than 0xffffff or a 
	 * messageSubtype greater than 0xff.  If the IMV does so, the TNCS
	 * SHOULD throw a TNC_RESULT_NO_LONG_TYPES exception.
	 * <p>
	 * The TNC Server MUST NOT ever modify the buffer contents and 
	 * MUST NOT access the buffer after sendMessageLong has returned. 
	 * The TNC Server will typically copy the message out of the 
	 * buffer, queue it up for delivery, and return from this method.
	 * <p>
	 * The IMV MUST NOT call this method unless it has received a call
	 * to beginHandshake, receiveMessage, receiveMessageSOH, 
	 * receiveMessageLong, or batchEnding for this connection and the 
	 * IMV has not yet returned from that method. If the IMV violates 
	 * this prohibition, the TNCS SHOULD throw 
	 * TNC_RESULT_ILLEGAL_OPERATION exception. If an IMV really wants 
	 * to communicate with an IMC at another time, it should call 
	 * requestHandshakeRetry.
	 * <p>
	 * Note that a TNCC or TNCS MAY cut off IMC-IMV communications at
	 * any time for any reason, including limited support for long 
	 * conversations in underlying protocols, user or administrator 
	 * intervention, or policy. If this happens, the TNCS will throw
	 * TNC_RESULT_ILLEGAL_OPERATION exception from sendMessageLong. 
	 * If the TNCS supports limiting the message size or number of 
	 * round trips, the TNCS MUST throw 
	 * TNC_RESULT_EXCEEDED_MAX_MESSAGE_SIZE or 
	 * TNC_RESULT_EXCEEDED_MAX_ROUND_TRIPS exception respectively if 
	 * the limits are exceeded.  An IMV can get the Maximum Message 
	 * Size and Maximum Number of Round Trips by using the related 
	 * Attribute ID within the getAttribute method.  The IMV SHOULD 
	 * adapt its behavior to accommodate these limitations if available.
	 * <p>
	 * The TNC Server MUST support any message type. However, the IMV 
	 * MUST NOT specify a message type whose vendor ID is 0xffffff or 
	 * whose subtype is 0xff. These values are reserved for use as 
	 * wild cards, as described in section 3.9.1. If the IMV violates 
	 * this prohibition, the TNCS SHOULD throw 
	 * TNC_RESULT_INVALID_PARAMETER exception.
	 * <p>
	 * @param messageFlags flags associated with the message
	 * @param messageVendorID vendor ID associated with the message
	 * @param messageSubtype message subtype associated with the message
	 * @param message the message to be delivered
	 * @param sourceIMVID source IMC ID for the message
	 * @param destinationIMCID destination IMV ID for message
	 * 
	 * @throws TNCException if a TNC error occurs
	 */
	public void sendMessageLong(long messageFlags,
							    long messageVendorID,
							    long messageSubtype,
							    byte[] message,
							    long sourceIMVID,
							    long destinationIMCID) throws TNCException;
}
