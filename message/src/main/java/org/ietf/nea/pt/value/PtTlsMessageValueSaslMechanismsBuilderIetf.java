package org.ietf.nea.pt.value;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pt.validate.rules.SaslMechanismName;
import org.ietf.nea.pt.value.util.SaslMechanism;

public class PtTlsMessageValueSaslMechanismsBuilderIetf implements
PtTlsMessageValueSaslMechanismsBuilder {
	
	private static final byte LENGTH_FIELDS_AND_RESERVED_LENGTH = 1;
	
	private long length;
	private List<SaslMechanism> mechanisms;
	
	public PtTlsMessageValueSaslMechanismsBuilderIetf(){
		this.length = 0;
		this.mechanisms = new LinkedList<>();
	}

	@Override
	public PtTlsMessageValueSaslMechanismsBuilder addMechanism(SaslMechanism mech1, SaslMechanism... mechs) throws RuleException {
		
		List<SaslMechanism> temp = new ArrayList<>();
		
		if(mech1 != null){
			SaslMechanismName.check(mech1.getName());
			temp.add(mech1);
		}
		
		if(mechs != null){
			for (SaslMechanism mech : mechs) {
				if(mech != null){
					SaslMechanismName.check(mech.getName());
					temp.add(mech);
				}
			}
		}

		this.mechanisms.addAll(temp);
		this.updateLength();
		
		return this;
	}

	private void updateLength() {
		this.length = 0;
		for (SaslMechanism mech : this.mechanisms) {
			this.length += (mech.getNameLength() + LENGTH_FIELDS_AND_RESERVED_LENGTH) ; // 1 bytes for length values
		}
	}

	@Override
	public PtTlsMessageValueSaslMechanisms toObject(){
		
		return new PtTlsMessageValueSaslMechanisms(this.length, this.mechanisms);
	}

	@Override
	public PtTlsMessageValueSaslMechanismsBuilder newInstance() {
		return new PtTlsMessageValueSaslMechanismsBuilderIetf();
	}

}
