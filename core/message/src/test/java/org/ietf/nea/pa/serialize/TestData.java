package org.ietf.nea.pa.serialize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.BasicConfigurator;
import org.ietf.nea.pa.attribute.PaAttribute;
import org.ietf.nea.pa.attribute.PaAttributeFactoryIetf;
import org.ietf.nea.pa.attribute.enums.PaAttributeAssessmentResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.attribute.util.AttributeReferenceEntry;
import org.ietf.nea.pa.attribute.util.PackageEntry;
import org.ietf.nea.pa.message.PaMessage;
import org.ietf.nea.pa.message.PaMessageFactoryIetf;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.ValidationException;

public class TestData {

	byte[] mixedMessage = new byte[]{1, 0, 0, 0, 0, 0, 0, 45, 0, 0, 0, 0, 0, 0, 0, 9,
			0, 0, 0, 16, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 28, 0, 0, 0, 1,
			0, 0, 0, 7, 0, 0, 1, 2, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 46,
			0, 0, 0, 2, 8, 105, 112, 116, 97, 98, 108, 101, 115, 6, 49, 46, 52, 46, 49,
			50, 4, 106, 97, 118, 97, 8, 49, 46, 55, 46, 48, 95, 52, 48};

	byte[] numericVersionMessage = new byte[]{1, 0, 0, 0, 0, 0, 0, 38, 0, 0, 0, 0, 
			0, 0, 0, 3, 0, 0, 0, 28, 0, 0, 0, 1, 0, 0, 0, 7, 0, 0, 1, 2, 0, 0, 0, 1};
	
	byte[] faultyNumericVersionMessage = new byte[]{1, 0, 0, 0,
													0, 0, 0, 38,
													0, 0, 0, 0, 
													0, 0, 0, 3, 
													0, 0, 0, 11, 
													(byte)0x80, 0, 0, 1, 
													0, 0, 0, 7, 
													0, 0, 1, 2, 
													0, 0, 0, 1};
	
	byte[] faultymixedMessage = new byte[]{1, 0, 0, 0,
											0, 0, 0, 45,
											0, 0, 0, 0,
											0, 0, 0, 9,
											0, 0, 0, 16,
											0, 0, 0, 1,
											// next msg
											0, 0, 0, 0,
											0, 0, 0, 3,
											0, 0, 0, 28,
											0, 0, 0, 1,
											0, 0, 0, 7,
											0, 0, 1, 2,
											0, 0, 0, 1,
											// next msg
											0, 0, 0, 0,
											0, 0, 0, 7, 
											0, 0, 0, 47,
											0, 0, 0, 2,
											9,105, 112, 116,
											97, 98, 108, 101,0x00,
											115, 6, 49, 46, 52,
											46, 49,	50,	4,
											106, 97, 118, 97,
											8, 49, 46, 55,
											46, 48, 95,52, 48};

	byte[] installedPackageMessage = new byte[]{1, 0, 0, 0, 0, 0, 0, 39, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 46, 0, 0, 0, 2, 8, 105, 112, 116, 97, 98, 108, 101, 115, 6, 49, 46, 52, 46, 49, 50, 4, 106, 97, 118, 97, 8, 49, 46, 55, 46, 48, 95, 52, 48};
	byte[] installedPackageMessag2 = new byte[]{1, 0, 0, 0, 0, 0, 0, 77, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 47, 0, 0, 0, 2, 9, 105, 112, 116, 97, 98, 108, 101, 115, 0, 6, 49, 46, 52, 46, 49, 50, 4, 106, 97, 118, 97, 8, 49, 46, 55, 46, 48, 95, 52, 48};
	
	byte[] assessmentMessage = new byte[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 16, 0, 0, 0, 1};
	
	byte[] attributeRequestStringVersion = new byte[] {1, 0, 0, 0, 0, 0, 0, 17, 0, 0, 0, 0, 0, 0, 0, 1,
	                                                   0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 4}; 
	
	public byte[] getMessageWithNumericVersionAsByteArray() throws IOException{
		return numericVersionMessage;
	}
	
