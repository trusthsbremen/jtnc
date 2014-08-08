package org.trustedcomputinggroup.tnc;
/**
* A collection of well known or common constants 
* to be used by the IMC and IMV packages.
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
public interface TNCConstants {
	
    /**
     * Network connection created.
     */

	public static final long TNC_CONNECTION_STATE_CREATE = 0;
    
	/**
     * Handshake about to start.
     */

	public static final long TNC_CONNECTION_STATE_HANDSHAKE = 1;
    
	/**
     * Handshake completed. TNC Server recommended that requested 
     * access be allowed.
     */
	
	public static final long TNC_CONNECTION_STATE_ACCESS_ALLOWED = 2;
    
	/**
     * 
     * Handshake completed. TNC Server recommended that isolated 
     * access be allowed.
     */
 
	public static final long TNC_CONNECTION_STATE_ACCESS_ISOLATED = 3;
	
	/**
     * Handshake completed. TNCS Server recommended that no network 
     * access be allowed.
     */
	
   public static final long TNC_CONNECTION_STATE_ACCESS_NONE = 4;
   
   /**
    * About to delete network connection. Remove all associated 
    * state.
    */
   
    public static final long TNC_CONNECTION_STATE_DELETE = 5;

    /**
     * Reserved for older TCG-defined values.
     */

    public static final long TNC_VENDORID_TCG = 0;

    /**
     * Reserved for newer TCG-defined values.
     */

    public static final long TNC_VENDORID_TCG_NEW = 0x005597;
    
    /**
     * Wild card matching any vendor ID.
     */

    public static final long TNC_VENDORID_ANY = 0xffffff;
    
    /**
     * Wild card matching any message subtype.
     */

    public static final long TNC_SUBTYPE_ANY = 0xff;

    /**
     * Message flag specifying whether connection supports exclusive
	 * delivery of messages to IMC.
     */

	public static final long TNC_MESSAGE_FLAGS_EXCLUSIVE = 0x80000000;

    /**
	 * Reserved value for IMC ID.
     */

	public static final long TNC_IMCID_ANY = 0xffff;

    /**
     * Reserved value for IMV ID.
     */

	public static final long TNC_IMVID_ANY = 0xffff;
}
