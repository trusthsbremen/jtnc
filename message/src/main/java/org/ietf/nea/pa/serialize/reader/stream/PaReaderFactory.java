package org.ietf.nea.pa.serialize.reader.stream;

import org.ietf.nea.pa.attribute.PaAttributeHeaderBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueAssessmentResultBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueAttributeRequestBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueErrorBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueFactoryDefaultPasswordEnabledBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueForwardingEnabledBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueInstalledPackagesBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueNumericVersionBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueOperationalStatusBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValuePortFilterBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueProductInformationBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueRemediationParametersBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueStringVersionBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueTestingBuilderIetf;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationInvalidParamBuilderIetf;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedAttributeBuilderIetf;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedVersionBuilderIetf;
import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterStringBuilderIetf;
import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterUriBuilderIetf;
import org.ietf.nea.pa.message.PaMessageHeaderBuilderIetf;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.m.enums.TcgMProtocolEnum;
import de.hsbremen.tc.tnc.message.m.enums.TcgMVersionEnum;
import de.hsbremen.tc.tnc.message.m.serialize.ImMessageContainer;
import de.hsbremen.tc.tnc.message.m.serialize.stream.ImReader;

public class PaReaderFactory {

    public static String getMProtocol(){
        return TcgMProtocolEnum.M.value();
    }
    
    public static String getMVersion(){
        return TcgMVersionEnum.V1.value();
    }
    
	@SuppressWarnings({"unchecked","rawtypes"})
	public static ImReader<ImMessageContainer> createProductionDefault(){

		/* 
		 * TODO Remove raw types and unchecked conversion.
		 * Unfortunately I could not find a way around using 
		 * raw types and unchecked conversion my be some one 
		 * else can.
		 */
	
		
		PaMessageHeaderReader mReader = new PaMessageHeaderReader(new PaMessageHeaderBuilderIetf());
		
		PaAttributeHeaderReader aReader = new PaAttributeHeaderReader(new PaAttributeHeaderBuilderIetf());
		
		PaReader reader = new PaReader(mReader, aReader);
		
		reader.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_ASSESSMENT_RESULT.attributeType(),
				(ImReader)new PaAttributeAssessmentResultValueReader(new PaAttributeValueAssessmentResultBuilderIetf()));
		
		reader.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_ATTRIBUTE_REQUEST.attributeType(),
				(ImReader)new PaAttributeAttributeRequestValueReader(new PaAttributeValueAttributeRequestBuilderIetf()));
		
		reader.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_FACTORY_DEFAULT_PW_ENABLED.attributeType(),
				(ImReader)new PaAttributeFactoryDefaultPasswordEnabledValueReader(new PaAttributeValueFactoryDefaultPasswordEnabledBuilderIetf()));
		
		reader.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_FORWARDING_ENABLED.attributeType(),
				(ImReader)new PaAttributeForwardingEnabledValueReader(new PaAttributeValueForwardingEnabledBuilderIetf()));
		
		reader.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_INSTALLED_PACKAGES.attributeType(),
				(ImReader)new PaAttributeInstalledPackagesValueReader(new PaAttributeValueInstalledPackagesBuilderIetf()));
		
		reader.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_NUMERIC_VERSION.attributeType(),
				(ImReader)new PaAttributeNumericVersionValueReader(new PaAttributeValueNumericVersionBuilderIetf()));
		
		reader.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_OPERATIONAL_STATUS.attributeType(),
				(ImReader)new PaAttributeOperationalStatusValueReader(new PaAttributeValueOperationalStatusBuilderIetf()));
		
		reader.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_PORT_FILTER.attributeType(),
				(ImReader)new PaAttributePortFilterValueReader(new PaAttributeValuePortFilterBuilderIetf()));
		
		reader.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_PRODUCT_INFORMATION.attributeType(),
				(ImReader)new PaAttributeProductInformationValueReader(new PaAttributeValueProductInformationBuilderIetf()));
		
		reader.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_STRING_VERSION.attributeType(),
				(ImReader)new PaAttributeStringVersionValueReader(new PaAttributeValueStringVersionBuilderIetf()));
		
		reader.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_ATTRIBUTE_REQUEST.attributeType(),
				(ImReader)new PaAttributeAttributeRequestValueReader(new PaAttributeValueAttributeRequestBuilderIetf()));
		
		reader.add(IETFConstants.IETF_PEN_VENDORID,  PaAttributeTypeEnum.IETF_PA_REMEDIATION_INSTRUCTIONS.attributeType(),
				(ImReader)new PaAttributeRemediationParametersValueReader(
						new PaAttributeValueRemediationParametersBuilderIetf(), 
						new PaAttributeRemediationParameterStringValueReader(new PaAttributeValueRemediationParameterStringBuilderIetf()),
						new PaAttributeRemediationParameterUriValueReader(new PaAttributeValueRemediationParameterUriBuilderIetf())
						)
		);
		
		reader.add(IETFConstants.IETF_PEN_VENDORID,  PaAttributeTypeEnum.IETF_PA_ERROR.attributeType(),
				(ImReader)new PaAttributeErrorValueReader(
						new PaAttributeValueErrorBuilderIetf(), 
						new PaAttributeErrorInformationInvalidParamValueReader(
								new PaAttributeValueErrorInformationInvalidParamBuilderIetf()), 
						new PaAttributeErrorInformationUnsupportedVersionValueReader(
								new PaAttributeValueErrorInformationUnsupportedVersionBuilderIetf()),
						new PaAttributeErrorInformationUnsupportedAttributeValueReader(
								new PaAttributeValueErrorInformationUnsupportedAttributeBuilderIetf(), new PaAttributeHeaderBuilderIetf())
						)
		);
	
		
		return reader;
	}
	
	@SuppressWarnings({"unchecked","rawtypes"})
	public static ImReader<ImMessageContainer> createTestingDefault(){
	
		/* 
		 * TODO Remove raw types and unchecked conversion.
		 * Unfortunately I could not find a way around using 
		 * raw types and unchecked conversion my be some one 
		 * else can.
		 */
		
		PaReader reader = (PaReader) createProductionDefault();
		
		reader.add(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_TESTING.attributeType(),
				(ImReader)new PaAttributeTestingValueReader(new PaAttributeValueTestingBuilderIetf()));
		
		return reader;
	}
}
