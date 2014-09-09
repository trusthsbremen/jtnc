package org.ietf.nea.pb.validate;

import org.ietf.nea.IETFConstants;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageValueLanguagePreference;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.ietf.nea.pb.validate.rules.MinMessageLength;
import org.ietf.nea.pb.validate.rules.NoNoSkip;
import org.ietf.nea.pb.validate.rules.NoNullTerminatedString;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.validate.TnccsValidator;

public class PbMessageLanguagePreferenceValidator implements
        TnccsValidator<PbMessage> {

	private static final class Singleton{
		private static final PbMessageLanguagePreferenceValidator INSTANCE = new PbMessageLanguagePreferenceValidator();  
	}
	public static PbMessageLanguagePreferenceValidator getInstance(){
	   return Singleton.INSTANCE;
	}
 
    private PbMessageLanguagePreferenceValidator() {
    	// Singleton
    }
    
    @Override
    public void validate(final PbMessage message) throws ValidationException {
    	if(message.getVendorId() != IETFConstants.IETF_PEN_VENDORID || message.getType() != PbMessageTypeEnum.IETF_PB_LANGUAGE_PREFERENCE.messageType()){
    		throw new IllegalArgumentException("Unsupported message type with vendor ID "+ message.getVendorId() +" and message type "+ message.getType() +".");
    	}
    	long fixedMessageLength = PbMessage.FIXED_LENGTH;
    	NoNoSkip.check(message.getFlags());
		// TODO regular expression test for language string (RFC 2234).
		// No Null termination is one thing of that.
		NoNullTerminatedString.check(((PbMessageValueLanguagePreference)message.getValue()).getPreferedLanguage());
		MinMessageLength.check(message.getLength(), fixedMessageLength);
    }
    
	@Override
	public void addValidator(final Long vendorId, final Long messageType,
			final TnccsValidator<PbMessage> validator) {
		throw new UnsupportedOperationException("Method is not supported by this implementation.");
	}

	@Override
	public void removeValidator(final Long vendorId, final Long messageType) {
		throw new UnsupportedOperationException("Method is not supported by this implementation.");
	}

   
}
