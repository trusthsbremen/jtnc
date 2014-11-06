package org.ietf.nea.pa.attribute;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ietf.nea.pa.attribute.enums.PaAttributeAssessmentResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeFactoryDefaultPasswordStatusEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeForwardingStatusEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeOperationLastResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeOperationStatusEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributePortFilterStatus;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLength;
import org.ietf.nea.pa.attribute.util.AbstractPaAttributeValueErrorInformation;
import org.ietf.nea.pa.attribute.util.AbstractPaAttributeValueRemediationParameter;
import org.ietf.nea.pa.attribute.util.AttributeReference;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationFactoryIetf;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationInvalidParam;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedAttribute;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedVersion;
import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterFactoryIetf;
import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterString;
import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterUri;
import org.ietf.nea.pa.attribute.util.PackageEntry;
import org.ietf.nea.pa.attribute.util.PortFilterEntry;
import org.ietf.nea.pa.message.PaMessageHeader;

import de.hsbremen.tc.tnc.IETFConstants;

public class PaAttributeValueBuilderIetf {
	
	
	public static PaAttributeValueAssessmentResult createAssessmentResultValue(final PaAttributeAssessmentResultEnum result){
		
		if(result == null){
			throw new NullPointerException("Result cannot be null.");
		}
		
		return new PaAttributeValueAssessmentResult(PaAttributeTlvFixedLength.ASS_RES.length(),result);
	}
	
	public static PaAttributeValueFactoryDefaultPasswordEnabled createFactoryDefaultPasswordValue(final PaAttributeFactoryDefaultPasswordStatusEnum status){
		
		if(status == null){
			throw new NullPointerException("Status cannot be null.");
		}
		
		return new PaAttributeValueFactoryDefaultPasswordEnabled(PaAttributeTlvFixedLength.FAC_PW.length(),status);
	}
	
	public static PaAttributeValueForwardingEnabled createForwardingEnabledValue(final PaAttributeForwardingStatusEnum status){
		
		if(status == null){
			throw new NullPointerException("Status cannot be null.");
		}
		
		return new PaAttributeValueForwardingEnabled(PaAttributeTlvFixedLength.FWD_EN.length(),status);
	}
	
	public static PaAttributeValueAttributeRequest createAttributeRequestValue(final AttributeReference reference, AttributeReference...moreReferences){
		
		if(reference == null ){
			throw new NullPointerException("Reference cannot be null, there must be at least one reference.");
		}
		
		List<AttributeReference> referenceList = new LinkedList<>();
		referenceList.add(reference);
		if(moreReferences != null){
			for (AttributeReference attributeReference : moreReferences) {
				if(attributeReference != null){
					referenceList.add(attributeReference);
				}
			}
		}
		
		long length = PaAttributeTlvFixedLength.ATT_REQ.length() * referenceList.size();
		
		return new PaAttributeValueAttributeRequest(length, referenceList);
	}
	
	public static PaAttributeValueProductInformation createProductInformationValue(long vendorId, int productId, String productName){
		
		if(vendorId == 0 && productId != 0){
			throw new IllegalArgumentException("Because vendor ID is zero product ID must be zero.");
		}
		
		if(productName == null){
			throw new NullPointerException("Product name cannot be null.");
		}
		
		long length = PaAttributeTlvFixedLength.PRO_INF.length();
		if(productName.length() > 0){
			length += productName.getBytes(Charset.forName("UTF-8")).length;
		}
		
		return new PaAttributeValueProductInformation(length, vendorId, productId, productName);
	}
		
	public static PaAttributeValueNumericVersion createNumericVersionValue(long majorVersion, long minorVersion, long buildVersion, int servicePackMajor, int servicePackMinor){
		// Nothing to check here. Maybe negativ values are bad, but could not fully determine this.
		return new PaAttributeValueNumericVersion(PaAttributeTlvFixedLength.NUM_VER.length(), majorVersion, minorVersion, buildVersion, servicePackMajor, servicePackMinor);
	}
	
