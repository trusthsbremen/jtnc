package de.hsbremen.tc.tnc.tnccs.im.manager;

import java.util.Deque;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.tnccs.im.manager.exception.ImInitializeException;
import de.hsbremen.tc.tnc.tnccs.im.route.ImMessageRouter;

public abstract class AbstractImManager<T> implements ImManager<T>{

//	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractImManager.class);
	
	private long idDispensor;
	private Deque<Long> idRecyclingBin;
	private final long maxImId;
	
	private Map<Long,T> imcIndex;
	private Map<T, Long> imcs;
	
	private ImMessageRouter router;
	
	public AbstractImManager(ImMessageRouter router, long maxImId) {
		
		this.idDispensor = 0;
		this.idRecyclingBin = new ConcurrentLinkedDeque<>(); // use this because sessions and IM management may have threads
		this.imcs = new ConcurrentHashMap<>(); // use this because sessions and IM management may have threads
		this.imcIndex = new ConcurrentHashMap<>(); // use this because sessions and IM management may have threads
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

		this.idRecyclingBin.add(id);

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
}
