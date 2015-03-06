package org.trustedcomputinggroup.tnc.ifimv;

/**
 * This interface can be implemented by an object that already supports
 * the IMV interface to indicate that this object is an IMV that 
 * supports beginHandshake. To check whether an object implements this 
 * interface use the <code>instanceof</code> operator. If so, cast the
 * object to the interface type and use the methods in this interface.
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
public interface IMVTNCSFirst {

	/**
	 * The TNC Server calls this method to indicate that an Integrity 
	 * Check Handshake is beginning and solicits messages from IMVs 
	 * for the first batch. The IMV SHOULD send any IMC-IMV messages 
	 * it wants to send as soon as possible after this method is 
	 * called and then return from this method to indicate that it is 
	 * finished sending messages for this batch.
	 * <p>
	 * IMVs are not required to implement this method but any IMV that 
	 * implements the IMVTNCSFirst interface MUST implement this 
	 * method. An IMV should implement this method if it supports 
	 * having the TNCS send the first batch of IMC-IMV messages in the
	 * TNC handshake. This is useful when an IMV must send instructions
	 * to the IMC before the IMC can send useful integrity messages. 
	 * Simple IMVs that do not wish to send an initial message to the 
	 * IMC need not implement this method.
	 * <p>
	 * Since this method was not included in IF-IMV 1.2, many IMVs do 
	 * not implement it. TNCSs MUST work properly if an IMV does not 
	 * implement this method. Likewise, many TNCSs do not implement 
	 * this method so IMVs MUST work properly if a TNCS does not 
	 * implement this method. If an IMV implements this method, the 
	 * TNCS SHOULD call this method if the TNCS is preparing to send 
	 * the first batch of messages in a TNC handshake. Even if the 
	 * TNCS and IMV both support this method and have used it for 
	 * previous handshakes, a particular handshake may not involve 
	 * having the TNCS send the first batch of messages. In that case,
	 * this method will not be called for that handshake. The TNCS and
	 * IMV MUST properly handle the situation where this method is 
	 * supported by both the TNCS and IMV but is not called for a 
	 * particular handshake because the TNCS will not be sending the 
	 * first batch of messages.
	 * <p>
	 * As with all IMV methods, the IMV SHOULD NOT wait a long time 
	 * before returning from beginHandshake. To do otherwise would 
	 * risk delaying the handshake indefinitely. A long delay might 
	 * frustrate users or exceed network timeouts (PDP, PEP or 
	 * otherwise).
	 * <p>
	 * @param c the IMVConnection object
	 *
	 * @throws TNCException if a TNC error occurs
	 */
	void beginHandshake(IMVConnection c) throws TNCException;
}
