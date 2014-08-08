package org.trustedcomputinggroup.tnc.ifimc;

import org.trustedcomputinggroup.tnc.TNCException;
/**
 * These methods are implemented by the TNC Client and 
 * called by the IMC.
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
public interface TNCC {

    /**
     * A call to this method is used to inform a TNCC about the 
     * set of message types the IMC is able to receive. Often, 
     * the IMC will call this method from the IMC's initialize method. 
     * A list of message types is contained in the 
     * supportedTypes parameter. The supportedTypes parameter 
     * may be null to represent no message types. 
     * <p>
     * All TNC Clients MUST implement this method. 
     * The TNC Client MUST NOT ever modify the list of 
     * message types and MUST NOT access this list after 
     * TNCC reportMessageTypes() method has returned. Generally, 
     * the TNC Client will copy the contents of this list 
     * before returning from this method. TNC Clients 
     * MUST support any message type.
     * <p>
     * Note that although all TNC Clients must implement 
     * this method, some IMCs may never call it if they 
     * don't support receiving any message types. This is 
     * acceptable. In such a case, the TNC Client MUST NOT 
     * deliver any messages to the IMC.
     * <p>
     * If an IMC requests a message type whose vendor ID is 
	 * TNC_VENDORID_ANY and whose subtype is TNC_SUBTYPE_ANY 
	 * it will receive all messages with any message type, 
	 * except for messages marked for exclusive delivery to 
	 * another IMC. If an IMC requests a message type whose 
	 * vendor ID is not TNC_VENDORID_ANY and whose subtype is
	 * TNC_SUBTYPE_ANY, it will receive all messages with the 
	 * specified vendor ID and any subtype, except for messages 
	 * marked for exclusive delivery to another IMC. If an IMC 
	 * calls the TNCC's reportMessageTypes or reportMessageTypesLong 
	 * method more than once, the message type list supplied in 
	 * the latest call supplants the message type lists supplied 
	 * in earlier calls.
     * <p>
     * @param imc an IMC reporting it message types
     * @param supportedTypes the messages types an IMC can support
     *
     * @exception TNCException if a TNC error occurs
     */

	public void reportMessageTypes(IMC imc, long [] supportedTypes) throws TNCException;
 
    /**
     * IMCs can call this method to ask a TNCC to 
     * retry an Integrity Check Handshake 
     * for all current network connections.  
     * The IMC MUST pass itself as 
     * the imc parameter and one of the handshake retry reasons
     * in IMCConnection as the reason parameter. 
     * <p>
     * TNCCs MAY check the parameters to make sure they are 
     * valid and return an error if not, but TNCCs are not 
     * required to make these checks. The reason parameter 
     * explains why the IMC is requesting a handshake retry. 
     * The TNCC MAY use this in deciding whether to attempt 
     * the handshake retry. TNCCs are not required to honor 
     * IMC requests for handshake retry (especially since 
     * handshake retry may not be possible or may interrupt 
     * network connectivity). An IMC MAY call this method at 
     * any time, even if an Integrity Check Handshake is 
     * currently underway. This is useful if the IMC suddenly 
     * gets important information but has already finished its 
     * dialog with the IMV, for instance. As always, the TNCC 
     * is not required to honor the request for handshake 
     * retry.
     * <p>
     * If the TNCC cannot attempt the handshake retry, 
     * it SHOULD throw a TNCException with result code
     * TNC_RESULT_CANT_RETRY. If 
     * the TNCC could attempt to retry the handshake but 
     * chooses not to, it SHOULD throw a TNCException with result code
     * TNC_RESULT_WONT_RETRY. The IMC MAY use this information 
     * in displaying diagnostic and progress messages.
     *  <p>
     * @param imc an IMC requesting a retry handshake
     * @param reason the reason for the handshake request
     *
     * @exception TNCException if a TNC error occurs
     */

	public void requestHandshakeRetry(IMC imc, long reason) throws TNCException;

}