	public static PaAttributeValueStringVersion createStringVersionValue(String versionNumber, String buildVersion, String configVersion){
		
		if(versionNumber == null){
			versionNumber = "";
		}
		if(buildVersion == null){
			buildVersion = "";
		}
		if(configVersion == null){
			configVersion = "";
		}
		
		if(versionNumber.length() > 0xFF){
			throw new IllegalArgumentException("Version number length " +versionNumber.length()+ "is to long.");
		}
		
		if(buildVersion.length() > 0xFF){
			throw new IllegalArgumentException("Build number length " +buildVersion.length()+ "is to long.");
		}

		if(configVersion.length() > 0xFF){
			throw new IllegalArgumentException("Build number length " +configVersion.length()+ "is to long.");
		}
		
		long length = PaAttributeTlvFixedLength.STR_VER.length();
		if(versionNumber.length() > 0){
			length += versionNumber.getBytes(Charset.forName("UTF-8")).length;
		}
		if(buildVersion.length() > 0){
			length += buildVersion.getBytes(Charset.forName("UTF-8")).length;
		}
		if(configVersion.length() > 0){
			length += configVersion.getBytes(Charset.forName("UTF-8")).length;
		}
		
		return new PaAttributeValueStringVersion(length, versionNumber, buildVersion, configVersion);
	}
	
	public static PaAttributeValueOperationalStatus createOperationalStatusValue(PaAttributeOperationStatusEnum status, PaAttributeOperationLastResultEnum result, Date lastUse){
		
		if(status == null){
			throw new NullPointerException("Operational status cannot be null.");
		}
		
		if(result == null){
			throw new NullPointerException("Operational result cannot be null.");
		}
		
		if(lastUse == null){
			throw new NullPointerException("Date of last use cannot be null, use 0000-00-00T00:00:00Z instead.");
		}
		
		return new PaAttributeValueOperationalStatus(PaAttributeTlvFixedLength.OP_STAT.length(), status, result, lastUse);
	}
	
	public static PaAttributeValuePortFilter createPortFilterValue(PortFilterEntry entry, PortFilterEntry... moreEntries){
		
		if(entry == null){
			throw new NullPointerException("Entry cannot be null, there must be at least one entry.");
		}
		
		List<PortFilterEntry> entries = new LinkedList<>();
		entries.add(entry);
		
		// check duplicates
		if(moreEntries != null){
			Map<Short,Map<Integer,PaAttributePortFilterStatus>> sorted = new HashMap<>();
			sorted.put(entry.getProtocolNumber(), new HashMap<Integer,PaAttributePortFilterStatus>());
			sorted.get(entry.getProtocolNumber()).put(entry.getPortNumber(), entry.getFilterStatus());
			
			for (PortFilterEntry pe : moreEntries) {
				if(pe != null){
					if(sorted.containsKey(pe.getProtocolNumber())){
						if(sorted.get(pe.getProtocolNumber()).containsKey(pe.getPortNumber())){
							if(sorted.get(pe.getProtocolNumber()).values().iterator().next() != pe.getFilterStatus()){
								throw new IllegalArgumentException("The port filter contains entries for the protocol " + pe.getProtocolNumber() + " with differnt blocking status.");
							}
							if(sorted.get(pe.getProtocolNumber()).get(pe.getPortNumber()) != pe.getFilterStatus()){
								throw new IllegalArgumentException("The port filter contains duplicate entries for the tupel " + pe.getProtocolNumber() + ":" +pe.getPortNumber() + " with differnt blocking status.");
							}else{
								throw new IllegalArgumentException("The port filter contains duplicate entries for the tupel " + pe.getProtocolNumber() + ":" +pe.getPortNumber() + ".");
							}
						}else{
							sorted.get(pe.getProtocolNumber()).put(pe.getPortNumber(), pe.getFilterStatus());
						}
					}else{
						sorted.put(pe.getProtocolNumber(), new HashMap<Integer,PaAttributePortFilterStatus>());
						sorted.get(pe.getProtocolNumber()).put(pe.getPortNumber(), pe.getFilterStatus());
					}
				}
			}
			
			entries.addAll(Arrays.asList(moreEntries));
		}

		long length = PaAttributeTlvFixedLength.PORT_FT.length() * entries.size();
		
		return new PaAttributeValuePortFilter(length, entries);
	}
	
