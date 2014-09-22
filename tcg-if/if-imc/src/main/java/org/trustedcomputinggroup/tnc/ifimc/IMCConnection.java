package org.trustedcomputinggroup.tnc.ifimc;

/**
 * The IMC and TNCC use this IMCConnection object to refer 
 * to the network connection when delivering messages 
 * and performing other operations relevant to the 
 * network connection. This helps ensure that IMC 
 * messages are sent to the right TNCS and helps the 
 * IMC match up messages from IMVs with any state the 
 * IMC may be maintaining from earlier parts of that 
 * IMC-IMV conversation (even extending across multiple 
 * Integrity Check Handshakes in a single network 
 * connection).
 * <p>
 * The TNCC MUST create a new <code>IMCConnection</code> object for each
 * combination of an IMC and a connection. <code>IMCConnection</code>
 * objects MUST NOT be shared between multiple IMCs.
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
public interface IMCConnection {

    /**
     * Handshake retry reason when IMC has completed remediation.
     */

    public static final long TNC_RETRY_REASON_IMC_REMEDIATION_COMPLETE = 0;
    
    /**
     * IMC has detected a serious event and recommends 
     * handshake retry even if network connectivity must be interrupted.
     */

    public static final long TNC_RETRY_REASON_IMC_SERIOUS_EVENT = 1;
    
    /**
     * IMC has detected an event that it would like to communicate to 
     * the IMV. It requests handshake retry but not if network 
     * connectivity must be interrupted.
     */

    public static final long TNC_RETRY_REASON_IMC_INFORMATIONAL_EVENT = 2;
    
    /**
     * IMC wishes to conduct a periodic recheck. It recommends 
     * handshake retry but not if network connectivity must be 
     * interrupted.
     */

    public static final long TNC_RETRY_REASON_IMC_PERIODIC = 3;
    
    /**
     * An IMC calls this method to give a message to the TNCC for 
     * delivery. The message is contained in the buffer referenced by 
     * the message parameter.  The message parameter may 
     * be NULL which represent an empty message. The type 
     * of the message is indicated by the messageType parameter. 
     * <p>
     * All IMC Connections MUST implement this method. The IMC 
     * Connection MUST NOT ever modify the buffer contents and MUST NOT 
     * access the buffer after the TNCC sendMessage() method has 
     * returned. The TNC Client will typically copy the message out of 
     * the buffer, queue it up for delivery, and return from this method.
     * <p>
     * The IMC MUST NOT call this method unless it has received a 
     * call to the IMC's beginHandshake() method, the IMC's 
     * receiveMessage() method, or the IMC's batchEnding() method
     * for this connection and the IMC has not yet returned from that 
     * method. If the IMC violates this prohibition, the TNCC SHOULD 
     * throw a TNC_RESULT_ILLEGAL_OPERATION exception. If an IMC 
     * really wants to communicate with an IMV at another time, it 
     * should call the TNCC requestHandshakeRetry() method.
     * <p>
     * Note that a TNCC or TNCS MAY cut off IMC-IMV communications at 
     * any time for any reason, including limited support for long 
     * conversations in underlying protocols, user or administrator 
     * intervention, or policy. If this happens, the TNCC will throw a 
     * TNC_RESULT_ILLEGAL_OPERATION. If the TNCC supports limiting the
	 * message size or number of round trips, the TNCC MUST throw a 
	 * TNCException with result code TNC_RESULT_EXCEEDED_MAX_MESSAGE_SIZE
	 * or TNC_RESULT_EXCEEDED_MAX_ROUND_TRIPS respectively if the 
	 * limits are exceeded.  An IMC can get the Maximum Message Size 
	 * and Maximum Number of Round Trips by using the related Attribute
	 * ID with the getAttribute method on the IMCConnection object.  
	 * The IMC SHOULD adapt its behavior to accommodate these 
	 * limitations if available.
     * <p>
     * The TNC Client MUST support any message type. However, the 
     * IMC MUST NOT specify a message type whose vendor ID is 0xffffff 
     * or whose subtype is 0xff. These values are reserved for use as 
     * wild cards.  If the IMC violates 
     * this prohibition, the TNCC SHOULD throw a
     * TNC_RESULT_INVALID_PARAMETER exception. If the IMCConnection 
	 * supports limiting the message size or number of round trips, 
	 * the IMCConnection MUST return TNC_RESULT_EXCEEDED_MAX_MESSAGE_SIZE
	 * exception or TNC_RESULT_EXCEEDED_MAX_ROUND_TRIPS exception 
	 * respectively if the limits are exceeded.  An IMC can get the 
	 * Maximum Message Size and Maximum Number of Round Trips by using
	 * the related TNC_ATTRIBUTEID within the 
	 * AttributeSupport.GetAttribute method.  The IMC SHOULD adapt its
	 * behavior to accommodate these limitations if available.
     * <p>
     * @param messageType the type of message to be delivered
     * @param message the message to be delivered 
 
     * @exception TNCException if a TNC error occurs
     */

    public void sendMessage(long messageType, byte[] message) throws TNCException;

   /**
    * An IMC calls this method to ask a IMC Connection to retry an
    * Integrity Check Handshake. The IMC MUST pass one of 
    * the handshake retry reasons listed in IF-IMC 
    * Abstract API as the reason parameter.
    * <p>
    * TNCCs MAY check the parameters to make sure they are valid and 
	* throw an exception if not, but TNCCs are not required to make 
	* these checks. The reason parameter explains why the IMC is 
	* requesting a handshake retry. The TNCC MAY use this in deciding 
	* whether to attempt the handshake retry. As noted in the Abstract
	* API, TNCCs are not required to honor IMC requests for handshake 
	* retry (especially since handshake retry may not be possible or 
	* may interrupt network connectivity). An IMC MAY call this method
	* at any time, even if an Integrity Check Handshake is currently 
	* underway. This is useful if the IMC suddenly gets important 
	* information but has already finished its dialog with the IMV, 
	* for instance. As always, the TNCC is not required to honor the 
	* request for handshake retry.
    * <p>
    * If the TNCC cannot attempt the handshake retry, the IMCConnection
	* SHOULD throw a TNCException with result code 
	* TNC_RESULT_CANT_RETRY. If the TNCC could attempt to retry the 
	* handshake but chooses not to, the IMCConnection SHOULD throw a 
	* TNCException with result code TNC_RESULT_WONT_RETRY. The IMC MAY
	* use this information in displaying diagnostic and progress 
	* messages. 
    * <p>
    * @param reason the reason for the handshake request
    *
    * @exception TNCException if a TNC error occurs
    */
    
    public void requestHandshakeRetry(long reason) throws TNCException;
    
}
