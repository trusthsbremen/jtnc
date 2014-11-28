package de.hsbremen.tc.tnc.newp;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimv.TNCException;
import org.trustedcomputinggroup.tnc.ifimv.TNCS;

import de.hsbremen.tc.tnc.adapter.im.ImvAdapter;
import de.hsbremen.tc.tnc.adapter.im.ImvAdapterFactory;
import de.hsbremen.tc.tnc.adapter.tncc.TncsAdapterFactory;
import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.attribute.TncServerAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.newp.route.ImMessageRouter;

public class DefaultImvManager extends AbstractImManager<IMV> implements ImAdapterManager<ImvAdapter>{

//	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultImvManager.class);
	
	private ImvAdapterFactory adapterFactory;
	private final TncsAdapterFactory tncsFactory;
	private final ImMessageRouter router;

	private Map<Long,ImvAdapter> adapterIndex;  
	
	public DefaultImvManager(ImMessageRouter router, ImvAdapterFactory adapterFactory, TncsAdapterFactory tncsFactory){
		this(router, adapterFactory, tncsFactory, (TNCConstants.TNC_IMCID_ANY -1));
	}
	
	public DefaultImvManager(ImMessageRouter router, ImvAdapterFactory adapterFactory, TncsAdapterFactory tncsFactory, long maxImId) {
		
		super(router, maxImId);
		
		this.router = router;
		
		this.adapterFactory = adapterFactory;
		this.tncsFactory = tncsFactory;
		
		this.adapterIndex = new ConcurrentHashMap<>();
		
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.newp.AbstractImManager#initialize(long, java.lang.Object)
	 */
	@Override
	protected void initialize(long primaryId, IMV im) throws TncException {
		TNCS tncc = this.tncsFactory.createTncs(im, this.createPrimaryIdAttribute(primaryId), this);
		
		try {
			im.initialize(tncc);
		} catch (TNCException e) {
			throw new TncException(e);
		}
		
		ImvAdapter adapter = this.adapterFactory.createImvAdapter(im, primaryId);
		this.adapterIndex.put(primaryId, adapter);
		
	}

	@Override
	public Map<Long,ImvAdapter> getAdapter() {
		return new HashMap<>(this.adapterIndex);
	}

	@Override
	public ImMessageRouter getRouter() {
		return this.router;
	}

	
	private Attributed createPrimaryIdAttribute(final long primaryId) {
		Attributed a = new Attributed(){
			
			private long primaryImvId = primaryId;
			
			@Override
			public void setAttribute(TncAttributeType type, Object value)
					throws TncException {
				throw new UnsupportedOperationException("The operation setAttribute(...) is not supported, because there are no attributes to set.");
				
			}
			
			@Override
			public Object getAttribute(TncAttributeType type) throws TncException {
				if(type.id() == TncServerAttributeTypeEnum.TNC_ATTRIBUTEID_PRIMARY_IMV_ID.id()){
					return new Long(this.primaryImvId);
				}
				throw new TncException("The attribute with ID " + type.id() + " is unknown.", TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
			}
		};
		
		return a;
	}

	@Override
	public void removeAdapter(long id) {
		this.remove(id);
		
	}
}
