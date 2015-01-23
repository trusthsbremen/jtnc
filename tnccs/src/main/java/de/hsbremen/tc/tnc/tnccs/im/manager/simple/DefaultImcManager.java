package de.hsbremen.tc.tnc.tnccs.im.manager.simple;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.attribute.TncClientAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImcAdapter;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImcAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.im.exception.TerminatedException;
import de.hsbremen.tc.tnc.tnccs.adapter.tnccs.TnccAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.im.manager.AbstractImManager;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImcManager;
import de.hsbremen.tc.tnc.tnccs.im.route.ImMessageRouter;

public class DefaultImcManager extends AbstractImManager<IMC> implements ImcManager{

//	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultImcManager2.class);
	
	private ImcAdapterFactory adapterFactory;
	private final TnccAdapterFactory tnccFactory;
	private final ImMessageRouter router;

	private Map<Long,ImcAdapter> adapterIndex;  
	
	public DefaultImcManager(ImMessageRouter router, ImcAdapterFactory adapterFactory, TnccAdapterFactory tnccFactory){
		this(router, adapterFactory, tnccFactory, (TNCConstants.TNC_IMCID_ANY -1));
	}
	
	public DefaultImcManager(ImMessageRouter router, ImcAdapterFactory adapterFactory, TnccAdapterFactory tnccFactory, long maxImId) {
		
		super(router, maxImId);
		
		this.router = router;
		
		this.adapterFactory = adapterFactory;
		this.tnccFactory = tnccFactory;
		
		this.adapterIndex = new ConcurrentHashMap<>();
		
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.newp.AbstractImManager#initialize(long, java.lang.Object)
	 */
	@Override
	protected void initialize(long primaryId, IMC im) throws TncException {
		TNCC tncc = this.tnccFactory.createTncc(im, this.createPrimaryIdAttribute(primaryId), this);
		
		try {
			im.initialize(tncc);
		} catch (TNCException e) {
			throw new TncException(e);
		}
		
		ImcAdapter adapter = this.adapterFactory.createImcAdapter(im, primaryId);
		this.adapterIndex.put(primaryId, adapter);
		
	}

	@Override
	public Map<Long,ImcAdapter> getAdapter() {
		return new HashMap<>(this.adapterIndex);
	}

	@Override
	public ImMessageRouter getRouter() {
		return this.router;
	}

	
	private Attributed createPrimaryIdAttribute(final long primaryId) {
		Attributed a = new Attributed(){
			
			private long primaryImcId = primaryId;
			
			@Override
			public void setAttribute(TncAttributeType type, Object value)
					throws TncException {
				throw new UnsupportedOperationException("The operation setAttribute(...) is not supported, because there are no attributes to set.");
				
			}
			
			@Override
			public Object getAttribute(TncAttributeType type) throws TncException {
				if(type.id() == TncClientAttributeTypeEnum.TNC_ATTRIBUTEID_PRIMARY_IMC_ID.id()){
					return new Long(this.primaryImcId);
				}
				throw new TncException("The attribute with ID " + type.id() + " is unknown.", TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
			}
		};
		
		return a;
	}

	@Override
	public void removeAdapter(long id) {
		if(this.adapterIndex.containsKey(id)){
			ImcAdapter adapter = this.adapterIndex.remove(id);
			try {
				adapter.terminate();
			} catch (TerminatedException e) {
				// ignore
			}
			
		}
		super.remove(id);
	}

	@Override
	public void terminate() {
		Set<Long> keys = new HashSet<>(this.adapterIndex.keySet());
		for(Iterator<Long> iter = keys.iterator(); iter.hasNext(); ){
			Long key = iter.next();
			this.removeAdapter(key);
		}
	}
	
	
}
