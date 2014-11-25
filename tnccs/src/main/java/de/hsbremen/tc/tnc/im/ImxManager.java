package de.hsbremen.tc.tnc.im;

import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.adapter.connection.ImConnectionAdapter;
import de.hsbremen.tc.tnc.adapter.tncc.TnccAdapterFactory;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.report.SupportedMessageType;

public class ImxManager implements ImSubscriptionService{
	private static final Logger LOGGER = LoggerFactory.getLogger(ImxManager.class);
	
	private long idDispensor;
	private Deque<Long> reuseable;
	private final Set<ImSubscriber> subscribers;
	private final Map<Long,IMC> imIndex;
	private final Map<IMC,Imc> loadedIms;
	private final ImMessageRouter router;
	private final ImcFactory factory;
	private final TnccAdapterFactory tnccFactory;
	private final ImFunctionFactory functionFactory;
//	private final ImcAdapterFactory adapterFactory;
	
	public ImxManager(ImMessageRouter router, ImcFactory factory, TnccAdapterFactory tnccFactory) {
		
		this.idDispensor = 0;
		this.reuseable = new ConcurrentLinkedDeque<>(); // use this because sessions and IM management may have threads
		this.subscribers = Collections.synchronizedSet(new HashSet<ImSubscriber>()); // use this because sessions and IM management may have threads
		this.imIndex = new ConcurrentHashMap<>(); // use this because sessions and IM management may have threads
		this.loadedIms = new ConcurrentHashMap<>(); // use this because sessions and IM management may have threads
		this.router = router;
		this.factory = factory;
		this.tnccFactory = tnccFactory;
//		this.adapterFactory = adapterFactory;
	}

	/* ImManager functions */
	
	public void add(IMC im){
		ImAttributes attributes = new ImAttributes(this.reserveId());
		TNCC tncc = this.tnccFactory.createTncc(im, attributes);
		Imc imc = this.factory.createImc(attributes.getPrimaryId(),im,tncc);
		
		if(!this.loadedIms.containsKey(im)){
			Long id = new Long(imc.getPrimaryId());
		
			this.loadedIms.put(im, imc);
			
			try {
				im.initialize(tncc);
				this.router.addToMap(im, imc);
				this.imIndex.put(id, im);
				this.notifyAdd(id);
			} catch (TNCException e) {
				if(e.getResultCode() != TncExceptionCodeEnum.TNC_RESULT_ALREADY_INITIALIZED.result()){
					LOGGER.error("Error during initialization. IMC/V will be removed.", e);
					this.loadedIms.remove(im);
					return;
				}
			}
		}
	}
	
	public void terminate(IMC imc){
		Long id = new Long(this.loadedIms.get(imc).getPrimaryId());
		this.imIndex.remove(id);
		this.notifyDelete(id);
		
		this.reuseable.addAll(this.loadedIms.get(imc).getIds());
		this.router.removeFromMap(imc, this.loadedIms.get(imc));

		this.loadedIms.get(imc).terminate();
		this.loadedIms.remove(imc);
	}
	
	public void terminate(){
		
		this.imIndex.clear();
		this.notifyTerminate();
		
		this.reuseable.addAll(imIndex.keySet());
		for(IMC imc: this.loadedIms.keySet()){
			this.router.removeFromMap(imc, this.loadedIms.get(imc));
		}
			
		for (IMC im: this.loadedIms.keySet()) {
			this.loadedIms.get(im).terminate();
		}
			
		this.loadedIms.clear();
	}
	
	/* ImManager for IMC/V */
	
	public Long reserveId() {
		if(!this.reuseable.isEmpty()){
			return this.reuseable.pop();
		}else{
			return ++idDispensor;
		}
	}

	public void reportMessageTypes(IMC imc, Set<SupportedMessageType> types) {
		this.loadedIms.get(imc).setSupportedMessageTypes(types);
		// clear old routes
		this.router.removeFromMap(imc, this.loadedIms.get(imc));
		// add new routes
		this.router.addToMap(imc, this.loadedIms.get(imc));
	}
	
	/* ImSubscriptionService */ 
	
	@Override
	public Set<Long> subscribe(ImSubscriber subscriber) {
		this.subscribers.add(subscriber);
		return new HashSet<Long>(imIndex.keySet());
	}

	@Override
	public void unsubscribe(ImSubscriber subscriber) {
		this.subscribers.remove(subscriber);
	}

	@Override
	public Set<Long> update(ImSubscriber subscriber) {
		if(this.subscribers.contains(subscriber)){
			return new HashSet<Long>(imIndex.keySet());
		}
		return new HashSet<>();
	}
	
	public void execute(Set<ImConnectionAdapter> connections){
		Set<ImConnectionAdapter> keySet = (connections != null) ? connections : new HashSet<ImConnectionAdapter>(); 
		Set<IMC> set = new HashSet<>();
		for (ImConnectionAdapter idx : keySet) {
			if(this.imIndex.containsKey(idx.getImId())){
				if(this.loadedIms.containsKey(this.imIndex.get(idx))){
					function.call(this.loadedIms.get(key));
			}
		}
	}
	
		
		
	private void notifyAdd(long id){
		for (ImSubscriber subscriber : this.subscribers) {
			subscriber.notifyAdd(this, id);
		}
	}
	
	private void notifyDelete(long id){
		for (ImSubscriber subscriber : this.subscribers) {
			subscriber.notifyDelete(this, id);
		}
	}
	
	private void notifyTerminate(){
		for (ImSubscriber subscriber: subscribers) {
			subscriber.notifyTerminate(this);
		}
	}
	
}
