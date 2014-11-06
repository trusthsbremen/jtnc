package de.hsbremen.tc.tnc.im.evaluate;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.im.adapter.data.ImComponentFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.data.enums.ImComponentFlagsEnum;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.m.attribute.ImAttribute;

public class DefaultImEvaluator implements ImEvaluator {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultImEvaluator.class);
	
	private final long id;
	private final boolean useExcl;
	private final List<ImEvaluationUnit> evaluationUnits;
	
	public DefaultImEvaluator(long id, boolean shouldUseExcl ,List<ImEvaluationUnit> evaluationUnits){
		this.id = id;
		this.useExcl = shouldUseExcl;
		this.evaluationUnits = evaluationUnits;
	}
	
	@Override
	public long getId() {
		return this.id;
	}

	@Override
	public List<ImObjectComponent> evaluate(ImSessionContext context) {
		
		LOGGER.debug("evaluate() called, with connection state: " + context.getConnectionState().toString());
		
		List<ImObjectComponent> components = new LinkedList<>();
		if(this.evaluationUnits != null){
			for (ImEvaluationUnit unit: this.evaluationUnits) {
				List<ImAttribute> attributes = unit.evaluate(context);
				if(attributes != null && !attributes.isEmpty()){
					components.add(ImComponentFactory.createObjectComponent(
							(byte)0, 
							unit.getVendorId(), 
							unit.getType(), 
							this.getId(),
							TNCConstants.TNC_IMVID_ANY,
							attributes));
				}
			}
		}
		return components;
	}

	@Override
	public List<ImObjectComponent> handle(ImObjectComponent component,
			ImSessionContext context) {
		
		LOGGER.debug("handle() called, with connection state: " + context.getConnectionState().toString());
		
		List<ImObjectComponent> components = new LinkedList<>();
		if(this.evaluationUnits != null){
			for (ImEvaluationUnit unit: this.evaluationUnits) {
				
				long givenVendorId = component.getVendorId();
				long givenType = component.getType();
				long interestedVendorId = unit.getVendorId();
				long interestedType = unit.getType();
				
				if((interestedVendorId == TNCConstants.TNC_VENDORID_ANY) ||
						(
								(givenVendorId == interestedVendorId) &&
								(
										(givenType == TNCConstants.TNC_SUBTYPE_ANY) ||
										(givenType == interestedType)
								)
						)
					){
					List<ImAttribute> attributes = unit.handle(component.getAttributes(), context);
					if(attributes != null && !attributes.isEmpty()){
						components.add(ImComponentFactory.createObjectComponent(
								((component.getValidatorId() > 0 && this.useExcl) ? ImComponentFlagsEnum.EXCL.bit() : 0), 
								unit.getVendorId(), 
								unit.getType(), 
								this.getId(),
								component.getValidatorId(),
								attributes)
						);
					}
				}
			}
		}
		return components;
	}
	
	@Override
	public void terminate() {
		LOGGER.debug("Terminate called. Terminating units...");
		if(this.evaluationUnits != null){
			for (ImEvaluationUnit unit: this.evaluationUnits) {
				unit.terminate();
			}
		}
	}

	@Override
	public List<ImObjectComponent> lastCall(ImSessionContext context) {
		LOGGER.debug("lastCall() called, with connection state: " + context.getConnectionState().toString());
		
		List<ImObjectComponent> components = new LinkedList<>();
		if(this.evaluationUnits != null){
			for (ImEvaluationUnit unit: this.evaluationUnits) {
				List<ImAttribute> attributes = unit.lastCall(context);
				if(attributes != null && !attributes.isEmpty()){
					components.add(ImComponentFactory.createObjectComponent(
							(byte)0, 
							unit.getVendorId(), 
							unit.getType(), 
							this.getId(),
							TNCConstants.TNC_IMVID_ANY,
							attributes));
				}
			}
		}
		return components;
	}
}
