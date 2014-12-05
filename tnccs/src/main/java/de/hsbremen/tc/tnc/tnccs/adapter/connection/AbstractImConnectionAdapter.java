package de.hsbremen.tc.tnc.tnccs.adapter.connection;


public class AbstractImConnectionAdapter implements ImConnectionAdapter {

	private boolean receiving;
	private final int primaryImcId;

	public AbstractImConnectionAdapter(int primaryImcId) {
		this.primaryImcId = primaryImcId;
		this.receiving = false;
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.adapter.connection.ImConnectionAdapter#getId()
	 */
	@Override
	public long getImId() {
		return this.primaryImcId;
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.adapter.connection.ImConnectionAdapter#allowSending()
	 */
	@Override
	public void allowMessageReceipt(){
		this.receiving = true;	
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.adapter.connection.ImConnectionAdapter#denySending()
	 */
	@Override
	public void denyMessageReceipt(){
		this.receiving = false;
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.adapter.connection.ImConnectionAdapter#isSending()
	 */
	@Override
	public boolean isReceiving(){
		return this.receiving;
	}
}
