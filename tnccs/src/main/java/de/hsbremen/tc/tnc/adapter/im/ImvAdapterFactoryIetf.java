package de.hsbremen.tc.tnc.adapter.im;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.IMVLong;
import org.trustedcomputinggroup.tnc.ifimv.IMVTNCSFirst;

public class ImvAdapterFactoryIetf implements ImvAdapterFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImvAdapterFactory.class);


	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapterFactory#createConnectionAdapter(org.trustedcomputinggroup.tnc.ifimc.IMCConnection)
	 */
	@Override
	public ImvAdapter createImvAdapter(IMV imv, long primaryId) {
		

		if(imv == null){
			throw new NullPointerException("IMV cannot be null.");
		}
		
		ImvAdapter adapter = new ImvAdapterIetf(imv, primaryId);
		
		if(LOGGER.isDebugEnabled()){
			StringBuilder b = new StringBuilder();
			b.append("IMV adapter created for IMC ")
			.append(imv.toString())
			.append(" with the following supported abilities: \n")
			.append("TNCS first support: ").append((imv instanceof IMVTNCSFirst)).append("\n")
			.append("Long support: ").append((imv instanceof IMVLong)).append("\n");
			LOGGER.debug(b.toString());
		}
		
		return adapter;
	}
	
	
}
