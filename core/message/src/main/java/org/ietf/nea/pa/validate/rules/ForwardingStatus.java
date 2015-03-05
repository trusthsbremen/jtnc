package org.ietf.nea.pa.validate.rules;

import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeForwardingStatusEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
/**
 * Rule, that checks if traffic forwarding status ID is known.
 * @author Carl-Heinz Genzel
 *
 */
public abstract class ForwardingStatus {
    /**
     * Private constructor should never be invoked.
     */
    private ForwardingStatus() {
        throw new AssertionError();
    }

    /**
     * Checks if traffic forwarding status ID is known.
     * @param status the status ID
     * @throws RuleException if check fails
     */
    public static void check(final long status) throws RuleException {
        if (PaAttributeForwardingStatusEnum.fromId(status) == null) {
            throw new RuleException(
                    "The type value " + status + " is unknown.", false,
                    PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                    PaErrorCauseEnum.FORWARDING_STATUS_NOT_SUPPORTED.id(),
                    status);
        }
    }

}
