package de.hsbremen.tc.tnc.newp;

import java.util.Deque;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.adapter.im.ImcAdapter;
import de.hsbremen.tc.tnc.adapter.im.exception.TerminatedException;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.newp.route.ImMessageRouter;
import de.hsbremen.tc.tnc.report.SupportedMessageType;

public abstract class AbstractImManager<T> implements ImManager<T>{

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractImManager.class);
	
	private long idDispensor;
	private Deque<Long> idRecyclingBin;
	private final long maxImId;
	
	private Map<Long,T> imcIndex;
	private Map<T, Long> imcs;
	private Map<Long,ImcAdapter> adapterIndex;  
	
	private ImMessageRouter router;
	
	public AbstractImManager(ImMessageRouter router, long maxImId) {
		
		this.idDispensor = 0;
		this.idRecyclingBin = new ConcurrentLinkedDeque<>(); // use this because sessions and IM management may have threads
		this.imcs = new ConcurrentHashMap<>(); // use this because sessions and IM management may have threads
		this.imcIndex = new ConcurrentHashMap<>(); // use this because sessions and IM management may have threads
		this.adapterIndex = new ConcurrentHashMap<>(); // use this because sessions and IM management may have threads
		this.router = router;
		this.maxImId = maxImId;
	}
	
	@Override
	public long add(T im) throws ImInitializeException {
		long primaryId;
		try {
			primaryId = this.reserveId();
		} catch (TncException e) {
			throw new ImInitializeException("Intialization of IMC failed. IMC will be removed.", e); 
		}
		
		this.imcIndex.put(primaryId, im);
		this.imcs.put(im, primaryId);
		try{
			this.initialize(primaryId,im);
		}catch(TncException e){
			this.imcIndex.remove(primaryId);
			this.imcs.remove(im);
			this.idRecyclingBin.add(primaryId);
			throw new ImInitializeException("Intialization of IMC failed. IMC will be removed.", e); 
		}
		return primaryId;
	}

	@Override
	public void remove(long id) {
		T imc  = this.imcIndex.remove(id);
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
	public long reserveAdditionalId(T im) throws TncException {
		if(this.imcs.containsKey(im)){
			long additionalId = this.reserveId();
			this.router.addExclusiveId(this.imcs.get(im), additionalId);
			return additionalId;
		}
		
		throw new TncException("The given IMC/V " +im.getClass().getCanonicalName()+ " is unknown.", TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
	}

	@Override
	public void reportSupportedMessagesTypes(T im,
			Set<SupportedMessageType> types) throws TncException {
		if(this.imcs.containsKey(im)){
			this.router.updateMap(this.imcs.get(im),types);
		}else{
			throw new TncException("The given IMC/V " +im.getClass().getCanonicalName()+ " is unknown.", TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
		}
	}

	protected abstract void initialize(long primaryId, T im) throws TncException;
	
//	@Override
//	public Map<Long,ImcAdapter> getAdapter() {
//		return new HashMap<>(this.adapterIndex);
//	}
//
//	@Override
//	public ImMessageRouter getRouter() {
//		return this.router;
//	}

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

//	@Override
//	public void removeAdapter(long id) {
//		this.remove(id);
//		
//	}
}
