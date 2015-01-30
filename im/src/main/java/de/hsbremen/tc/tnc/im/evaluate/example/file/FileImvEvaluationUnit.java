package de.hsbremen.tc.tnc.im.evaluate.example.file;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.ietf.nea.pa.attribute.PaAttribute;
import org.ietf.nea.pa.attribute.PaAttributeFactoryIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueError;
import org.ietf.nea.pa.attribute.PaAttributeValueTesting;
import org.ietf.nea.pa.attribute.enums.PaAttributeAssessmentResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.attribute.util.AttributeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.im.evaluate.AbstractImEvaluationUnitIetf;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.enums.ImTypeEnum;
import de.hsbremen.tc.tnc.im.evaluate.example.os.util.ConfigurationParameterParser;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttributeValue;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;
import de.hsbremen.tc.tnc.report.ImvRecommendationPairFactory;
import de.hsbremen.tc.tnc.report.enums.ImvActionRecommendationEnum;
import de.hsbremen.tc.tnc.report.enums.ImvEvaluationResultEnum;

public class FileImvEvaluationUnit extends AbstractImEvaluationUnitIetf implements ImvEvaluationUnit{

	private static final Logger LOGGER = LoggerFactory.getLogger(FileImvEvaluationUnit.class);
	
	public final static long VENDOR_ID = IETFConstants.IETF_PEN_VENDORID;
	public final static long TYPE = ImTypeEnum.IETF_PA_TESTING.type();

	private Properties properties;
	
	private ImvRecommendationPair recommendation;
	
	public FileImvEvaluationUnit(String evaluationValuesFile, GlobalHandshakeRetryListener globalHandshakeRetryListener){
		super(globalHandshakeRetryListener);
		
		try{
			properties = ConfigurationParameterParser.loadProperties(evaluationValuesFile);
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
	public synchronized List<ImAttribute> evaluate(ImSessionContext context) {
		this.recommendation = null;
		List<ImAttribute> attributes = new ArrayList<>();
		try{
			PaAttribute attrReq = this.getAttributeRequest();
			attributes.add(attrReq);
		}catch(ValidationException e){
			LOGGER.error("Attribute request clould not be created.",e);
		}
	
		return attributes;
	}

	@Override
	public synchronized List<ImAttribute> handle(List<? extends ImAttribute> attribute,
			ImSessionContext context) {
		List<ImAttribute> attributes = new ArrayList<>();
		this.recommendation = null;
		if(properties != null){
			int rating = 0;
			for (ImAttribute imAttribute : attribute) {
				ImAttributeValue value = imAttribute.getValue();
				if(value instanceof PaAttributeValueTesting){
					rating = this.handleTesting((PaAttributeValueTesting)value, context);
				} if(value instanceof PaAttributeValueError){
					this.handleError((PaAttributeValueError)value, context);
				}
			}
			
			LOGGER.debug("Rating: " + rating + " of 1." );
			
			if(rating < 1 ){
				this.recommendation = ImvRecommendationPairFactory.createRecommendationPair(ImvActionRecommendationEnum.TNC_IMV_ACTION_RECOMMENDATION_NO_ACCESS, ImvEvaluationResultEnum.TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MAJOR);
			}else{
				this.recommendation = ImvRecommendationPairFactory.createRecommendationPair(ImvActionRecommendationEnum.TNC_IMV_ACTION_RECOMMENDATION_ALLOW, ImvEvaluationResultEnum.TNC_IMV_EVALUATION_RESULT_COMPLIANT);
			}
			
			try{
				attributes.add(PaAttributeFactoryIetf.createAssessmentResult(
						(this.recommendation.getResult().equals(ImvEvaluationResultEnum.TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MAJOR)) ? 
								PaAttributeAssessmentResultEnum.SIGNIFICANT_DIFFERENCES : 
								PaAttributeAssessmentResultEnum.COMPLIANT));
			}catch(ValidationException e){
				LOGGER.error("Assessment result clould not be created.",e);
			}
		}
		
		return attributes;
	}

	private int handleTesting(PaAttributeValueTesting value,
			ImSessionContext context) {
		
		if(value.getContent().trim().equals(properties.getProperty("checksum").trim())){
			return 1;
		}
		
		return 0;
	}

	private void handleError(PaAttributeValueError value,
			ImSessionContext context) {
		// TODO better error handling
		LOGGER.error("IMC has send an error: " + value.getErrorInformation().toString());
		
	}

	@Override
	public synchronized ImvRecommendationPair getRecommendation(ImSessionContext context) {
		// look if recommendation is present and handle it if possible else return default no recommendation
		ImvRecommendationPair rec = this.recommendation;
		this.recommendation = null; // remove recommendation after it has been ask for, to make room for a new evaluation. 
		return (rec != null) ? rec : ImvRecommendationPairFactory.getDefaultRecommendationPair();
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.evaluate.ImvEvaluationUnit#hasRecommendation()
	 */
	@Override
	public boolean hasRecommendation() {
		return (this.recommendation != null);
	}

	@Override
	public synchronized List<ImAttribute> lastCall(ImSessionContext context) {
		
		LOGGER.info("Last call received.");
		return new ArrayList<>(0);
	}
	
	@Override
	public void terminate() {
		LOGGER.debug("Terminate called.");
	}

	private PaAttribute getAttributeRequest() throws ValidationException {
		
		return PaAttributeFactoryIetf.createAttributeRequest(
				new AttributeReference(IETFConstants.IETF_PEN_VENDORID,PaAttributeTypeEnum.IETF_PA_TESTING.attributeType()));
	}

}
