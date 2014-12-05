package de.hsbremen.tc.tnc.im.adapter.data;

import java.util.List;

import de.hsbremen.tc.tnc.im.adapter.data.enums.ImComponentFlagsEnum;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;

public class ImFaultyObjectComponent extends ImObjectComponent {

	private final List<ValidationException> exceptions;
	private final byte[] messageHeader;
	
	ImFaultyObjectComponent(ImComponentFlagsEnum[] flags, long subVendorId,
			long subType, long collectorId, long validatorId,
			List<? extends ImAttribute> attributes, List<ValidationException> exceptions, byte[] messageHeader) {
		super(flags, subVendorId, subType, collectorId, validatorId, attributes);
		this.exceptions = exceptions;
		this.messageHeader = messageHeader;
	}

	/**
	 * @return the exceptions
	 */
	public List<ValidationException> getExceptions() {
		return this.exceptions;
	}

	/**
	 * @return the messageHeader
	 */
	public byte[] getMessageHeader() {
		return this.messageHeader;
	}
	
	

}
