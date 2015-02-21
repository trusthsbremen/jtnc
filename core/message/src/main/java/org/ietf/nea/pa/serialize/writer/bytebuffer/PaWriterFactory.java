package org.ietf.nea.pa.serialize.writer.bytebuffer;

import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.m.enums.TcgMProtocolEnum;
import de.hsbremen.tc.tnc.message.m.enums.TcgMVersionEnum;
import de.hsbremen.tc.tnc.message.m.message.ImMessage;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;

public class PaWriterFactory {

    public static String getMProtocol(){
        return TcgMProtocolEnum.M.value();
    }
    
    public static String getMVersion(){
        return TcgMVersionEnum.V1.value();
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

		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_ASSESSMENT_RESULT.attributeType(), 
				(ImWriter)new PaAttributeAssessmentResultValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_ATTRIBUTE_REQUEST.attributeType(),
				(ImWriter)new PaAttributeAttributeRequestValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_FACTORY_DEFAULT_PW_ENABLED.attributeType(),
				(ImWriter)new PaAttributeFactoryDefaultPasswordEnabledValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_FORWARDING_ENABLED.attributeType(),
				(ImWriter)new PaAttributeForwardingEnabledValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_INSTALLED_PACKAGES.attributeType(),
				(ImWriter)new PaAttributeInstalledPackagesValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_NUMERIC_VERSION.attributeType(),
				(ImWriter)new PaAttributeNumericVersionValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_OPERATIONAL_STATUS.attributeType(),
				(ImWriter)new PaAttributeOperationalStatusValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_PORT_FILTER.attributeType(),
				(ImWriter)new PaAttributePortFilterValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_PRODUCT_INFORMATION.attributeType(),
				(ImWriter)new PaAttributeProductInformationValueWriter());
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_STRING_VERSION.attributeType(),
				(ImWriter)new PaAttributeStringVersionValueWriter());
		
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_ERROR.attributeType(),
				(ImWriter)new PaAttributeErrorValueWriter(new PaAttributeErrorInformationInvalidParamValueWriter(), 
						new PaAttributeErrorInformationUnsupportedVersionValueWriter(), 
						new PaAttributeErrorInformationUnsupportedAttributeValueWriter()
						)
		);
		
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_REMEDIATION_INSTRUCTIONS.attributeType(),
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
		
		writer.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_TESTING.attributeType(),
				(ImWriter)new PaAttributeTestingValueWriter());
		
		return writer;
		
	}
	
}
