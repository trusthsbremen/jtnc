package org.ietf.nea.pt.serialize;

import java.io.UnsupportedEncodingException;

import org.ietf.nea.pt.message.PtTlsMessage;
import org.ietf.nea.pt.message.PtTlsMessageFactoryIetf;
import org.ietf.nea.pt.value.enums.PtTlsSaslResultEnum;
import org.ietf.nea.pt.value.util.SaslMechanismEntry;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.message.util.DefaultByteBuffer;

public class TestData {

	byte[] versionRequestByte = new byte[]{0,0,0,0,
			 0,0,0,1,
			 0,0,0,20,
			 0,0,0,1,
		  // -- content --
			 0,1,1,1};
	
	byte[] faultyVersionRequestByte = new byte[]{0,0,0,0,
			 -1,-1,-1,-1,
			 0,0,0,20,
			 0,0,0,1,
		  // -- content --
			 0,1,1,1};
	
	byte[] versionResponseByte = new byte[]{0,0,0,0,
			 0,0,0,2,
			 0,0,0,20,
			 0,0,0,2,
	      // -- content --
			 0,0,0,1};

	byte[] saslMechanismsByte = new byte[]{0,0,0,0,
			 0,0,0,3,
			 0,0,0,22,
			 0,0,0,3,
	      // -- content --
			 5,80,76,65,73,78};
	
	byte[] saslMechanismsSelectByte = new byte[]{0,0,0,0,
			 0,0,0,4,
			 0,0,0,26,
			 0,0,0,4,
	      // -- content --
			 5,80,76,65,73,78,
			 80,87,78,68};
	
	byte[] faultySaslMechanismsSelectByte = new byte[]{0,0,0,0,
			 0,0,0,4,
			 0,0,0,26,
			 0,0,0,4,
	      // -- content --
			 5,80,32,65,73,78,
			 80,87,78,68};
	
	byte[] saslAuthDataByte = new byte[]{0,0,0,0,
			 0,0,0,5,
			 0,0,0,20,
			 0,0,0,5,
	      // -- content --
			 80,87,78,68};
	
	byte[] saslResultByte = new byte[]{0,0,0,0,
			 0,0,0,6,
			 0,0,0,22,
			 0,0,0,6,
	      // -- content --
			 0,0,80,87,78,68};
	
	byte[] errorByte = new byte[]{0,0,0,0,
			 0,0,0,8,
			 0,0,0,44,
			 0,0,0,7,
	      // -- content --
			 0,0,0,0,
			 0,0,0,2,
	      // -- error content --
			 0,0,0,0,
			 0,0,0,2,
			 0,0,0,20,
			 0,0,0,2,
			 0,0,0,1
			 };

	byte[] tncBatchByte = new byte[]{0,0,0,0,
			 0,0,0,7,
			 0,0,0,56,
			 0,0,0,8,
		// -- content --
			 2,0,0,3,
			 0,0,0,40,
			 0,0,0,0,
			 0,0,0,3,
			 0,0,0,16,
			 0,0,0,1,
			 -128,0,0,0,
			 0,0,0,2,
			 0,0,0,16,
			 0,0,0,0};
	
	/**
	 * @return the versionRequestByte
	 */
	public byte[] getVersionRequestAsByte() {
		return this.versionRequestByte;
	}
	
	/**
	 * @return the faultyVersionRequestByte
	 */
	public byte[] getFaultyVersionRequestAsByte() {
		return this.faultyVersionRequestByte;
	}

	/**
	 * @return the versionResponseByte
	 */
	public byte[] getVersionResponseAsByte() {
		return this.versionResponseByte;
	}

	/**
	 * @return the saslMechanismsByte
	 */
	public byte[] getSaslMechanismsAsByte() {
		return this.saslMechanismsByte;
	}

	/**
	 * @return the saslMechanismsSelectByte
	 */
	public byte[] getSaslMechanismsSelectAsByte() {
		return this.saslMechanismsSelectByte;
	}
	
