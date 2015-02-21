package org.trustedcomputinggroup.tnc.ifimv;

/**
 * The IMV and TNCS use this IMVConnection object to 
 * refer to the network connection when delivering 
 * messages and performing other operations relevant to 
 * the network connection. This helps ensure that IMV 
 * messages are sent to the right TNCC and IMCs, helps 
 * ensure that the IMV Action Recommendation is 
 * associated with the right endpoint, and helps the IMV 
 * match up messages from IMCs with any state the IMV may 
 * be maintaining from earlier parts of that IMC-IMV 
 * conversation (even extending across multiple 
 * Integrity Check Handshakes in a single network 
 * connection).
 * <p>
 * The TNCS MUST create a new IMVConnection object for each
 * combination of an IMV and a connection. IMVConnection
 * objects MUST NOT be shared between multiple IMVs.
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
public interface IMVConnection {
    
    /**
     * IMV policy has changed. It recommends handshake retry even
     * if network connectivity must be interrupted;
     */
    public static final long TNC_RETRY_REASON_IMV_IMPORTANT_POLICY_CHANGE = 4;
    
    /**
     * IMV policy has changed. It requests handshake retry but 
     * not if network connectivity must be interrupted;
     */
    public static final long TNC_RETRY_REASON_IMV_MINOR_POLICY_CHANGE = 5;
    
    /**
     * IMV has detected a serious event and recommends 
     * handshake retry even if network connectivity must be 
     * interrupted.
     */
    
    public static final long TNC_RETRY_REASON_IMV_SERIOUS_EVENT = 6;
    
    /**
     * IMV has detected a minor event. It requests handshake 
     * retry but not if network connectivity must be 
     * interrupted.
     */
    
    public static final long TNC_RETRY_REASON_IMV_MINOR_EVENT = 7;
    
    /**
     * IMV wishes to conduct a periodic recheck. It recommends 
     * handshake retry but not if network connectivity must be 
     * interrupted.
     */
    
    public static final long TNC_RETRY_REASON_IMV_PERIODIC = 8;
    
    /**
     * IMV recommends allowing access.
     */
    public static final long TNC_IMV_ACTION_RECOMMENDATION_ALLOW = 0;
    
    /**
     * IMV recommends no access.
     */
    public static final long TNC_IMV_ACTION_RECOMMENDATION_NO_ACCESS = 1;
    
    /**
     * IMV recommends limited access. This access may be
     * expanded after remediation.
     */
    public static final long TNC_IMV_ACTION_RECOMMENDATION_ISOLATE = 2;
    
    /**
     * IMV does not have a recommendation.
     */
    public static final long TNC_IMV_ACTION_RECOMMENDATION_NO_RECOMMENDATION = 3;

    /**
     * AR complies with policy.
     */
    
    public static final long TNC_IMV_EVALUATION_RESULT_COMPLIANT = 0;
    
    /**
     * AR is not compliant with policy. Non-compliance is minor.
     */
    
    public static final long TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MINOR = 1;
    
    /**
     * AR is not compliant with policy. Non-compliance is major.
     */
    public static final long TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MAJOR = 2;
    
    /**
     * IMV is unable to determine policy compliance due to error.
     */
    
    public static final long TNC_IMV_EVALUATION_RESULT_ERROR = 3;
    
    /**
     * IMV does not know whether AR complies with policy.
     */
    public static final long TNC_IMV_EVALUATION_RESULT_DONT_KNOW = 4;
    
    /**
     * Gives a message to the TNCS for
     * delivery. The message is contained in the buffer referenced by 
     * the <code>message</code> parameter.  The <code>message</code> parameter may 
     * be null which represent an empty message. The type 
     * of the message is indicated by the messageType parameter. 
     * <p>
     * All IMVConnections MUST implement this method. An
     * IMVConnection MUST NOT ever modify the buffer contents and 
     * MUST NOT access the buffer after the sendMessage
     * method has returned. The IMVConnection will 
     * typically copy the message out of the buffer, queue it up 
     * for delivery, and return from this method.
     * <p>
     * The IMV MUST NOT call this method unless it has 
     * received a call to the IMV's receiveMessage method or the 
     * IMV's batchEnding method for this connection and the IMV 
     * has not yet returned from that method. If the IMV 
     * violates this prohibition, the TNCS SHOULD throw the 
     * TNC_RESULT_ILLEGAL_OPERATION exception. If an IMV 
     * really wants to communicate with an IMC at another 
     * time, it should call the IMVConnection's requestHandshakeRetry
     * method.
     * <p>
     * Note that a TNCC or TNCS MAY cut off IMC-IMV 
     * communications at any time for any reason, including 
     * limited support for long conversations in underlying 
     * protocols, user or administrator intervention, or 
     * policy. If this happens, the IMVConnection's
     * sendMessage method will 
     * throw a TNCException with result code TNC_RESULT_ILLEGAL_OPERATION
     * and call the IMVs'
     * solicitRecommendation() to elicit IMV Action 
     * Recommendations based on the data they have gathered 
     * so far. If the TNCS supports limiting the message size 
	 * or number of round trips, the TNCS MUST throw a TNCException
	 * with result code TNC_RESULT_EXCEEDED_MAX_MESSAGE_SIZE 
	 * or TNC_RESULT_EXCEEDED_MAX_ROUND_TRIPS respectively if 
	 * the limits are exceeded.  An IMV can get the Maximum 
	 * Message Size and Maximum Number of Round Trips by using 
	 * the related Attribute ID with the getAttribute method on 
	 * the TNCS object.  The IMV SHOULD adapt its behavior to 
	 * accommodate these limitations if available.
     * <p>
     * The TNC Server MUST support any message type. However, 
     * the IMV MUST NOT specify a message type whose vendor 
     * ID is 0xffffff or whose subtype is 0xff. These values 
     * are reserved for use as wild cards, as described in 
     * the Abstract API. If the IMV violates this prohibition, 
     * the IMVConnection SHOULD throw a TNCException
     * with result code TNC_RESULT_INVALID_PARAMETER. If the 
	 * IMVConnection supports limiting the message size or number
	 * of round trips, the IMVConnection MUST return 
	 * TNC_RESULT_EXCEEDED_MAX_MESSAGE_SIZE exception or 
	 * TNC_RESULT_EXCEEDED_MAX_ROUND_TRIPS exception respectively
	 * if the limits are exceeded.  An IMV can get the Maximum 
	 * Message Size and Maximum Number of Round Trips by using
	 * the related TNC_ATTRIBUTEID within the TNCS.getAttribute
	 * method.  The IMV SHOULD adapt its behavior to accommodate
	 * these limitations if available.
     * 
     * @param messageType the type of message to be delivered
     * @param message the message to be delivered 
     * 
     * @throws TNCException if an error occurs
     */
    
    public void sendMessage(long messageType, byte[] message) throws TNCException;
    
    /**
     * Asks a TNCS to retry an Integrity Check Handshake for this
     * IMVConnection. The IMV MUST pass one of the 
     * handshake retry reasons listed in the Abstract API as the 
     * reason parameter. 
     * <p>
     * TNCSs MAY check the parameters to make sure they are 
     * valid and throw an exception if not, but TNCSs are not 
     * required to make these checks. The reason parameter 
     * explains why the IMV is requesting a handshake retry. 
     * The TNCS MAY use this in deciding whether to attempt 
     * the handshake retry. As noted in the Abstract API, TNCSs 
     * are not required to honor IMV requests for handshake 
     * retry (especially since handshake retry may not be 
     * possible or may interrupt network connectivity). An 
     * IMV MAY call this method at any time, even if an 
     * Integrity Check Handshake is currently underway. 
     * This is useful if the IMV suddenly gets important 
     * information but has already finished its dialog with 
     * the IMC, for instance. As always, the TNCS is not 
     * required to honor the request for handshake retry.
     * <p>
     * If the TNCS cannot attempt the handshake retry, 
     * the IMVConnection SHOULD throw a TNCException
     * with result code TNC_RESULT_CANT_RETRY.
     * If the TNCS could attempt to retry the handshake but 
     * chooses not to, the IMVConnection SHOULD throw a TNCException
     * with result code TNC_RESULT_WONT_RETRY.
     * The IMV MAY use this information 
     * in displaying diagnostic and progress messages.
     * <p>
     * @param reason handshake retry reason code
     * 
     * @throws TNCException
     */
    public void requestHandshakeRetry(long reason) throws TNCException;
    
    /**
     * An IMV calls this method to deliver its IMV 
     * Action Recommendation and IMV Evaluation Result to 
     * the TNCS. The TNCS SHOULD use the recommendation 
     * value in determining its own TNCS Action 
     * Recommendation or decision about endpoint access. 
     * The TNC specifications do not specify how the TNCS 
     * does the recommendation value but it is certainly 
     * essential to have a recommendation from the IMV. 
     * The TNC specifications also do not specify what 
     * the TNCS does with the evaluation value. It may 
     * log it.
     * <p>
     * The IMV MUST pass one of the IMV Action 
     * Recommendation values listed in the Abstract API
     * as the recommendation parameter and one of 
     * the IMV Evaluation Result values listed in the Abstract 
     * API as the evaluation parameter. TNCSs 
     * MAY check these values to make sure they are 
     * valid and throw an exception if not, but TNCSs are 
     * not required to make these checks.
     * <p>
     * The IMV should deliver its IMV Action 
     * Recommendation as soon as possible so that the 
     * TNCS can proceed with determining its own TNCS 
     * Action Recommendation. If the IMV receives a 
     * message from an IMC and is able to decide on 
     * an IMV Action Recommendation and deliver it 
     * to the TNCS before returning from the IMV 
     * receiveMessage method, it SHOULD do so. 
     * However, as always the IMV SHOULD return 
     * promptly to avoid a long delay that might 
     * frustrate users or exceed network timeouts (PDP, 
     * PEP or otherwise).
     * <p>
     * An IMV SHOULD NOT expect that it will be able 
     * to send IMC-IMV messages after calling the 
     * IMVConnection's provideRecommendation method. The TNCS 
     * may decide to terminate the handshake immediately 
     * based on the IMV Action Recommendation. For 
     * instance, IMVs SHOULD send remediation 
     * instructions before calling the IMVConnection's
     * provideRecommendation method.
     * <p>
     * However, a TNCS MAY continue to deliver messages 
     * after an IMV calls the IMVConnection's provideRecommendation method, 
     * especially if other IMVs continue the dialog after
     * the one IMV has rendered its decision. The IMV 
     * MUST be prepared for this. It MAY simply ignore 
     * these late messages or it MAY consider them and 
     * even change its recommendation by calling the IMVConnection's
     * provideRecommendation method again. In this 
     * case, the TNCS SHOULD use the last recommendation 
     * received from an IMV during a particular handshake. 
     * However, the TNCS is not required to do this.
     * <p>
     * If an IMV does not provide a recommendation 
     * earlier, the TNCS will call the IMV's
     * solicitRecommendation method at the end of an 
     * Integrity Check Handshake (after all IMC-IMV 
     * messages have been delivered). The IMV SHOULD 
     * then call the IMVConnection's provideRecommendation method to 
     * deliver its recommendation. If the IMV calls 
     * this method when there is no active handshake 
     * on the specified network connection, the TNCS 
     * SHOULD throw the TNC_RESULT_ILLEGAL_OPERATION 
     * exception. If an IMV really needs to communicate 
     * a recommendation at another time, it should call
     * the IMVConnection's requestHandshakeRetry method.
     * <p>
     * @param recommendation action recommendation
     * @param evaluation evaluation result
     * 
     * @throws TNCException
     */
    
    public void provideRecommendation(long recommendation, long evaluation) throws TNCException;
    
    /**
     * An IMV calls this method to get the value of the attribute
     * identified by <code>attributeID</code> for this
     * <code>IMVConnection</code>.
     * <p>
     * This function is optional. The TNCS is not required to implement
     * it. If it is not implemented for this IMVConnection, it MUST
     * throw an UnsupportedOperationException. IMVs MUST work properly
     * if a TNCS does not implement this function.
     * <p>
     * The IMV MUST pass a standard or vendor-specific attribute ID as
     * the attributeID parameter. If the TNCS does not recognize the
     * attribute ID, it SHOULD throw a TNCException with the
     * TNC_RESULT_INVALID_PARAMETER result code. If the TNCS
     * recognizes the attribute ID but does not have an attribute
     * value for the requested attribute ID for this
     * <code>IMVConnection</code>, it SHOULD also throw a TNCException
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
     * <code>IMVConnection</code>.
     * <p>
     * This function is optional. The TNCS is not required to implement
     * it. If it is not implemented for this IMVConnection, it MUST
     * throw an UnsupportedOperationException. IMVs MUST work properly
     * if a TNCS does not implement this function.
     * <p>
     * The IMV MUST pass a standard or vendor-specific attribute ID as
     * the attributeID parameter. If the TNCS does not recognize the
     * attribute ID, it SHOULD throw a TNCException with the
     * TNC_RESULT_INVALID_PARAMETER result code. If the TNCS
     * recognizes the attribute ID but does not support setting
     * an attribute value for the requested attribute ID for this
     * <code>IMVConnection</code>, it SHOULD also throw a TNCException
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
    public void setAttribute(long attributeID, Object attributeValue)
	throws TNCException;

}