	public byte[] getMessageWithFaultyNumericVersionAsByteArray() throws IOException{
		return faultyNumericVersionMessage;
	}
	
	public byte[] getMessageWithAssessmentResultAsByteArray() throws IOException{
		return assessmentMessage;
	}
	
	public byte[] getMessageWithInstalledPackagesAsByteArray() throws IOException{
		return installedPackageMessage;
	}
	
	public byte[] getMessageWithAttributeRequestAsByteArray() throws IOException{
		return attributeRequestStringVersion;
	}
	
	public byte[] getMessageWithMixedAttributesAsByteArray() throws IOException{
		return mixedMessage;
	}
	
	public byte[] getMessageWithFaultyMixedAttributesAsByteArray() throws IOException{
		return faultymixedMessage;
	}
	
	public PaMessage getMessageWithNumericVersion() throws ValidationException{
		
		PaAttribute a = PaAttributeFactoryIetf.createNumericVersion(1,7,258,0,1);
		List<PaAttribute> attributes = new ArrayList<>();
		attributes.add(a);
		return PaMessageFactoryIetf.createMessage((long)new Random().nextInt(100) , attributes);
		
	}
	
	public PaMessage getMessageWithAssessmentResult() throws ValidationException{
		
		PaAttribute a = PaAttributeFactoryIetf.createAssessmentResult(PaAttributeAssessmentResultEnum.MINOR_DIFFERENCES);
		List<PaAttribute> attributes = new ArrayList<>();
		attributes.add(a);
		return PaMessageFactoryIetf.createMessage((long)new Random().nextInt(100) , attributes);
		
	}
	
	public PaMessage getMessageWithInstalledPackages() throws ValidationException{
		
		List<PackageEntry> packages = new ArrayList<>();
		packages.add(new PackageEntry("iptables\0", "1.4.12"));
		packages.add(new PackageEntry("java","1.7.0_40"));
		
		PaAttribute a = PaAttributeFactoryIetf.createInstalledPackages(packages);
		List<PaAttribute> attributes = new ArrayList<>();
		attributes.add(a);
		return PaMessageFactoryIetf.createMessage((long)new Random().nextInt(100) , attributes);
		
	}
	
	public PaMessage getMessageWithAttributeRequest() throws ValidationException{
		
		AttributeReferenceEntry ref1 = new AttributeReferenceEntry(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_STRING_VERSION.id());
		
		PaAttribute a = PaAttributeFactoryIetf.createAttributeRequest(ref1);
		List<PaAttribute> attributes = new ArrayList<>();
		attributes.add(a);
		return PaMessageFactoryIetf.createMessage((long)new Random().nextInt(100) , attributes);
		
	}
	
	public PaMessage getMessageWithMixedAttributes() throws ValidationException{
		
		List<PaAttribute> attributes = new ArrayList<>();
		
		PaAttribute b = PaAttributeFactoryIetf.createAssessmentResult(PaAttributeAssessmentResultEnum.MINOR_DIFFERENCES);
		attributes.add(b);
		
		PaAttribute c = PaAttributeFactoryIetf.createNumericVersion(1,7,258,0,1);
		attributes.add(c);
		
		List<PackageEntry> packages = new ArrayList<>();
		packages.add(new PackageEntry("iptables", "1.4.12"));
		packages.add(new PackageEntry("java","1.7.0_40"));
		PaAttribute a = PaAttributeFactoryIetf.createInstalledPackages(packages);
		attributes.add(a);
		
		
		
		return PaMessageFactoryIetf.createMessage((long)new Random().nextInt(100) , attributes);
		
	}
	
	public static final void setLogSettings(){
		BasicConfigurator.configure();
	}
	
	public static String getTestDescriptionHead(String className, String head){
		StringBuilder b = new StringBuilder();
		b.append("----- \n");
		b.append(className);
		b.append(" - ");
		b.append(head);
		b.append("\n----- \n");
	
		return b.toString();
	}
	
}
