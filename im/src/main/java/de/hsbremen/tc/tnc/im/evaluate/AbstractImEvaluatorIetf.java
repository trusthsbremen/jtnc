package de.hsbremen.tc.tnc.im.evaluate;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.data.ImComponentFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImFaultyObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.data.enums.ImComponentFlagsEnum;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluator;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.m.attribute.ImAttribute;

public class AbstractImEvaluatorIetf implements ImEvaluator {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractImEvaluatorIetf.class);
	
	private final long id;
	private final List<? extends ImEvaluationUnit> evaluationUnits;
	private final ImValueExceptionHandler valueExceptionHandler;
	
	public AbstractImEvaluatorIetf(long id ,List<? extends ImEvaluationUnit> evaluationUnits, ImValueExceptionHandler valueExceptionHandler){
		this.id = id;
		this.evaluationUnits = evaluationUnits;
		this.valueExceptionHandler = valueExceptionHandler;
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
	public List<ImObjectComponent> handle(List<? extends ImObjectComponent> components,
			ImSessionContext context) {
		
		LOGGER.debug("handle() called, with connection state: " + context.getConnectionState().toString());
		
		List<ImObjectComponent> componentList = new LinkedList<>();
		
		if(this.evaluationUnits == null){
			return componentList;
		}
		
		for (ImObjectComponent component : components) {
			
			boolean useExcl = false;
			if(component.getCollectorId() != HSBConstants.HSB_IM_ID_UNKNOWN && 
					component.getValidatorId() != HSBConstants.HSB_IM_ID_UNKNOWN && 
					component.getValidatorId() != TNCConstants.TNC_IMVID_ANY){
				
				useExcl = this.checkExclusiveDeliverySupport(context) ;
			}
			
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
					if(component instanceof ImFaultyObjectComponent){
						List<ImAttribute> errorAttributes = this.valueExceptionHandler.handle((ImFaultyObjectComponent)component);
						if(errorAttributes != null){
							if(attributes != null){
								attributes.addAll(errorAttributes); 
							}else{
								attributes = errorAttributes;
							}
						}
					}
					if(attributes != null && !attributes.isEmpty()){
						componentList.add(ImComponentFactory.createObjectComponent(
								((useExcl) ? ImComponentFlagsEnum.EXCL.bit() : 0), 
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
		
		return componentList;
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
	
	private boolean checkExclusiveDeliverySupport(ImSessionContext context){
		boolean hasSupport = false;
		
		try{
			Object o = context.getAttribute(TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_HAS_EXCLUSIVE);
			if(o instanceof Boolean){
				hasSupport = (Boolean) o; 
			}
		}catch(UnsupportedOperationException | TncException e){
			LOGGER.info("Could not access attribute from connection. " + e.getMessage());
		}
		
		return hasSupport;
		
	}
	
	protected List<? extends ImEvaluationUnit> getEvaluationUnits(){
		return Collections.unmodifiableList(this.evaluationUnits);		
	}
}