	public static PaAttributeValueInstalledPackages createInstalledPackagesValue(List<PackageEntry> packages){
		
		if(packages == null){
			throw new NullPointerException("Packages cannot be null, you may use an empty list instead.");
		}
		
		long length = PaAttributeTlvFixedLength.INS_PKG.length();
		
		for (PackageEntry pkgEntry : packages) {
			length += pkgEntry.getPackageNameLength();
			length += pkgEntry.getPackageVersionLength();
			length += 2; // +2 for the fields that contain the length
		}
		
		return new PaAttributeValueInstalledPackages(length, packages);
	}
	
	public static PaAttributeValueRemediationParameters createRemediationParameterString(final long rpVendorId, final long rpType, final String remediationString, final String langCode){
		
		PaAttributeValueRemediationParameterString parameter = PaAttributeValueRemediationParameterFactoryIetf.createRemediationParameterString(rpVendorId, rpType, remediationString, langCode);
		
		return createRemediationParametersValue(rpVendorId, rpType, parameter);
	}
	
	public static PaAttributeValueRemediationParameters createRemediationParameterUri(final long rpVendorId, final long rpType, String uri){
		
		PaAttributeValueRemediationParameterUri parameter = PaAttributeValueRemediationParameterFactoryIetf.createRemediationParameterUri(rpVendorId, rpType, uri);
		
		return createRemediationParametersValue(rpVendorId, rpType, parameter);
		
	}
	
	private static PaAttributeValueRemediationParameters createRemediationParametersValue(final long rpVendorId, final long rpType, final AbstractPaAttributeValueRemediationParameter parameter){
		
		if(rpVendorId > IETFConstants.IETF_MAX_VENDOR_ID){
			throw new IllegalArgumentException("Vendor ID is greater than "+ Long.toString(IETFConstants.IETF_MAX_VENDOR_ID) + ".");
		}
		if(rpType > IETFConstants.IETF_MAX_TYPE){
			throw new IllegalArgumentException("Type is greater than "+ Long.toString(IETFConstants.IETF_MAX_TYPE) + ".");
		}

		long length = PaAttributeTlvFixedLength.REM_PAR.length() + parameter.getLength();
		
		return new PaAttributeValueRemediationParameters(rpVendorId, rpType, length,parameter);
	}
	
	public static PaAttributeValueError createErrorInformationUnsupportedVersion(final long errVendorId, final long errCode, final PaMessageHeader messageHeader, final short maxVersion, final short minVersion){
		
		PaAttributeValueErrorInformationUnsupportedVersion errorInformation = PaAttributeValueErrorInformationFactoryIetf.createErrorInformationUnsupportedVersion(errVendorId, errCode, messageHeader, maxVersion, minVersion);
		
		return createErrorValue(errVendorId, errCode, errorInformation);
	}
	
	public static PaAttributeValueError createErrorInformationUnsupportedAttribute(final long errVendorId, final long errCode, final PaMessageHeader messageHeader, final PaAttributeHeader attributeHeader){
		
		PaAttributeValueErrorInformationUnsupportedAttribute errorInformation = PaAttributeValueErrorInformationFactoryIetf.createErrorInformationUnsupportedAttribute(errVendorId, errCode, messageHeader, attributeHeader);
		
		return createErrorValue(errVendorId, errCode, errorInformation);
	}
	
	public static PaAttributeValueError createErrorInformationInvalidParameter(final long errVendorId, final long errCode, final PaMessageHeader messageHeader, final long offset){
		
		PaAttributeValueErrorInformationInvalidParam errorInformation = PaAttributeValueErrorInformationFactoryIetf.createErrorInformationInvalidParameter(errVendorId, errCode,  messageHeader, offset);
		
		return createErrorValue(errVendorId, errCode, errorInformation);
	}
	
	private static PaAttributeValueError createErrorValue(final long errVendorId, final long errCode, final AbstractPaAttributeValueErrorInformation errorInformation){
		
		if(errVendorId > IETFConstants.IETF_MAX_VENDOR_ID){
			throw new IllegalArgumentException("Vendor ID is greater than "+ Long.toString(IETFConstants.IETF_MAX_VENDOR_ID) + ".");
		}
		if(errCode > IETFConstants.IETF_MAX_TYPE){
			throw new IllegalArgumentException("Code is greater than "+ Long.toString(IETFConstants.IETF_MAX_TYPE) + ".");
		}

		long length = PaAttributeTlvFixedLength.ERR_INF.length() + errorInformation.getLength();
		
		return new PaAttributeValueError(length, errVendorId, errCode, errorInformation);
	}
	
}
