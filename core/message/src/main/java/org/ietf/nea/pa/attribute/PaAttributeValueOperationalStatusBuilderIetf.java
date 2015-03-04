package org.ietf.nea.pa.attribute;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeOperationLastResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeOperationStatusEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;
import org.ietf.nea.pa.validate.rules.LastUseSyntaxCheck;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class PaAttributeValueOperationalStatusBuilderIetf implements
	PaAttributeValueOperationalStatusBuilder {
	
	
	private long length;
	private PaAttributeOperationStatusEnum status;
	private PaAttributeOperationLastResultEnum result;
	private Date lastUse;
	private SimpleDateFormat dateFormater;
	
	public PaAttributeValueOperationalStatusBuilderIetf(){
		this.length = PaAttributeTlvFixedLengthEnum.OP_STAT.length();
		this.status = PaAttributeOperationStatusEnum.UNKNOWN;
		this.result = PaAttributeOperationLastResultEnum.UNKNOWN;
		this.lastUse = null;
		
		this.dateFormater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		this.dateFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	@Override
	public void setStatus(short status) {
		// defaults to unknown
		this.status = PaAttributeOperationStatusEnum.fromId(status);
	}

	@Override
	public void setResult(short result) {
		// defaults to unknown
		this.result = PaAttributeOperationLastResultEnum.fromCode(result);
	}

	@Override
	public void setLastUse(String dateTime) throws RuleException {
		if(dateTime != null && !dateTime.equals("0000-00-00T00:00:00Z")){
			LastUseSyntaxCheck.check(dateTime);
			try{
				Date d = this.dateFormater.parse(dateTime);
				this.lastUse = d;
			} catch (ParseException e) {
				// should never happen because of the check before.
				throw new RuleException("Time format: " + dateTime + " could not be parsed.",e, false,PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.TIME_FORMAT_NOT_VALID.number(),dateTime);
			}
			
		}
	}		

	@Override
	public PaAttributeValueOperationalStatus toObject(){
		
		return new PaAttributeValueOperationalStatus(this.length, this.status, this.result, this.lastUse);
	}

	@Override
	public PaAttributeValueOperationalStatusBuilder newInstance() {

		return new PaAttributeValueOperationalStatusBuilderIetf();
	}

}
