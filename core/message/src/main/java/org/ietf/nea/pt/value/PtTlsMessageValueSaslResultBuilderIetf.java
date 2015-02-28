package org.ietf.nea.pt.value;

import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.validate.rules.SaslResult;
import org.ietf.nea.pt.value.enums.PtTlsSaslResultEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class PtTlsMessageValueSaslResultBuilderIetf implements
PtTlsMessageValueSaslResultBuilder {
	
	private long length;
	private PtTlsSaslResultEnum result;
    private byte[] resultData;
	
	public PtTlsMessageValueSaslResultBuilderIetf(){
		this.length = PtTlsMessageTlvFixedLengthEnum.SASL_RLT.length();
		this.result = PtTlsSaslResultEnum.ABORT;
		this.resultData = null;
	}

	@Override
	public PtTlsMessageValueSaslResultBuilder setResult(int result) throws RuleException {
		
		
		SaslResult.check(result);
		this.result = PtTlsSaslResultEnum.fromCode(result);
		
		return this;
	}

	@Override
	public PtTlsMessageValueSaslResultBuilder setOptionalResultData(byte[] resultData) throws RuleException {
		
		
		if(resultData != null){
			this.resultData = resultData;
		}
		
		return this;
	}
	
	
	@Override
	public PtTlsMessageValueSaslResult toObject(){
		
		
		if(this.resultData != null){
			this.length += this.resultData.length;
			return new PtTlsMessageValueSaslResult(this.length, this.result, this.resultData);
		}else{
			return new PtTlsMessageValueSaslResult(this.length,this.result);
		}
	}

	@Override
	public PtTlsMessageValueSaslResultBuilder newInstance() {
		return new PtTlsMessageValueSaslResultBuilderIetf();
	}

}
