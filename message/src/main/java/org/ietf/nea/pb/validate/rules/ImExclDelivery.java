package org.ietf.nea.pb.validate.rules;

import java.util.Set;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class ImExclDelivery {

	public static void check(final Set<PbMessageImFlagsEnum> flags, final long deliveryAddressId) throws ValidationException {
        // check if exclusive flag is set, but no receiver address was set.
    	// TODO has to be checked in interoperability test if this does not always evaluate true.
        if(flags.contains(PbMessageImFlagsEnum.EXCL) && deliveryAddressId == TNCConstants.TNC_IMCID_ANY){
            throw new ValidationException("Exclusive flag found, but address id set to ANY.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.EXCL_DELIVERY_NOT_POSSIBLE.number(),Long.toString(deliveryAddressId)); 
        }
    }
	
}
