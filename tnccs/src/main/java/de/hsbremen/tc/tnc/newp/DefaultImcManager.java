package de.hsbremen.tc.tnc.newp;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.adapter.im.ImcAdapter;
import de.hsbremen.tc.tnc.adapter.im.ImcAdapterFactory;
import de.hsbremen.tc.tnc.adapter.im.TerminatedException;
import de.hsbremen.tc.tnc.adapter.tncc.TnccAdapterFactory;
import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.attribute.TncClientAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.report.SupportedMessageType;

public class DefaultImcManager implements ImManager<IMC>, ImAdapterManager<ImcAdapter>{

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultImcManager.class);
	
	private long idDispensor;
	private Deque<Long> idRecyclingBin;
	private final long maxImId;
	
	private Map<Long,IMC> imcIndex;
	private Map<IMC, Long> imcs;
	private Map<Long,ImcAdapter> adapterIndex;  
	
	private ImcAdapterFactory adapterFactory;
	private ImMessageRouter router;
	
	private final TnccAdapterFactory tnccFactory;

	public DefaultImcManager(ImMessageRouter router, ImcAdapterFactory adapterFactory, TnccAdapterFactory tnccFactory){
		this(router, adapterFactory, tnccFactory, (TNCConstants.TNC_IMCID_ANY -1));
	}
	
	public DefaultImcManager(ImMessageRouter router, ImcAdapterFactory adapterFactory, TnccAdapterFactory tnccFactory, long maxImId) {
		
		this.idDispensor = 0;
		this.idRecyclingBin = new ConcurrentLinkedDeque<>(); // use this because sessions and IM management may have threads
		this.imcs = new ConcurrentHashMap<>(); // use this because sessions and IM management may have threads
		this.imcIndex = new ConcurrentHashMap<>(); // use this because sessions and IM management may have threads
		this.adapterIndex = new ConcurrentHashMap<>(); // use this because sessions and IM management may have threads
		this.router = router;
		this.adapterFactory = adapterFactory;
		this.tnccFactory = tnccFactory;
		this.maxImId = maxImId;
	}
	
	@Override
	public long add(IMC im) throws ImInitializeException {
		long primaryId;
		try {
			primaryId = this.reserveId();
		} catch (TncException e) {
			throw new ImInitializeException("Intialization of IMC failed. IMC will be removed.", e); 
		}
		
		this.imcIndex.put(primaryId, im);
		this.imcs.put(im, primaryId);
		
		TNCC tncc = this.tnccFactory.createTncc(im, this.createPrimaryIdAttribute(primaryId), this);
		
		try {
			im.initialize(tncc);
		} catch (TNCException e) {
			this.imcIndex.remove(primaryId);
			this.imcs.remove(im);
			this.idRecyclingBin.add(primaryId);
			throw new ImInitializeException("Intialization of IMC failed. IMC will be removed.", new TncException(e)); 
		}
		
		ImcAdapter adapter = this.adapterFactory.createImcAdapter(im, primaryId);
		this.adapterIndex.put(primaryId, adapter);
		return primaryId;
	}

	@Override
	public void remove(long id) {
		IMC imc  = this.imcIndex.remove(id);
		this.router.remove(id);
		if(imc != null){
			this.imcs.remove(imc);
		}
		if(this.adapterIndex.containsKey(id)){
			ImcAdapter adapter = this.adapterIndex.remove(id);
			try {
				adapter.terminate();
			} catch (TerminatedException e) {
				// ignore
			}
			
		}
		
		if(!this.imcIndex.containsKey(id) && !this.adapterIndex.containsKey(id)){
			this.idRecyclingBin.add(id);
		}else{
			LOGGER.warn("An ID "+ id +" cannot be reused, because it has remaining dependencies.");
		}
		
		
	}

	@Override
	public long reserveAdditionalId(IMC im) throws TncException {
		if(this.imcs.containsKey(im)){
			long additionalId = this.reserveId();
			this.router.addExclusiveId(this.imcs.get(im), additionalId);
			return additionalId;
		}
		
		throw new TncException("The given IMC/V " +im.getClass().getCanonicalName()+ " is unknown.", TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
	}

	@Override
	public void reportSupportedMessagesTypes(IMC im,
			Set<SupportedMessageType> types) throws TncException {
		if(this.imcs.containsKey(im)){
			this.router.updateMap(this.imcs.get(im),types);
		}else{
			throw new TncException("The given IMC/V " +im.getClass().getCanonicalName()+ " is unknown.", TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
		}
	}

	@Override
	public Map<Long,ImcAdapter> getAdapter() {
		return new HashMap<>(this.adapterIndex);
	}

	@Override
	public ImMessageRouter getRouter() {
		return this.router;
	}

	private long reserveId() throws TncException {
		if(!this.idRecyclingBin.isEmpty()){
			return this.idRecyclingBin.pop();
		}else{
			if(idDispensor < this.maxImId){
				return ++idDispensor;
			}else{
				throw new TncException("No Ids left.", TncExceptionCodeEnum.TNC_RESULT_OTHER);
			}
		}
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
		this.remove(id);
		
	}
}
