package org.ietf.nea.pa.serialize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttribute;
import org.ietf.nea.pa.attribute.PaAttributeFactoryIetf;
import org.ietf.nea.pa.attribute.enums.PaAttributeAssessmentResultEnum;
import org.ietf.nea.pa.attribute.util.PackageEntry;
import org.ietf.nea.pa.message.PaMessage;
import org.ietf.nea.pa.message.PaMessageFactoryIetf;

public class TestData {

	byte[] mixedMessage = new byte[]{1, 0, 0, 0, 0, 0, 0, 45, 0, 0, 0, 0, 0, 0, 0, 9,
			0, 0, 0, 16, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 28, 0, 0, 0, 1,
			0, 0, 0, 7, 0, 0, 1, 2, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 46,
			0, 0, 0, 2, 8, 105, 112, 116, 97, 98, 108, 101, 115, 6, 49, 46, 52, 46, 49,
			50, 4, 106, 97, 118, 97, 8, 49, 46, 55, 46, 48, 95, 52, 48};

	byte[] numericVersionMessage = new byte[]{1, 0, 0, 0, 0, 0, 0, 38, 0, 0, 0, 0, 
			0, 0, 0, 3, 0, 0, 0, 28, 0, 0, 0, 1, 0, 0, 0, 7, 0, 0, 1, 2, 0, 0, 0, 1};

	byte[] installedPackageMessage = new byte[]{1, 0, 0, 0, 0, 0, 0, 39, 0, 0, 0, 0,
			0, 0, 0, 7, 0, 0, 0, 46, 0, 0, 0, 2, 8, 105, 112, 116, 97, 98, 108, 101,
			115, 6, 49, 46, 52, 46, 49, 50, 4, 106, 97, 118, 97, 8, 49, 46, 55, 46, 
			48, 95, 52, 48};
	
	byte[] assessmentMessage = new byte[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 16, 0, 0, 0, 1};
	
	public byte[] getMessageWithNumericVersionAsByteArray() throws IOException{
		return numericVersionMessage;
	}
	
	public byte[] getMessageWithAssessmentResultAsByteArray() throws IOException{
		return assessmentMessage;
	}
	
	public byte[] getMessageWithInstalledPackagesAsByteArray() throws IOException{
		return installedPackageMessage;
	}
	
	public byte[] getBatchWithMixedAttributesAsByteArray() throws IOException{
		return mixedMessage;
	}
	
	public PaMessage getMessageWithNumericVersion() throws RuleException{
		
		PaAttribute a = PaAttributeFactoryIetf.createNumericVersion(1,7,258,0,1);
		List<PaAttribute> attributes = new ArrayList<>();
		attributes.add(a);
		return PaMessageFactoryIetf.createMessage((short)1, (long)new Random().nextInt(100) , attributes);
		
	}
	
	public PaMessage getMessageWithAssessmentResult() throws RuleException{
		
		PaAttribute a = PaAttributeFactoryIetf.createAssessmentResult(PaAttributeAssessmentResultEnum.MINOR_DIFFERENCES);
		List<PaAttribute> attributes = new ArrayList<>();
		attributes.add(a);
		return PaMessageFactoryIetf.createMessage((short)1, (long)new Random().nextInt(100) , attributes);
		
	}
	
	public PaMessage getMessageWithInstalledPackages() throws RuleException{
		
		List<PackageEntry> packages = new ArrayList<>();
		packages.add(new PackageEntry("iptables", "1.4.12"));
		packages.add(new PackageEntry("java","1.7.0_40"));
		
		PaAttribute a = PaAttributeFactoryIetf.createInstalledPackages(packages);
		List<PaAttribute> attributes = new ArrayList<>();
		attributes.add(a);
		return PaMessageFactoryIetf.createMessage((short)1, (long)new Random().nextInt(100) , attributes);
		
	}
	
	public PaMessage getMessageWithMixedAttributes() throws RuleException{
		
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
		
		
		
		return PaMessageFactoryIetf.createMessage((short)1, (long)new Random().nextInt(100) , attributes);
		
	}
	
//	
//	public PbBatch getInvalidImMessage() throws RuleException{
//		PbMessageHeaderBuilderIetf builder = new PbMessageHeaderBuilderIetf();
//		builder.setFlags((byte)0);
//		builder.setVendorId(IETFConstants.IETF_PEN_VENDORID);
//		builder.setType(PbMessageTypeEnum.IETF_PB_PA.messageType());
//
//
//		List<PbMessage> messages = new ArrayList<>();
//		messages.add(new PbMessage(builder.toMessageHeader(), PbMessageValueBuilderIetf.createImValue(new PbMessageImFlagsEnum[0], 0, 0, (short)0xFFFF, (short)1, new byte[]{ -128, 34, 12})));
//		
//		
//		return PbBatchFactoryIetf.createClientData(messages);
//	}
	
}
