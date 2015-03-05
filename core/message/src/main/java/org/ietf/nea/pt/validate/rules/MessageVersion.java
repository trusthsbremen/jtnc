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
package org.ietf.nea.pt.validate.rules;

import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
/**
 * Rule, that checks if the given message version is supported.
 * @author Carl-Heinz Genzel
 *
 */
public abstract class MessageVersion {
    /**
     * Private constructor should never be invoked.
     */
    private MessageVersion() {
        throw new AssertionError();
    }

    /**
     * Checks if the given message version is supported.
     * @param actualVersion the actual version
     * @param supportedVersion the supported version
     * @throws RuleException if check fails
     */
    public static void check(final short actualVersion,
            final short supportedVersion) throws RuleException {
        if (actualVersion != supportedVersion) {
            // the version parameter are added as reasons first actual, than
            // supported. This is only by convention!
            throw new RuleException("The version " + actualVersion
                    + " is not supported.", true,
                    PtTlsMessageErrorCodeEnum.IETF_UNSUPPORTED_VERSION.code(),
                    PtTlsErrorCauseEnum.MESSAGE_TYPE_NOT_SUPPORTED.id(),
                    actualVersion, supportedVersion);
        }
    }

}
