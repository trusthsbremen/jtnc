package de.hsbremen.tc.tnc.im.adapter.data;


public abstract class AbstractImComponent {

	
	private final long vendorId;
	private final long type;
	private final long collectorId;
	private final long validatorId;

	AbstractImComponent(final long vendorId, final long type,
			final long collectorId, final long validatorId) {
		
		this.vendorId = vendorId;
		this.type = type;
		this.collectorId = collectorId;
		this.validatorId = validatorId;
		
	}

	/**
	 * @return the subVendorId
	 */
	public long getVendorId() {
	    return vendorId;
	}

	/**
	 * @return the subType
	 */
	public long getType() {
	    return type;
	}

	/**
	 * @return the collectorId
	 */
	public long getCollectorId() {
	    return collectorId;
	}

	/**
	 * @return the validatorId
	 */
	public long getValidatorId() {
	    return validatorId;
	}

}