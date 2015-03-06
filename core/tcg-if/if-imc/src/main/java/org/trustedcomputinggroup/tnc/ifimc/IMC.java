package org.trustedcomputinggroup.tnc.ifimc;

import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
/**
 * These methods are implemented by the IMC and called by the 
 * TNC Client.
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
public interface IMC {

    /**
     * The TNC Client calls this method to initialize the IMC and 
     * agree on the API version number to be used.  The TNC Client supplies 
     * itself as a parameter. All IMCs MUST implement this 
     * method.
     * <p>
     * The TNC Client MUST NOT call any other IF-IMC API methods for 
     * an IMC until it has successfully completed a call to the IMC
     * initialize() method. Once a call to this method has completed 
     * successfully, this method MUST NOT be called again for a 
     * particular IMC-TNCC pair until a call to IMC terminate() method 
     * has completed successfully.
     * <p>
     * @param tncc the TNC Client
     *
     * @exception TNCException if a TNC error occurs
     */

    public void initialize(TNCC tncc) throws TNCException;
 
    /**
     * Closes down the IMC. The TNC Client calls this method to close down the IMC when 
     * all work is complete or the IMC throws an TNC_RESULT_FATAL 
     * exception.  Once a call to an IMC's terminate() method is made, 
     * the TNC Client MUST NOT call the IMC except to call 
     * the IMC's initialize() method (which may not 
     * succeed if the IMC cannot reinitialize itself). Even if the IMC 
     * throws an exception from this method, the TNC Client MAY continue
     * with its unload or shutdown procedure.
     * <p>
     *
     * @exception TNCException if a TNC error occurs
     */
    
    public void terminate() throws TNCException;

    /**
     * Informs the IMC that the 
     * state of the network connection identified by connection 
     * parameter has changed to a new state.  All the possible values 
     * of the new state for this version of the IF-IMC API are 
     * identified in the TNCConstants class.  The TNCC MUST 
     * NOT use any other values with this version of IF-IMC.
     * <p>
     * IMCs that want to track the state of network connections or 
     * maintain per-connection data structures SHOULD implement this 
     * method.  If an IMC chooses to not implement this method it MUST
     * throw an UnsupportedOperationException.
     * <p>
     * If the state is TNC_CONNECTION_STATE_CREATE, the IMC SHOULD 
     * note the creation of a new network connection.
     * <p>
     * If the state is TNC_CONNECTION_STATE_ACCESS_ALLOWED or 
     * TNC_CONNECTION_STATE_ACCESS_ISOLATED, the IMC SHOULD proceed 
     * with any remediation instructions received during the Integrity 
     * Check Handshake. However, the IMC SHOULD be prepared for delays 
     * in network access or even complete denial of network access, 
     * even in these cases. Network access will often be delayed for a 
     * few seconds while an IP address is acquired. And network access 
     * may be denied if the NAA overrides the TNCS Action 
     * Recommendation reflected in the new state value.
     * <p>
     * If the state is TNC_CONNECTION_STATE_ACCESS_NONE, the IMC MAY 
     * discard any remediation instructions received during the 
     * Integrity Check Handshake or it MAY follow them if possible.
     * <p>
     * If the state is TNC_CONNECTION_STATE_HANDSHAKE, an Integrity 
     * Check Handshake is about to begin.
     * <p>
     * If the state is TNC_CONNECTION_STATE_DELETE, the IMC SHOULD 
     * discard any state pertaining to this network connection and 
     * MUST NOT pass this network connection ID to the TNC Client 
     * after this method returns (unless the TNCC later creates 
     * another network connection with the same network connection ID).
     * <p>
     * @param c the IMC connection object 
     * @param newState new network connection state
     *
     * @exception TNCException if a TNC error occurs
     */
 
   public void notifyConnectionChange(IMCConnection c, long newState) throws TNCException;
    
    /**
     * The TNC Client calls this method to indicate that an 
     * Integrity Check Handshake is beginning and solicit messages 
     * from IMCs for the first batch. The IMC SHOULD send any IMC-IMV 
     * messages it wants to send as soon as possible after this 
     * method is called and then return from this method to 
     * indicate that it is finished sending messages for this batch.
     * <p>
     * As with all IMC methods, the IMC SHOULD NOT wait a long time 
     * before returning from the IMC beginHandshake() method.  To do 
     * otherwise would risk delaying the handshake indefinitely. A long 
     * delay might frustrate users or exceed network timeouts (PDP, 
     * PEP or otherwise).
     * <p>
     * All IMCs MUST implement this method.
      * <p>
     * @param c the IMC connection object 
     *
     * @exception TNCException if a TNC error occurs
     */
    public void beginHandshake(IMCConnection c) throws TNCException;
    
    /**
     * The TNC Client calls this method to deliver a message to the 
     * IMC. The message is contained in the buffer referenced by 
     * message and contains the number of octets (bytes) indicated by 
     * messageLength. The type of the message is indicated by 
     * messageType. The message must be from an IMV (or a TNCS or 
     * other party acting as an IMV).
     * <p>
	 * If IF-TNCCS-SOH is used for a connection and the IMC and TNCC 
	 * implement receiveMessageSOH(), the TNCC SHOULD use that method 
	 * to deliver the contents of SOHRReportEntries instead of using 
	 * receiveMessage. However, if IF-TNCCS-SOH is used for a 
	 * connection but either the IMC or the TNCC does not implement 
	 * receiveMessageSOH, receiveMessage SHOULD be used instead. For 
	 * each SOHResponseEntry, the value contained in the first 
	 * System-Health-ID attribute should be compared to the messageType
	 * values previously supplied in the IMC's most recent call to 
	 * reportMessageTypes or reportMessageTypesLong. The 
	 * SOHResponseEntry should be delivered to each IMC that has a 
	 * match. If an IMC that does not support receiveMessageSOH (or 
	 * when the TNCC does not support that method), receiveMessage 
	 * SHOULD be employed. In that case, the TNCC MUST pass in the 
	 * message parameter a reference to a buffer containing the Data 
	 * field of the first Vendor-Specific attribute whose Vendor ID 
	 * matches the value contained in the System-Health-ID. If no such
	 * Vendor-Specific attribute exists, the SOHResponseEntry MUST NOT
	 * be delivered to this IMC. The TNCC MUST pass in the messageType
	 * parameter the value contained in the first System-Health-ID 
	 * attribute.
     * <p>
     * The IMC SHOULD send any IMC-IMV messages it wants to send as 
     * soon as possible after this method is called and then return 
     * from this method to indicate that it is finished sending 
     * messages in response to this message.
     * <p>
     * As with all IMC methods, the IMC SHOULD NOT wait a long time 
     * before returning from the IMC receiveMessage() method. To do 
     * otherwise would risk delaying the handshake indefinitely. A long 
     * delay might frustrate users or exceed network timeouts (PDP, 
     * PEP or otherwise).
     * <p>
     * The IMC should implement this method if it wants to receive 
     * messages. Simple IMCs that only send messages need not 
     * implement this method but MUST at a minimum throw an 
     * UnsupportedOperationException.  The IMC MUST NOT ever modify the 
     * buffer contents and MUST NOT access the buffer after the IMC
     * receiveMessage() method has returned. If the IMC wants to retain the 
     * message, it should copy it before returning from 
     * the IMC receiveMessage() method.
     * <p>
     * The message parameter may be null to represent an empty message. 
     * In the messageType parameter, the TNCC MUST pass the type of the
     * message. This value MUST match one of the TNC_MessageType values
     * previously supplied by the IMC to the TNCC in the IMC's most
     * recent call 
     * to the TNCC's reportMessageTypes() method. IMCs MAY check these 
     * parameters to make sure they are valid and throw an exception
     * if not, but IMCs are not required to make these checks.
     * <p>
     * @param c the IMC connection object 
     * @param messageType the type of message to be delivered
     * @param message the message to be delivered 
     *
     * @exception TNCException if a TNC error occurs
     */
    
    public void receiveMessage(IMCConnection c, long messageType, byte[] message) throws TNCException;
    
    /**
     * The TNC Client calls this method to notify IMCs that all IMV 
     * messages received in a batch have been delivered and this is 
     * the IMC's last chance to send a message in the batch of IMC 
     * messages currently being collected. An IMC MAY implement this 
     * method if it wants to perform some actions after all the IMV 
     * messages received during a batch have been delivered 
     * (using the IMC receiveMessage(), receiveMessageLong(), or 
	 * receiveMessageSOH() methods).  This is especially 
     * useful for IMCs that have included a wildcard in the list of 
     * message types reported using the TNCC reportMessageTypes() or 
	 * reportMessageTypesLong() methods. If an IMC chooses to not 
	 * implement this method it MUST throw an 
	 * UnsupportedOperationException. 
     * <p>
     * An IMC MAY call the IMCConnection's sendMessage(), 
	 * sendMessageSOH(), or sendMessageLong() methods from this method.
	 * As with all IMC methods, the IMC SHOULD NOT wait a long time 
	 * before returning from the batchEnding method. To do otherwise 
	 * would risk delaying the handshake indefinitely. A long delay 
	 * might frustrate users or exceed network timeouts (PDP, PEP or 
	 * otherwise).
     * <p>
     * @param c the IMC connection object 
     *
     * @exception TNCException if a TNC error occurs
     */

    public void batchEnding(IMCConnection c) throws TNCException;

}
