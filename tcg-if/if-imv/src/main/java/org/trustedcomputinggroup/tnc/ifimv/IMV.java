package org.trustedcomputinggroup.tnc.ifimv;

/**
 * An Integrity Measurement Verifier (IMV).
 * These methods are implemented by the IMV and called by the 
 * TNC Server.
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
public interface IMV {

	/**
	 * Initializes the IMV. All IMVs MUST implement this Method.
	 * The TNC Server supplies itself as a parameter
	 * so the IMV can call the TNCS as needed.
	 * <p>
	 * The TNC Server MUST NOT call any other 
	 * IF-IMV API Methods for an IMV until it has 
	 * successfully completed a call to the IMV's initialize
	 * method. Once a call to this method has 
	 * completed successfully, this method MUST NOT be 
	 * called again for a particular IMV-TNCS pair until 
	 * a call to the IMV's terminate method has completed 
	 * successfully.
	 * <p>
	 * @param tncs the TNC Server
	 * 
	 * @throws TNCException if a TNC error occurs
	 */
    public void initialize(TNCS tncs) throws TNCException;
    
    /**
     * Closes down the IMV. The TNC Server calls this method
     * when all work is complete 
     * and the TNCS is preparing to shut down or when 
     * the IMV throws a TNC_RESULT_FATAL exception. Once a call 
     * to an IMV's terminate method is made, the 
     * TNC Server MUST NOT call the IMV except 
     * to call the IMV's initialize method (which may
     * not succeed if the IMV cannot reinitialize
     * itself). Even if the IMV returns an error from
     * this method, the TNC Server MAY continue
     * with its unload or shutdown procedure.
     * <p>
     * @throws TNCException if a TNC error occurs
     */
    public void terminate() throws TNCException;

    /**
     * Informs the IMV that the state of IMVConnection c
     * has changed to newState.
     * The TNCConstants interface lists all the possible 
     * values of the new state for this version of the 
     * IF-IMV API. The TNCS MUST NOT use any other 
     * values with this version of IF-IMV.
     * <p>
     * IMVs that want to track the state of network 
     * connections or maintain per-connection data 
     * structures SHOULD implement this method. 
     * Other IMVs MAY implement it.  If an IMV chooses
     * to not implement this method it MUST throw 
     * an UnsupportedOperationException.
     * <p>
     * If the state is TNC_CONNECTION_STATE_CREATE, 
     * the IMV SHOULD note the creation of a new 
     * network connection.
     * <p>
     * If the state is TNC_CONNECTION_STATE_HANDSHAKE, 
     * an Integrity Check Handshake is about to begin.
     * <p>
     * If the state is TNC_CONNECTION_STATE_DELETE,
     * the IMV SHOULD discard any state pertaining
     * to this network connection and MUST NOT pass
     * this network connection to the TNC Server
     * after this method returns.
     * <p> 
     * @param c the IMV Connection
     * @param newState new network connection state
     * 
     * @throws TNCException
     */
    public void notifyConnectionChange(IMVConnection c, long newState) throws TNCException;

    /**
     * The TNC Server calls this method to deliver a 
     * message to the IMV. The message is contained in the 
     * buffer referenced by message. The type of the message is 
     * indicated by messageType. The message MUST 
     * be from an IMC (or a TNCC or other party 
     * acting as an IMC).
     * <p>
	 * If IF-TNCCS-SOH is used for a connection and the 
	 * IMV and TNCS implement receiveMessageSOH(), the 
	 * TNCS SHOULD use that method to deliver the contents
	 * of SOHReportEntries instead of using receiveMessage.
	 * However, if IF-TNCCS-SOH is used for a connection 
	 * but either the IMV or the TNCS does not implement
	 * receiveMessageSOH, receiveMessage SHOULD be used 
	 * instead. For each SOHReportEntry, the value contained
	 * in the first System-Health-ID attribute should be 
	 * compared to the messageType values previously supplied
	 * in the IMV's most recent call to reportMessageTypes 
	 * or reportMessageTypesLong. The SOHReportEntry should
	 * be delivered to each IMV that has a match. If an IMV
	 * that does not support receiveMessageSOH (or when the
	 * TNCS does not support that method), receiveMessage 
	 * SHOULD be employed. In that case, the TNCS MUST pass
	 * in the message parameter a reference to a buffer
	 * containing the Data field of the first Vendor-Specific
	 * attribute whose Vendor ID matches the value contained
	 * in the System-Health-ID. If no such Vendor-Specific 
	 * attribute exists, the SOHReportEntry MUST NOT be 
	 * delivered to this IMV. The TNCS MUST pass in the 
	 * messageType parameter the value contained in the 
	 * first System-Health-ID attribute.
     * <p>
     * The IMV SHOULD send any IMC-IMV messages it 
     * wants to send as soon as possible after this 
     * method is called and then return from this 
     * method to indicate that it is finished 
     * sending messages in response to this message.
     * <p>
     * As with all IMV methods, the IMV SHOULD NOT 
     * wait a long time before returning from the IMV
     * receiveMessage() method. To do otherwise 
     * would risk delaying the handshake indefinitely. 
     * A long delay might frustrate users or exceed 
     * network timeouts (PDP, PEP or otherwise).
     * <p>
     * The IMV should implement this method if it 
     * wants to receive messages. Most IMVs will do 
     * so, since they will base their IMV Action 
     * Recommendations on measurements received 
     * from the IMC. However, some IMVs may base 
     * their IMV Action Recommendations on other 
     * data such as reports from intrusion 
     * detection systems or scanners. Those IMVs 
     * need not implement this method.
     * <p>
     * The IMV MUST NOT ever modify the buffer 
     * contents and MUST NOT access the buffer 
     * after the IMV receiveMessage() method has returned. 
     * If the IMV wants to retain the message, it 
     * should copy it before returning from this
     * method.
     * <p>
     * The message parameter may be null to represent an empty message.
     * In the messageType parameter, the TNCS MUST pass the type of the
     * message. This value MUST 
     * match one of the TNC_MessageType values 
     * previously supplied by the IMV to the TNCS in 
     * the IMV's most recent call to the TNCS 
     * reportMessageTypes method. IMVs MAY check 
     * these parameters to make sure they are valid 
     * and return an error if not, but IMVs are not 
     * required to make these checks.
     * <p>
     * @param c the IMVConnection
     * @param messageType the message type that is being delivered to the IMV
     * @param message the message that is being delivered to the IMV
     * 
     * @throws TNCException
     */
    public void receiveMessage(IMVConnection c, long messageType, byte[] message) throws TNCException;
    
    /**
     * The TNC Server calls this method at the end of an
     * Integrity Check Handshake (after all IMC-IMV 
     * messages have been delivered) to solicit 
     * recommendations from IMVs that have not 
     * yet provided a recommendation. The TNCS
     * MUST NOT call this method for an IMV for
     * a particular connection if that IMV has
     * already called <code>provideRecommendation</code>
     * on that connection since the TNCS last called
     * <code>notifyConnectionChange</code> for that
     * IMV with that connection. If an IMV is 
     * not able to provide a recommendation at this 
     * time, it SHOULD call the TNCS 
     * provideRecommendation() method with the 
     * recommendation parameter set to 
     * TNC_IMV_ACTION_RECOMMENDATION_NO_RECOMMENDATION. 
     * If an IMV returns from this method without 
     * calling the TNCS' provideRecommendation method, 
     * the TNCS MAY consider the IMV's Action 
     * Recommendation to be 
     * TNC_IMV_ACTION_RECOMMENDATION_NO_RECOMMENDATION. 
     * The TNCS MAY take other actions, 
     * such as logging this IMV behavior, which is erroneous.
     * <p>
     * All IMVs MUST implement this method.
     * <p>
     * Note that a TNCC or TNCS MAY cut off 
     * IMC-IMV communications at any time for 
     * any reason, including limited support for 
     * long conversations in underlying protocols, 
     * user or administrator intervention, or 
     * policy. If this happens, the TNCS will 
     * return TNC_RESULT_ILLEGAL_OPERATION 
     * from sendMessage and call the 
     * solicitRecommendation method to 
     * elicit IMV Action Recommendations based 
     * on the data they have gathered so far.
     * <p>
     * @param c IMV connection
     * 
     * @throws TNCException
     */
    public void solicitRecommendation(IMVConnection c) throws TNCException;
    
    /**
     * The TNC Server calls this method to notify 
	 * IMVs that all IMC messages received in a 
	 * batch have been delivered and this is the 
	 * IMV's last chance to send a message in the
	 * batch of IMV messages currently being collected.
	 * An IMV MAY implement this method if it wants 
	 * to perform some actions after all the IMC 
	 * messages received during a batch have been 
	 * delivered (using the IMV's receiveMessage(),
	 * receiveMessageLong() or receiveMessageSOH() 
	 * methods). For instance, if an IMV has not 
	 * received any messages from an IMC it may 
	 * conclude that its IMC is not installed on 
	 * the endpoint and may decide to call the 
	 * TNCS' provideRecommendation method with 
	 * the recommendation parameter set to 
	 * TNC_IMV_ACTION_RECOMMENDATION_NO_ACCESS. 
     * <p>
	 * An IMV MAY call the TNCS' sendMessage(), 
	 * sendMessageSOH()  or sendMessageLong() 
	 * methods from this method. As with all IMV 
	 * methods, the IMV SHOULD NOT wait a long 
	 * time before returning from the batchEnding 
	 * method. To do otherwise would risk delaying 
	 * the handshake indefinitely. A long delay 
	 * might frustrate users or exceed network 
	 * timeouts (PDP, PEP or otherwise). 
     * <p>
     * @param c IMV connection
     * 
     * @throws TNCException
     */
    public void batchEnding(IMVConnection c) throws TNCException;
}
