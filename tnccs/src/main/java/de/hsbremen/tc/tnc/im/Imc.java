package de.hsbremen.tc.tnc.im;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.adapter.im.ImcAdapter;
import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.SupportedMessageType;

public class Imc implements Im<ImcAdapter> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Imc.class);
	
	private final ImAttributes attributes;
	private final ImcAdapter imc;
	
	private Set<SupportedMessageType> supportedMessageTypes;
	private Set<Long> ids;
	private Boolean running;
	
	public Imc(ImAttributes attributes,ImcAdapter imc) {
		this.running = Boolean.TRUE;
		this.attributes = attributes;
		this.imc = imc;
		this.supportedMessageTypes = new HashSet<>();
		this.ids = new HashSet<>();
		this.ids.add(this.attributes.getPrimaryId());
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.Im#getPrimaryId()
	 */
	@Override
	public long getPrimaryId(){
		this.attributes.getPrimaryId();
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.Im#getAttributes()
	 */
	@Override
	public Attributed getAttributes() {
		return this.attributes;
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.Im#getIm()
	 */
	@Override
	public ImcAdapter getIm() {
		return this.imc;
	}

	public Set<Long> getIds() {
		return Collections.unmodifiableSet(this.ids);
	}

	public void adId(Long id){
		this.ids.add(id);
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.Im#getSupportedMessageTypes()
	 */
	@Override
	public Set<SupportedMessageType> getSupportedMessageTypes() {
		synchronized (supportedMessageTypes) {
			return Collections.unmodifiableSet(this.supportedMessageTypes);
		}
		
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.Im#setSupportedMessageTypes(java.util.Set)
	 */
	@Override
	public void setSupportedMessageTypes(
			Set<SupportedMessageType> supportedMessageTypes) {
		synchronized (supportedMessageTypes) {
			this.supportedMessageTypes = supportedMessageTypes;
		}
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.Im#terminate()
	 */
	@Override
	public synchronized void terminate(){
		synchronized (running) {
			if(running){
				this.running = false;
				try {
					this.imc.terminate();
				} catch (TncException e) {
					LOGGER.warn("An error occured, while terminating IMC/V " + this.imc + ". IMC/V will be removed anyway.",e);
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.Im#isTerminated()
	 */
	@Override
	public boolean isTerminated(){
		return this.running;
	}
	
}
