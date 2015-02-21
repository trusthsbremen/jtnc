package org.trustedcomputinggroup.tnc.ifimc;

/**
 * This interface can be implemented by an object that already supports
 * the IMC interface to indicate that this object is an IMC that 
 * supports functions specific to IF-TNCCS-SOH.  To check whether an 
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
public interface IMCSOH {

    /**
	 * The TNC Client calls this method (if supported by the TNCC and 
	 * implemented by an IMC) to deliver an SOHRReportEntry message to
	 * the IMC. This allows an IMC to receive the entire 
	 * SOHRReportEntry instead of just the contents of the 
	 * Vendor-Specific attribute, as would be the case if the 
	 * receiveMessage method was used.
	 * <p>
	 * A TNCC that supports IF-TNCCS-SOH SHOULD support this method 
	 * and a TNCC that supports this function SHOULD call this method 
	 * for a particular IMC and connection instead of calling 
	 * receiveMessage if the IMC implements this method and the 
	 * connection uses IF-TNCCS-SOH. A TNCC MUST NOT call this method 
	 * for a particular IMC and connection if the IMC does not 
	 * implement this function or the connection does not use 
	 * IF-TNCCS-SOH. A TNCC will often find that some IMCs loaded by 
	 * that TNCC implement this function and some do not. In that case,
	 * when the TNCC is handling a connection that uses IF-TNCCS-SOH, 
	 * the TNCC would call receiveMessageSOH for the IMCs that 
	 * implement this method and receiveMessage for the IMCs that do 
	 * not implement receiveMessageSOH. A TNCC MUST NOT call both 
	 * receiveMessage and receiveMessageSOH for a single 
	 * SOHRReportEntry for a single IMC.
	 * <p>
	 * IMCs are not required to implement this method but any IMC that
	 * implements the IMCSOH interface MUST implement this method. An 
	 * IMC should implement receiveMessageSOH if it wants to receive 
	 * the entire SOHRReportEntry instead of just the Vendor-Specific 
	 * attribute when IF-TNCCS-SOH is used. However, IMCs should 
	 * recognize that many TNCCs do not support IF-TNCCS-SOH and some 
	 * TNCCs that do support IF-TNCCS-SOH will not support this 
	 * function. Therefore, IMCs should be prepared to receive 
	 * messages via receiveMessage in some cases when IF-TNCC-SOH is 
	 * used. Simple IMCs that only send messages need not implement 
	 * this method. IMC implementers may find that implementing 
	 * receiveMessageSOH is more complex than implementing 
	 * receiveMessage since receiveMessageSOH must parse the 
	 * SOHRReportEntry while  receiveMessage only needs to parse the 
	 * contents of the Vendor-Specific attribute within the 
	 * SOHRReportEntry. For many IMCs, the contents of the 
	 * Vendor-Specific attribute is all that they need. Therefore, 
	 * IMCs are not required to implement receiveMessageSOH.
	 * <p>
	 * The contents of the SOHRReportEntry is contained in the buffer 
	 * referenced by sohrReportEntry. The length of this array MUST 
	 * NOT be zero and the parameter MUST NOT be null since zero-length
	 * SOHRReportEntries are not permitted by the IF-TNCCS-SOH 
	 * protocol. The contents of the first System-Health-ID attribute 
	 * in the SOHRReportEntry is indicated by systemHealthID. This 
	 * value MUST match one of the message type values previously 
	 * supplied by the IMC to the TNCC in the IMC's most recent call 
	 * to the TNCC's reportMessageTypes() or reportMessageTypesLong() 
	 * methods. IMCs MAY check these parameters to make sure they are 
	 * valid and throw an exception if not, but IMCs are not required 
	 * to make these checks. 
	 * <p>
	 * The IMC MUST NOT send any IMC-IMV messages on the connection 
	 * after this method is called since IF-TNCCS-SOH supports only 
	 * one round-trip.
	 * <p>
	 * As with all IMC methods, the IMC SHOULD NOT wait a long time 
	 * before returning from receiveMessageSOH. To do otherwise would 
	 * risk delaying the handshake indefinitely. A long delay might 
	 * frustrate users or exceed network timeouts (PDP, PEP or otherwise).
	 * <p>
	 * The IMC MUST NOT ever modify the buffer contents and MUST NOT 
	 * access the buffer after receiveMessageSOH has returned. If the 
	 * IMC wants to retain the contents of the SOHRReportEntry, it 
	 * should copy it before returning from receiveMessageSOH.
	 * <p>
     * @param c the IMC connection object
     * @param systemHealthID the type of message to be delivered
     * @param sohrReportEntry the message to be delivered
     *
     * @exception TNCException if a TNC error occurs
     */
	public void receiveMessageSOH(IMCConnection c,
								  long systemHealthID,
								  byte[] sohrReportEntry) throws TNCException;
}
