package org.trustedcomputinggroup.tnc.ifimc;

/**
 * This interface can be implemented by an object that already supports
 * the IMCConnection interface to indicate that this object is an 
 * IMCConnection that supports sendMessageSOH.  To check whether an 
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
public interface IMCConnectionSOH {

   /**
    * An IMC calls this method (if implemented by the TNCC) to give a 
	* SoHReportEntry to the TNCC for delivery. This allows an IMC to 
	* send an entire SoHReportEntry instead of just the contents of 
	* the Vendor-Specific attribute, as would be the case if the 
	* sendMessage method was used.
    * <p>
    * The SoHReportEntry (as defined in section 3.5 of [9]) is 
	* contained in the buffer referenced by the sohReportEntry 
	* parameter. If the length of the sohReportEntry parameter is 
	* zero (0), the TNCC MUST throw a TNCException with a 
	* TNC_RESULT_INVALID_PARAMETER result since a zero-length 
	* SoHReportEntry is prohibited by the IF_TNCCS-SOH 1.0 
	* specification. No type is included with the SoHReportEntry since
	* this is included in the System-Health-ID attribute in the 
	* SoHReportEntry. TNCCs MAY check these values to make sure they 
	* are valid and throw a TNCException with a 
	* TNC_RESULT_INVALID_PARAMETER result if not, but TNCCs are not 
	* required to make these checks.
    * <p>
    * A TNCC that supports IF-TNCCS-SOH SHOULD support this method. 
	* The TNC Client MUST NOT ever modify the buffer contents and MUST
	* NOT access the buffer after sendMessageSOH has returned. The TNC
	* Client will typically copy the SoHReportEntry out of the buffer,
	* place it into the SoH message or queue it up for such placement,
	* and return from this method.
    * <p>
    * An IMC may call this method if it needs to send an 
	* SoHReportEntry with a connection that supports IF-TNCCS-SOH (as 
	* indicated by the Has SOH attribute being set to a value of 1). 
	* If an IMC calls this method for a connection that does not 
	* support IF-TNCCS-SOH, the TNCC SHOULD throw a TNCException with 
	* a TNC_RESULT_NO_SOH_SUPPORT result. IMCs should recognize that 
	* many TNCCs do not support IF-TNCCS-SOH and some TNCCs that do 
	* support IF-TNCCS-SOH will not support this method. Therefore, 
	* IMCs should be prepared to send messages via sendMessage in some
	* cases when IF-TNCC-SOH is used. Simple IMCs that only need to 
	* send simple messages need not call this method. They can just 
	* call sendMessage, which will automatically place their message 
	* in a Vendor-Specific attribute in an SoHReportEntry.
    * <p>
    * The IMC MUST NOT call this method unless it has received a call 
	* to beginHandshake, receiveMessage, receiveMessageSOH, 
	* receiveMessageLong, or batchEnding for this connection and the 
	* IMC has not yet returned from that method. If the IMC violates 
	* this prohibition, the TNCC SHOULD throw 
	* TNC_RESULT_ILLEGAL_OPERATION exception. If an IMC really wants 
	* to communicate with an IMV at another time, it should call 
	* requestHandshakeRetry.
    * <p>
    * Note that a TNCC or TNCS MAY cut off IMC-IMV communications at 
	* any time for any reason, including limited support for long 
	* conversations in underlying protocols, user or administrator 
	* intervention, or policy. If this happens, the TNCC will throw a 
	* TNCException with a TNC_RESULT_ILLEGAL_OPERATION result.
    * <p>
    * If the TNCC supports limiting the message size or number of 
	* round trips and the limits are exceeded, the TNCC MUST throw a 
	* TNCException with a result of 
	* TNC_RESULT_EXCEEDED_MAX_MESSAGE_SIZE or 
	* TNC_RESULT_EXCEEDED_MAX_ROUND_TRIPS as the case may be.  An IMC 
	* can get the Maximum Message Size and Maximum Number of Round 
	* Trips by using the related Attribute ID within the getAttribute 
	* method.  The IMC SHOULD adapt its behavior to accommodate these 
	* limitations if available.
    * <p>
    * @param sohReportEntry sohReportEntry to be delivered
    *
    * @exception TNCException if a TNC error occurs
    */
    public void sendMessageSOH(byte[] sohReportEntry) throws TNCException;
}
