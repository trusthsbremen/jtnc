package org.ietf.nea.pt.value;

import java.util.ArrayList;
import java.util.List;

import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.value.enums.PtTlsSaslResultEnum;
import org.ietf.nea.pt.value.util.SaslMechanism;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

public class PtTlsMessageValueBuilderIetf {
	
	public static PtTlsMessageValueVersionRequest createVersionRequestValue(final short minVersion, final short maxVersion, final short preferredVersion){

		if(minVersion > 0xFF){
			throw new IllegalArgumentException("Min. version is greater than "+ Short.toString((short)0xFF) + ".");
		}
		if(maxVersion > 0xFF){
			throw new IllegalArgumentException("Max. version is greater than "+ Short.toString((short)0xFF) + ".");
		}

		if(minVersion > 0xFF){
			throw new IllegalArgumentException("Prefered version is greater than "+ Short.toString((short)0xFF) + ".");
		}
		
		long length = PtTlsMessageTlvFixedLengthEnum.VER_REQ.length();
		
		return new PtTlsMessageValueVersionRequest(length, minVersion, maxVersion, preferredVersion);
	}
	
	public static PtTlsMessageValueVersionResponse createVersionResponseValue(final short selectedVersion){

		if(selectedVersion > 0xFF){
			throw new IllegalArgumentException("Min. version is greater than "+ Short.toString((short)0xFF) + ".");
		}
		
		long length = PtTlsMessageTlvFixedLengthEnum.VER_RES.length();
		
		return new PtTlsMessageValueVersionResponse(length, selectedVersion);
	}
	
	public static PtTlsMessageValueSaslMechanisms createSaslMechanismsValue(final SaslMechanism... mechanisms){
		
		List<SaslMechanism> mechs = new ArrayList<>();
		
		long length = 0;
		
		if(mechanisms != null){
			
			for (SaslMechanism saslMechanism : mechanisms) {
				if(saslMechanism.getName().matches("[A-Z0-9\\-_]{1,20}")){
					mechs.add(saslMechanism);
					length += saslMechanism.getNameLength();
					length += 1; // the names' length field is one byte
				}else{
					throw new IllegalArgumentException("SASL mechanism name " +saslMechanism.getName()+ " does not match the naming requirements.");
				}
			}
			
		}
		
		return new PtTlsMessageValueSaslMechanisms(length, mechs);
	}
	
	public static PtTlsMessageValueSaslMechanismSelection createSaslMechanisSelectionValue(final SaslMechanism mechanism){
		return createSaslMechanisSelectionValue(mechanism,null);
	}

	public static PtTlsMessageValueSaslMechanismSelection createSaslMechanisSelectionValue(final SaslMechanism mechanism, byte[] initialData){
	
		NotNull.check("Mechanism cannot be null.", mechanism);
		
		long length = 0;
		
		if(mechanism.getName().matches("[A-Z0-9\\-_]{1,20}")){
			length += mechanism.getNameLength();
			length += PtTlsMessageTlvFixedLengthEnum.SASL_SEL.length();
		}else{
			throw new IllegalArgumentException("SASL mechanism name " +mechanism.getName()+ " does not match the naming requirements.");
		}
		
		if(initialData != null){
			length += initialData.length;
			return new PtTlsMessageValueSaslMechanismSelection(length, mechanism, initialData);
		}else{
			return new PtTlsMessageValueSaslMechanismSelection(length, mechanism);
		}

	}
	
	public static PtTlsMessageValueSaslAuthenticationData createSaslAuthenticationDataValue(final byte[] authenticationData){
		
		NotNull.check("Data cannot be null.", authenticationData);
		
		return new PtTlsMessageValueSaslAuthenticationData(authenticationData.length, authenticationData);
	}
	
	public static PtTlsMessageValueSaslResult createSaslResultValue(final PtTlsSaslResultEnum result){
		return createSaslResultValue(result,null);
	}
	
	public static PtTlsMessageValueSaslResult createSaslResultValue(final PtTlsSaslResultEnum result, byte[] resultData){
		
		NotNull.check("Result cannot be null.", result);
		
		long length = PtTlsMessageTlvFixedLengthEnum.SASL_RLT.length();
		
		if(resultData != null){
			length += resultData.length;
			return new PtTlsMessageValueSaslResult(length, result, resultData);
		}else{
			return new PtTlsMessageValueSaslResult(length, result);
		}
	}
	
	public static PtTlsMessageValueError createErrorValue(final long errorVendorId, final long errorCode, final byte[] messageCopy ){

		NotNull.check("Message copy cannot be null.", messageCopy);
		
		if(errorVendorId > IETFConstants.IETF_MAX_VENDOR_ID){
			throw new IllegalArgumentException("Vendor ID is greater than "+ Long.toString(IETFConstants.IETF_MAX_VENDOR_ID) + ".");
		}
		if(errorCode > IETFConstants.IETF_MAX_TYPE){
			throw new IllegalArgumentException("Type is greater than "+ Long.toString(IETFConstants.IETF_MAX_TYPE) + ".");
		}
		
		long length = PtTlsMessageTlvFixedLengthEnum.ERR_VALUE.length() + messageCopy.length;
		
		return new PtTlsMessageValueError(errorVendorId, errorCode, length, messageCopy);
	}
	
	public static PtTlsMessageValuePbBatch createPbBatchValue(final ByteBuffer tnccsData){
		
		NotNull.check("Data cannot be null.", tnccsData);
		
		return new PtTlsMessageValuePbBatch(tnccsData.bytesWritten(), tnccsData);
	}
	
	
}