package de.hsbremen.tc.tnc.im.evaluate.example.os;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ietf.nea.pa.attribute.PaAttribute;
import org.ietf.nea.pa.attribute.PaAttributeFactoryIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueAssessmentResult;
import org.ietf.nea.pa.attribute.PaAttributeValueAttributeRequest;
import org.ietf.nea.pa.attribute.PaAttributeValueError;
import org.ietf.nea.pa.attribute.PaAttributeValueRemediationParameters;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.attribute.util.AttributeReference;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationInvalidParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.im.evaluate.AbstractImcEvaluationUnitIetf;
import de.hsbremen.tc.tnc.im.evaluate.enums.ImTypeEnum;
import de.hsbremen.tc.tnc.im.evaluate.example.os.exception.PatternNotFoundException;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.m.attribute.ImAttribute;
import de.hsbremen.tc.tnc.natives.CLibrary;
import de.hsbremen.tc.tnc.natives.CLibrary.UTSNAME;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

public class OsImcEvaluationUnit extends AbstractImcEvaluationUnitIetf{

	private static final Logger LOGGER = LoggerFactory.getLogger(OsImcEvaluationUnit.class);
	
	public final static long VENDOR_ID = IETFConstants.IETF_PEN_VENDORID;
	public final static long TYPE = ImTypeEnum.IETF_PA_OPERATING_SYSTEM.type();

	
	public OsImcEvaluationUnit(GlobalHandshakeRetryListener globalHandshakeRetryListener){
		super(globalHandshakeRetryListener);
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
		final UTSNAME systemDescription = new UTSNAME();
		CLibrary.INSTANCE.uname(systemDescription);
		
		List<ImAttribute> attributes = new ArrayList<>();
		try{
			PaAttribute prodInfo = this.getProductInformation(systemDescription);
			attributes.add(prodInfo);
		}catch(ValidationException e){
			LOGGER.error("Product information clould not be created.",e);
		}
		
		try {
			PaAttribute numericVers = this.getNumericVersion(systemDescription);
			attributes.add(numericVers);
		} catch (NumberFormatException | ValidationException
				| PatternNotFoundException e) {
			LOGGER.error("Numeric version clould not be created.",e);
		}
		
		try{
			PaAttribute stringVers = this.getStringVersion(systemDescription);
			attributes.add(stringVers);
		}catch(ValidationException e){
			LOGGER.error("String version clould not be created.",e);
		}
		
//		PaAttribute forwardEnabled = this.getForwardingStatus();
//		attributes.add(forwardEnabled);
//	
//		PaAttribute facDefPwd = this.getFactoryDefaultPwdStatus();
//		attributes.add(facDefPwd);
	
		return attributes;
	}

	@Override
	public void terminate() {
		LOGGER.debug("Terminate called.");
	}
	
	@Override
	protected List<? extends ImAttribute> handleAttributeRequest(
			PaAttributeValueAttributeRequest value, ImSessionContext context) {
		
		List<PaAttribute> attributeList = new ArrayList<>();
		
		final UTSNAME systemDescription = new UTSNAME();
		CLibrary.INSTANCE.uname(systemDescription);
		
		List<AttributeReference> references = value.getReferences();
		for (AttributeReference attributeReference : references) {
			try{
				if(attributeReference.getVendorId() == 0){
					if(attributeReference.getType() == PaAttributeTypeEnum.IETF_PA_PRODUCT_INFORMATION.attributeType()){
						attributeList.add(this.getProductInformation(systemDescription));
					}else if( attributeReference.getType() == PaAttributeTypeEnum.IETF_PA_NUMERIC_VERSION.attributeType()){
						attributeList.add(this.getNumericVersion(systemDescription));
					}else if( attributeReference.getType() == PaAttributeTypeEnum.IETF_PA_STRING_VERSION.attributeType()){
						attributeList.add(this.getStringVersion(systemDescription));
					}
				}
			}catch(ValidationException | NumberFormatException | PatternNotFoundException e){
				LOGGER.error("Requested attribute could not be created.",e);
			}
		}
		return attributeList;
	}

	@Override
	protected List<? extends ImAttribute> handleError(PaAttributeValueError value, ImSessionContext context) {
		//TODO implement error handling
		StringBuilder b = new StringBuilder();
		b.append("An error was received: \n")
		.append("Error with vendor ID ").append(value.getErrorVendorId())
		.append(" and type ")
		.append(PaAttributeErrorCodeEnum.fromCode(value.getErrorCode()))
		.append(".\n")
		.append("Error was found in message ").append(value.getErrorInformation().getMessageHeader().toString());
		if(value.getErrorInformation() instanceof PaAttributeValueErrorInformationInvalidParam){
			b.append(" at offset ")
			.append(((PaAttributeValueErrorInformationInvalidParam) value.getErrorInformation()).getOffset());
		}
		b.append(".");
		LOGGER.error(b.toString());
		
		return new ArrayList<>(0);
	}

	@Override
	protected List<? extends ImAttribute> handleRemediation(
			PaAttributeValueRemediationParameters value, ImSessionContext context) {
		// TODO implement remediation handling.
		LOGGER.info("Remediation instructions were received.");

		context.requestConnectionHandshakeRetry(ImHandshakeRetryReasonEnum.TNC_RETRY_REASON_IMC_REMEDIATION_COMPLETE);
		
		return new ArrayList<>(0);
	}
	
	@Override
	protected List<? extends ImAttribute> handleResult(
			PaAttributeValueAssessmentResult value, ImSessionContext context) {
		LOGGER.info("Assessment result is: " + value.getResult().toString() + " - (# " + value.getResult().number() +")");
		return new ArrayList<>(0);
	}

	private PaAttribute getStringVersion(UTSNAME systemDescription) throws ValidationException {
		return PaAttributeFactoryIetf.createStringVersion(new String(systemDescription.release).trim(),null,new String(systemDescription.machine).trim());
	}

	private PaAttribute getNumericVersion(UTSNAME systemDescription) throws NumberFormatException, ValidationException, PatternNotFoundException {
		String release = new String(systemDescription.release).trim();
		Pattern p = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)-(\\d+)");
		Matcher m = p.matcher(release);
		if(m.find()){
			long majorVersion = Long.parseLong(m.group(1));
			long minorVersion = Long.parseLong(m.group(2));
			long buildVersion = Long.parseLong(m.group(3));
			int servicePackVersion =  Integer.parseInt(m.group(4));
			int servicePackVersionMinor = 0;
			return PaAttributeFactoryIetf.createNumericVersion(majorVersion,minorVersion,buildVersion,servicePackVersion, servicePackVersionMinor);
		}else{
			throw new PatternNotFoundException("Version pattern " + p.toString() +" was not found.", release, p.toString());
		}
	}

	private PaAttribute getProductInformation(UTSNAME systemDescription) throws ValidationException {
		// RFC 5792 Vendor ID unknown = 0 => Product ID  = 0
		return PaAttributeFactoryIetf.createProductInformation(0,0, new String(systemDescription.version).trim());
	}

	@Override
	public synchronized List<ImAttribute> lastCall(ImSessionContext context) {
		
		LOGGER.info("Last call received.");
		return new ArrayList<>(0);
	}
}
