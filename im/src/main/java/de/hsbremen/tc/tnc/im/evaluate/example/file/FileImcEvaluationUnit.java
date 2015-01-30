package de.hsbremen.tc.tnc.im.evaluate.example.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

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
import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.im.evaluate.AbstractImcEvaluationUnitIetf;
import de.hsbremen.tc.tnc.im.evaluate.enums.ImTypeEnum;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;
import de.hsbremen.tc.tnc.natives.CLibrary;
import de.hsbremen.tc.tnc.natives.CLibrary.UTSNAME;

public class FileImcEvaluationUnit extends AbstractImcEvaluationUnitIetf{

	private static final Logger LOGGER = LoggerFactory.getLogger(FileImcEvaluationUnit.class);
	
	public final static long VENDOR_ID = IETFConstants.IETF_PEN_VENDORID;
	public final static long TYPE = ImTypeEnum.IETF_PA_TESTING.type();

	private final String filePath;
	private final String messageDigestIdentifier;
	
	public FileImcEvaluationUnit(String messageDigestIdentifier, String filePath, GlobalHandshakeRetryListener globalHandshakeRetryListener){
		super(globalHandshakeRetryListener);
		this.filePath = filePath;
		this.messageDigestIdentifier = messageDigestIdentifier;
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
		
		List<ImAttribute> attributes = new ArrayList<>();
		
		try{
			PaAttribute file = this.getFileHash();
			attributes.add(file);
		}catch(ValidationException e){
			LOGGER.error("String version clould not be created.",e);
		}
	
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
					if(attributeReference.getType() == PaAttributeTypeEnum.IETF_PA_TESTING.attributeType()){
						attributeList.add(this.getFileHash());
					}
				}
			}catch(ValidationException | NumberFormatException e){
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
		LOGGER.info("Remediation instructions were received. ");

		return new ArrayList<>(0);
	}
	
	@Override
	protected List<? extends ImAttribute> handleResult(
			PaAttributeValueAssessmentResult value, ImSessionContext context) {
		LOGGER.info("Assessment result is: " + value.getResult().toString() + " - (# " + value.getResult().number() +")");
		return new ArrayList<>(0);
	}

	private PaAttribute getFileHash() throws ValidationException {
		String content = "";
		
		byte[] newDigest = null;
		try{
			MessageDigest md = MessageDigest.getInstance(this.messageDigestIdentifier);
			DigestInputStream dis = null;
			File f = new File(this.filePath);
			if(f.exists() && f.canRead()){
				try{
					dis = new DigestInputStream(new BufferedInputStream(new FileInputStream(f)),md);
					
					while ((dis.read()) != -1){}
					newDigest = md.digest();
				} catch (IOException e){
					LOGGER.error("Could not check file because of an error.",e);
				} finally {
					if(dis != null){
						try{
							dis.close();
						} catch (IOException e){
							// ignore
						}
					}
				}
			}
		}catch(NoSuchAlgorithmException e){
			LOGGER.error("Could not check file because of an error.",e);
		}
		
		if(newDigest != null){
			content = DatatypeConverter.printBase64Binary(newDigest);
		}
		
		return PaAttributeFactoryIetf.createTestValue(content);
	}

	@Override
	public synchronized List<ImAttribute> lastCall(ImSessionContext context) {
		
		LOGGER.info("Last call received.");
		return new ArrayList<>(0);
	}
}
