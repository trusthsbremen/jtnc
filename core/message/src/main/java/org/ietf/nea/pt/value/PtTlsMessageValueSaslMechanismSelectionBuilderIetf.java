package org.ietf.nea.pt.value;

import org.ietf.nea.pt.validate.rules.SaslMechanismName;
import org.ietf.nea.pt.value.util.SaslMechanism;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class PtTlsMessageValueSaslMechanismSelectionBuilderIetf implements
PtTlsMessageValueSaslMechanismSelectionBuilder {
	
	private static final byte LENGTH_FIELDS_AND_RESERVED_LENGTH = 1;
	
	private long length;
	private SaslMechanism mechanism;
	private byte[] initialSaslMsg;
	
	public PtTlsMessageValueSaslMechanismSelectionBuilderIetf(){
		this.length = 0;
		this.mechanism = null;
		this.initialSaslMsg = null;
	}

	@Override
	public PtTlsMessageValueSaslMechanismSelectionBuilder setMechanism(SaslMechanism mech) throws RuleException {
		
		
		if(mech != null){
			SaslMechanismName.check(mech.getName());
			this.mechanism = mech;
		}
		
		return this;
	}

	@Override
	public PtTlsMessageValueSaslMechanismSelectionBuilder setInitialSaslMessage(byte[] initialSaslMsg) throws RuleException {
		
		
		if(initialSaslMsg != null){
			this.initialSaslMsg = initialSaslMsg;
		}
		
		return this;
	}
	
	
	@Override
	public PtTlsMessageValueSaslMechanismSelection toObject(){
		
		if(this.mechanism == null){
			throw new IllegalStateException("The SASL mechanism has to be set.");
		}
		
		this.length = (this.mechanism.getNameLength() + LENGTH_FIELDS_AND_RESERVED_LENGTH) ;
		
		if(this.initialSaslMsg != null){
			this.length += this.initialSaslMsg.length;
			return new PtTlsMessageValueSaslMechanismSelection(this.length,this.mechanism,this.initialSaslMsg);
		}else{
			return new PtTlsMessageValueSaslMechanismSelection(this.length,this.mechanism);
		}
	}

	@Override
	public PtTlsMessageValueSaslMechanismSelectionBuilder newInstance() {
		return new PtTlsMessageValueSaslMechanismSelectionBuilderIetf();
	}

}
