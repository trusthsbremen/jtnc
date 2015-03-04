package org.ietf.nea.pa.serialize.writer.bytebuffer;

import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.TcgProtocolBindingIdentifier;
import de.hsbremen.tc.tnc.message.m.enums.TcgMProtocolBindingEnum;
import de.hsbremen.tc.tnc.message.m.message.ImMessage;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;

public class PaWriterFactory {

    public static TcgProtocolBindingIdentifier getProtocolIdentifier(){
        return TcgMProtocolBindingEnum.M1;
    }
    
	@SuppressWarnings({"unchecked","rawtypes"})
	public static ImWriter<ImMessage> createProductionDefault(){

		/* 
		 * TODO Remove raw types and unchecked conversion.
		 * Unfortunately I could not find a way around using 
		 * raw types and unchecked conversion my be some one 
		 * else can.
		 */
	
		
		PaAttributeHeaderWriter aWriter = new PaAttributeHeaderWriter();
		
		PaMessageHeaderWriter mWriter = new PaMessageHeaderWriter();
		
		PaWriter writer = new PaWriter(mWriter, aWriter);

		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_ASSESSMENT_RESULT.id(), 
				(ImWriter)new PaAttributeAssessmentResultValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_ATTRIBUTE_REQUEST.id(),
				(ImWriter)new PaAttributeAttributeRequestValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_FACTORY_DEFAULT_PW_ENABLED.id(),
				(ImWriter)new PaAttributeFactoryDefaultPasswordEnabledValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_FORWARDING_ENABLED.id(),
				(ImWriter)new PaAttributeForwardingEnabledValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_INSTALLED_PACKAGES.id(),
				(ImWriter)new PaAttributeInstalledPackagesValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_NUMERIC_VERSION.id(),
				(ImWriter)new PaAttributeNumericVersionValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_OPERATIONAL_STATUS.id(),
				(ImWriter)new PaAttributeOperationalStatusValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_PORT_FILTER.id(),
				(ImWriter)new PaAttributePortFilterValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_PRODUCT_INFORMATION.id(),
				(ImWriter)new PaAttributeProductInformationValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_STRING_VERSION.id(),
				(ImWriter)new PaAttributeStringVersionValueWriter());
		
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_ERROR.id(),
				(ImWriter)new PaAttributeErrorValueWriter(new PaAttributeErrorInformationInvalidParamValueWriter(), 
						new PaAttributeErrorInformationUnsupportedVersionValueWriter(), 
						new PaAttributeErrorInformationUnsupportedAttributeValueWriter()
						)
		);
		
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_REMEDIATION_INSTRUCTIONS.id(),
				(ImWriter)new PaAttributeRemediationParametersValueWriter(
						new PaAttributeRemediationParameterStringValueWriter(),
						new PaAttributeRemediationParameterUriValueWriter()
						)
		);
		
		return writer;
	}
	
	@SuppressWarnings({"unchecked","rawtypes"})
	public static ImWriter<ImMessage> createTestingDefault(){
	
		/* 
		 * TODO Remove raw types and unchecked conversion.
		 * Unfortunately I could not find a way around using 
		 * raw types and unchecked conversion my be some one 
		 * else can.
		 */
		
		PaWriter writer = (PaWriter) createProductionDefault();
		
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_TESTING.id(),
				(ImWriter)new PaAttributeTestingValueWriter());
		
		return writer;
		
	}
	
}
