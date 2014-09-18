package org.trustedcomputinggroup.tnc.ifimv;

import org.trustedcomputinggroup.tnc.TNCException;
/**
 * This interface can be implemented by an object that already supports
 * the IMV interface to indicate that this object is an IMV that 
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
public interface IMVSOH {

	/**
	 * The TNC Server calls this method (if supported by the TNCS and 
	 * implemented by an IMV) to deliver an SOHReportEntry message to 
	 * the IMV. This allows an IMV to receive the entire SOHReportEntry
	 * instead of just the contents of the Vendor-Specific attribute, 
	 * as would be the case if the receiveMessage method was used.
	 * <p>
	 * A TNCS that supports IF-TNCCS-SOH SHOULD support this method 
	 * and a TNCS that supports this function SHOULD call this method 
	 * for a particular IMV and connection instead of calling 
	 * receiveMessage if the IMV implements this method and the 
	 * connection uses IF-TNCCS-SOH. A TNCS MUST NOT call this method 
	 * for a particular IMV and connection if the IMV does not implement
	 * this function or the connection does not use IF-TNCCS-SOH. A 
	 * TNCS will often find that some IMVs loaded by that TNCS implement
	 * this function and some do not. In that case, when the TNCS is 
	 * handling a connection that uses IF-TNCCS-SOH, the TNCS would 
	 * call receiveMessageSOH for the IMVs that implement this method 
	 * and receiveMessage for the IMVs that do not implement 
	 * receiveMessageSOH. A TNCS MUST NOT call both receiveMessage and
	 * receiveMessageSOH for a single SOHReportEntry for a single IMV.
	 * <p>
	 * IMVs are not required to implement this method but any IMV that 
	 * implements the IMVSOH interface MUST implement this method. An 
	 * IMV should implement receiveMessageSOH if it wants to receive 
	 * the entire SOHReportEntry instead of just the Vendor-Specific 
	 * attribute when IF-TNCCS-SOH is used. However, IMVs should 
	 * recognize that many TNCSs do not support IF-TNCCS-SOH and some 
	 * TNCSs that do support IF-TNCCS-SOH will not support this 
	 * function. Therefore, IMVs should be prepared to receive messages
	 * via receiveMessage in some cases when IF-TNCC-SOH is used. 
	 * Simple IMVs that do not wish to receive the entire SOHReportEntry 
	 * need not implement this function. IMV implementers may find that 
	 * implementing receiveMessageSOH is more complex than implementing 
	 * receiveMessage since receiveMessageSOH must parse the 
	 * SOHReportEntrywhile  receiveMessage only needs to parse the 
	 * contents of the Vendor-Specific attribute within the 
	 * SOHReportEntry. For many IMVs, the contents of the 
	 * Vendor-Specific attribute is all that they need. Therefore, 
	 * IMVs are never required to implement receiveMessageSOH.
	 * <p>
	 * The contents of the SOHReportEntry is contained in the buffer 
	 * referenced by sohReportEntry. This parameter may be null to 
	 * represent an empty message. The contents of the first 
	 * System-Health-ID attribute in the SOHReportEntry is indicated 
	 * by systemHealthID. This value MUST match one of the message 
	 * type values previously supplied by the IMV to the TNCS in the 
	 * IMV's most recent call to the TNCS's reportMessageTypes() or 
	 * reportMessageTypesLong() methods. IMVs MAY check these parameters
	 * to make sure they are valid and throw an exception if not, but 
	 * IMVs are not required to make these checks.
	 * <p>
	 * The IMV SHOULD send any IMC-IMV messages it wants to send as 
	 * soon as possible after this method is called and then return 
	 * from this method to indicate that it is finished sending 
	 * messages in response to this message.
	 * <p>
	 * As with all IMV methods, the IMV SHOULD NOT wait a long time 
	 * before returning from receiveMessageSOH. To do otherwise would 
	 * risk delaying the handshake indefinitely. A long delay might 
	 * frustrate users or exceed network timeouts (PDP, PEP or 
	 * otherwise).
	 * <p>
	 * The IMV MUST NOT ever modify the buffer contents and MUST NOT 
	 * access the buffer after receiveMessageSOH has returned. If the 
	 * IMV wants to retain the contents of the SOHReportEntry, it 
	 * should copy it before returning from receiveMessageSOH.
	 * <p>
	 * @param c the IMV connection object
	 * @param systemHealthID the type of message to be delivered
	 * @param sohReportEntry the message to be delivered
	 * 
	 * @throws TNCException if a TNC error occurs
	 */
	void receiveMessageSOH(IMVConnection c,
						   long systemHealthID,
						   byte[] sohReportEntry) throws TNCException;
}
