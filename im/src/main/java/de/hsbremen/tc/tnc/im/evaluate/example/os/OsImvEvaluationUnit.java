package de.hsbremen.tc.tnc.im.evaluate.example.os;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttribute;
import org.ietf.nea.pa.attribute.PaAttributeFactoryIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueError;
import org.ietf.nea.pa.attribute.PaAttributeValueNumericVersion;
import org.ietf.nea.pa.attribute.PaAttributeValueProductInformation;
import org.ietf.nea.pa.attribute.PaAttributeValueStringVersion;
import org.ietf.nea.pa.attribute.enums.PaAttributeAssessmentResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.attribute.util.AttributeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.im.adapter.imv.enums.ImvActionRecommendationEnum;
import de.hsbremen.tc.tnc.im.adapter.imv.enums.ImvEvaluationResultEnum;
import de.hsbremen.tc.tnc.im.evaluate.AbstractImEvaluationUnitIetf;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.ImvRecommendationObject;
import de.hsbremen.tc.tnc.im.evaluate.enums.ImTypeEnum;
import de.hsbremen.tc.tnc.im.evaluate.example.os.util.ConfigurationParameterParser;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.m.attribute.ImAttribute;
import de.hsbremen.tc.tnc.m.attribute.ImAttributeValue;

public class OsImvEvaluationUnit extends AbstractImEvaluationUnitIetf implements ImvEvaluationUnit{

	private static final Logger LOGGER = LoggerFactory.getLogger(OsImvEvaluationUnit.class);
	
	public final static long VENDOR_ID = IETFConstants.IETF_PEN_VENDORID;
	public final static long TYPE = ImTypeEnum.IETF_PA_OPERATING_SYSTEM.type();

	private Properties properties;
	
	private ImvRecommendationObject recommendation;
	
	public OsImvEvaluationUnit(GlobalHandshakeRetryListener globalHandshakeRetryListener){
		super(globalHandshakeRetryListener);
		
		try{
			properties = ConfigurationParameterParser.loadProperties();
		}catch(IOException e){
			LOGGER.error(e.getMessage(),e);
			properties = null;
		}
		
	}
	
	@Override
	public long getVendorId() {
		return VENDOR_ID;
	}

	@Override
	public long getType() {
		return TYPE;
	}

	@Override
	public List<ImAttribute> evaluate(ImSessionContext context) {
		this.recommendation = null;
		List<ImAttribute> attributes = new ArrayList<>();
		try{
			PaAttribute attrReq = this.getAttributeRefequest();
			attributes.add(attrReq);
		}catch(RuleException e){
			LOGGER.error("Attribute request clould not be created.",e);
		}
	
		return attributes;
	}

	@Override
	public List<ImAttribute> handle(List<? extends ImAttribute> attribute,
			ImSessionContext context) {
		List<ImAttribute> attributes = new ArrayList<>();
		this.recommendation = null;
		if(properties != null){
			int rating = 0;
			for (ImAttribute imAttribute : attribute) {
				ImAttributeValue value = imAttribute.getValue();
				if(value instanceof PaAttributeValueStringVersion){
					rating += this.handleStringVersion((PaAttributeValueStringVersion)value, context);
				}else if(value instanceof PaAttributeValueNumericVersion){
					rating += this.handleNumericVersion((PaAttributeValueNumericVersion)value, context);
				}else if(value instanceof PaAttributeValueProductInformation){
					rating += this.handleProductInformationVersion((PaAttributeValueProductInformation)value, context);
				} if(value instanceof PaAttributeValueError){
					this.handleError((PaAttributeValueError)value, context);
				}
			}
			
			LOGGER.debug("Rating: " + rating + " of 10." );
			
			if(rating < 8){
				this.recommendation = new ImvRecommendationObject(ImvActionRecommendationEnum.TNC_IMV_ACTION_RECOMMENDATION_NO_ACCESS, ImvEvaluationResultEnum.TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MAJOR);
			}else{
				this.recommendation = new ImvRecommendationObject(ImvActionRecommendationEnum.TNC_IMV_ACTION_RECOMMENDATION_ALLOW, ImvEvaluationResultEnum.TNC_IMV_EVALUATION_RESULT_COMPLIANT);
			}
			
			try{
				attributes.add(PaAttributeFactoryIetf.createAssessmentResult(
						(this.recommendation.getResult().equals(ImvEvaluationResultEnum.TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MAJOR)) ? 
								PaAttributeAssessmentResultEnum.SIGNIFICANT_DIFFERENCES : 
								PaAttributeAssessmentResultEnum.COMPLIANT));
			}catch(RuleException e){
				LOGGER.error("Assessment result clould not be created.",e);
			}
		}
		
		return attributes;
	}

