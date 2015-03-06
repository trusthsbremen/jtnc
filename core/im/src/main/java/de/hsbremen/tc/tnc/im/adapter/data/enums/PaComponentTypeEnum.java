/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Carl-Heinz Genzel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
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
