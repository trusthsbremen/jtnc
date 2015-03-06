/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright (c) 2015, Carl-Heinz Genzel
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.hsbremen.tc.tnc.im.adapter.data.enums;


/**
 * Enumeration of known component types.
 *
 *
 */
public enum PaComponentTypeEnum {

    /* IETF */
    /**
     * Testing component type.
     */
    IETF_PA_TESTING(0),
    /**
     * Operating system component type.
     */
    IETF_PA_OPERATING_SYSTEM(1),
    /**
     * Anti-virus component type.
     */
    IETF_PA_ANTI_VIRUS(2),
    /**
     * Anti-spyware component type.
     */
    IETF_PA_ANTI_SPYWARE(3),
    /**
     * Anti-malware component type.
     */
    IETF_PA_ANTI_MALWARE(4),
    /**
     * Firewall component type.
     */
    IETF_PA_FIREWALL(5),
    /**
     * Intrusion detection/prevention component type.
     */
    IETF_PA_IDPS(6),
    /**
     * VPN component type.
     */
    IETF_PA_VPN(7),
    /**
     * NEA client component type.
     */
    IETF_PA_NEA_CLIENT(8),

    /**
     * Reserved (not specified in RFC) component type.
     */
    IETF_PA_RESERVED(0xffffffff);

    private long id;

    /**
     * Creates a component type with an ID.
     *
     * @param id the type ID
     */
    private PaComponentTypeEnum(final long id) {
        this.id = id;
    }

    /**
     * Returns the component type ID.
     *
     * @return the type ID
     */
    public long id() {
        return this.id;
    }

    /**
     * Returns the type for the given type ID.
     *
     * @param id the type ID
     * @return a type or null
     */
    public static PaComponentTypeEnum fromId(final long id) {

        if (id == IETF_PA_TESTING.id) {
            return IETF_PA_TESTING;
        }

        if (id == IETF_PA_OPERATING_SYSTEM.id) {
            return IETF_PA_OPERATING_SYSTEM;
        }

        if (id == IETF_PA_ANTI_VIRUS.id) {
            return IETF_PA_ANTI_VIRUS;
        }

        if (id == IETF_PA_ANTI_SPYWARE.id) {
            return IETF_PA_ANTI_SPYWARE;
        }

        if (id == IETF_PA_ANTI_MALWARE.id) {
            return IETF_PA_ANTI_MALWARE;
        }

        if (id == IETF_PA_FIREWALL.id) {
            return IETF_PA_FIREWALL;
        }

        if (id == IETF_PA_IDPS.id) {
            return IETF_PA_IDPS;
        }

        if (id == IETF_PA_VPN.id) {
            return IETF_PA_VPN;
        }

        if (id == IETF_PA_NEA_CLIENT.id) {
            return IETF_PA_NEA_CLIENT;
        }

        if (id == IETF_PA_RESERVED.id) {
            return IETF_PA_RESERVED;
        }

        return null;
    }

}
