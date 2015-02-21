package org.ietf.nea.pa.attribute;

import java.util.Date;

import org.ietf.nea.pa.attribute.enums.PaAttributeOperationLastResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeOperationStatusEnum;


public class PaAttributeValueOperationalStatus extends AbstractPaAttributeValue {

    private final PaAttributeOperationStatusEnum status;           // 8 bit(s)
    private final PaAttributeOperationLastResultEnum result;       // 8 bit(s)
    private final Date lastUse;        							   // 160 bit(s) RFC 3339 formatted YYYY-MM-DDTHH:MM:SSZ

	PaAttributeValueOperationalStatus(long length,
			PaAttributeOperationStatusEnum status,
			PaAttributeOperationLastResultEnum result, Date lastUse) {
		super(length);
		this.status = status;
		this.result = result;
		this.lastUse = lastUse;
	}

	/**
	 * @return the status
	 */
	public PaAttributeOperationStatusEnum getStatus() {
		return this.status;
	}

	/**
	 * @return the result
	 */
	public PaAttributeOperationLastResultEnum getResult() {
		return this.result;
	}

	/**
	 * @return the lastUse
	 */
	public Date getLastUse() {
		return this.lastUse;
	}
	
}