	private int handleStringVersion(PaAttributeValueStringVersion value,
			ImSessionContext context) {
		
		int i = 0;
		if(value.getBuildVersion().trim().equals(properties.getProperty("str_build_number").trim())){
			i++;
		}
		
		if(value.getVersionNumber().trim().equals(properties.getProperty("str_version_number").trim())){
			i++;
		}
	
		if(value.getConfigurationVersion().trim().equals(properties.getProperty("str_config_number").trim())){
			i++;	
		}
		
		return i;
	}

	private void handleError(PaAttributeValueError value,
			ImSessionContext context) {
		// TODO better error handling
		LOGGER.error("IMC has send an error: " + value.getErrorInformation().toString());
		
	}

	private int handleProductInformationVersion(
			PaAttributeValueProductInformation value, ImSessionContext context) {
		int i = 0;

		if(value.getName().contains(properties.getProperty("pi_name"))){
			i+=2;
		}
		
		if(value.getProductId() == Integer.parseInt(properties.getProperty("pi_id"))){
			i++;
		}
		
		if(value.getVendorId() == Integer.parseInt(properties.getProperty("pi_vid"))){
			i++;
		}
		
		return i;
		
	}

	private int handleNumericVersion(PaAttributeValueNumericVersion value,
			ImSessionContext context) {
		int i = 0;
		
		if(value.getMajorVersion() == Long.parseLong(properties.getProperty("nv_major"))){
			i++;
		}
		
		if(value.getMinorVersion() == Long.parseLong(properties.getProperty("nv_minor"))){
			i++;
		}
		
		if(value.getBuildVersion() == Long.parseLong(properties.getProperty("nv_build"))){
			i++;
		}
		
		// the rest is not relevant right now
		
		return i;
	}

	@Override
	public ImvRecommendationObject getRecommendation(ImSessionContext context) {
		// look if recommendation is present and handle it if possible else return defualt no recommendation
		return (this.recommendation != null) ? this.recommendation : new ImvRecommendationObject();
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.evaluate.ImvEvaluationUnit#hasRecommendation()
	 */
	@Override
	public boolean hasRecommendation() {
		return (this.recommendation != null);
	}

	@Override
	public List<ImAttribute> lastCall(ImSessionContext context) {
		
		LOGGER.info("Last call received.");
		return new ArrayList<>(0);
	}
	
	@Override
	public void terminate() {
		LOGGER.debug("Terminate called.");
	}

	private PaAttribute getAttributeRefequest() throws RuleException {
		
		return PaAttributeFactoryIetf.createAttributeRequest(
				new AttributeReference(IETFConstants.IETF_PEN_VENDORID,PaAttributeTypeEnum.IETF_PA_PRODUCT_INFORMATION.attributeType()), 
				new AttributeReference(IETFConstants.IETF_PEN_VENDORID,PaAttributeTypeEnum.IETF_PA_NUMERIC_VERSION.attributeType()),
				new AttributeReference(IETFConstants.IETF_PEN_VENDORID,PaAttributeTypeEnum.IETF_PA_STRING_VERSION.attributeType()));
		
	}

}