	/**
	 * @return the faultySaslMechanismsSelectByte
	 */
	public byte[] getFaultySaslMechanismsSelectAsByte() {
		return this.faultySaslMechanismsSelectByte;
	}

	/**
	 * @return the saslAuthDataByte
	 */
	public byte[] getSaslAuthDataAsByte() {
		return this.saslAuthDataByte;
	}

	/**
	 * @return the saslResultByte
	 */
	public byte[] getSaslResultAsByte() {
		return this.saslResultByte;
	}

	/**
	 * @return the errorByte
	 */
	public byte[] getErrorAsByte() {
		return this.errorByte;
	}

	/**
	 * @return the tncBatchByte
	 */
	public byte[] getTncBatchAsByte() {
		return this.tncBatchByte;
	}

	/**
	 * @return the versionRequestByte
	 * @throws ValidationException 
	 */
	public PtTlsMessage getVersionRequest() throws ValidationException {
		
		return PtTlsMessageFactoryIetf.createVersionRequest(1, (short)1, (short)1, (short)1);
		
	}

	/**
	 * @return the versionResponseByte
	 * @throws ValidationException 
	 */
	public PtTlsMessage getVersionResponse() throws ValidationException {
		return PtTlsMessageFactoryIetf.createVersionResponse(2, (short)1);
	}

	/**
	 * @return the saslMechanismsByte
	 * @throws ValidationException 
	 */
	public PtTlsMessage getSaslMechanisms() throws ValidationException {
		
		SaslMechanismEntry m = new SaslMechanismEntry("PLAIN");
		return PtTlsMessageFactoryIetf.createSaslMechanisms(3, m);
	}

	/**
	 * @return the saslMechanismsSelectByte
	 * @throws UnsupportedEncodingException 
	 * @throws ValidationException 
	 */
	public PtTlsMessage getSaslMechanismsSelect() throws ValidationException, UnsupportedEncodingException {
		SaslMechanismEntry m = new SaslMechanismEntry("PLAIN");
		return PtTlsMessageFactoryIetf.createSaslMechanismSelection(4, m, "PWND".getBytes("US-ASCII"));
	}

	/**
	 * @return the saslAuthDataByte
	 * @throws UnsupportedEncodingException 
	 * @throws ValidationException 
	 */
	public PtTlsMessage getSaslAuthData() throws ValidationException, UnsupportedEncodingException {
		return PtTlsMessageFactoryIetf.createSaslAuthenticationData(5, "PWND".getBytes("US-ASCII"));
	}

	/**
	 * @return the saslResultByte
	 * @throws UnsupportedEncodingException 
	 * @throws ValidationException 
	 */
	public PtTlsMessage getSaslResult() throws ValidationException, UnsupportedEncodingException {
		return  PtTlsMessageFactoryIetf.createSaslResult(6,PtTlsSaslResultEnum.SUCCESS,"PWND".getBytes("US-ASCII"));
	}

	/**
	 * @return the errorByte
	 * @throws ValidationException 
	 */
	public PtTlsMessage getError() throws ValidationException {
		byte[] messageCopy =  new byte[] {0,0,0,0,
				 0,0,0,2,
				 0,0,0,20,
				 0,0,0,2,
				 0,0,0,1};
		
		return PtTlsMessageFactoryIetf.createError(7, 0, 2, messageCopy);
	}

	/**
	 * @return the tncBatchByte
	 * @throws ValidationException 
	 */
	public PtTlsMessage getTncBatch() throws ValidationException {
		byte[] tnccsData = new byte[] { 
				 2,0,0,3,
				 0,0,0,40,
				 0,0,0,0,
				 0,0,0,3,
				 0,0,0,16,
				 0,0,0,1,
				 -128,0,0,0,
				 0,0,0,2,
				 0,0,0,16,
				 0,0,0,0
		};
		
		ByteBuffer buf = new DefaultByteBuffer(tnccsData.length);
		buf.write(tnccsData);
		
		return PtTlsMessageFactoryIetf.createPbBatch(8, buf);
	}
	
	
	
	
	
}
