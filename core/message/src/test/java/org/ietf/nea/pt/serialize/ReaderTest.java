package org.ietf.nea.pt.serialize;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

import org.ietf.nea.pt.message.PtTlsMessage;
import org.ietf.nea.pt.message.enums.PtTlsMessageTypeEnum;
import org.ietf.nea.pt.serialize.reader.bytebuffer.PtTlsReaderFactory;
import org.ietf.nea.pt.value.PtTlsMessageValueError;
import org.ietf.nea.pt.value.PtTlsMessageValuePbBatch;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslAuthenticationData;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanismSelection;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanisms;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslResult;
import org.ietf.nea.pt.value.PtTlsMessageValueVersionRequest;
import org.ietf.nea.pt.value.PtTlsMessageValueVersionResponse;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;
import org.ietf.nea.pt.value.enums.PtTlsSaslResultEnum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.serialize.TransportMessageContainer;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.message.util.DefaultByteBuffer;
import de.hsbremen.tc.tnc.message.util.StreamedReadOnlyByteBuffer;

public class ReaderTest {

	TestData transport;
	TransportReader<TransportMessageContainer> bs;
	
	@Before
	public void setUp(){
		transport = new TestData();
		bs = PtTlsReaderFactory.createProductionDefault();
	}
	
	@Test
	public void deserializeTransportMessageWithVersionRequest() throws SerializationException, ValidationException{
		byte[] data  = transport.getVersionRequestAsByte();
		ByteBuffer b = new DefaultByteBuffer(data.length);
		
		b.write(data);
		
		TransportMessageContainer tc = bs.read(b, -1);
		
		PtTlsMessage m = (PtTlsMessage)tc.getResult();
		
		Assert.assertEquals(PtTlsMessageTypeEnum.IETF_PT_TLS_VERSION_REQUEST.id(), m.getHeader().getMessageType());
		Assert.assertEquals(1, ((PtTlsMessageValueVersionRequest)m.getValue()).getPreferredVersion());
		Assert.assertEquals(data.length, m.getHeader().getLength());
	}
	
	@Test
	public void deserializeTransportMessageWithVersionResponse() throws SerializationException, ValidationException{
		byte[] data  = transport.getVersionResponseAsByte();
		ByteBuffer b = new DefaultByteBuffer(data.length);
		
		b.write(data);
		
		TransportMessageContainer tc = bs.read(b, -1);
		
		PtTlsMessage m = (PtTlsMessage)tc.getResult();
		
		Assert.assertEquals(PtTlsMessageTypeEnum.IETF_PT_TLS_VERSION_RESPONSE.id(), m.getHeader().getMessageType());
		Assert.assertEquals(1, ((PtTlsMessageValueVersionResponse)m.getValue()).getSelectedVersion());
		Assert.assertEquals(data.length, m.getHeader().getLength());
	}
	
	@Test
	public void deserializeTransportMessageWithSaslMechanisms() throws SerializationException, ValidationException{
		byte[] data  = transport.getSaslMechanismsAsByte();
		ByteBuffer b = new DefaultByteBuffer(data.length);
		
		b.write(data);
		
		TransportMessageContainer tc = bs.read(b, -1);
		
		PtTlsMessage m = (PtTlsMessage)tc.getResult();
		
		Assert.assertEquals(PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_MECHANISMS.id(), m.getHeader().getMessageType());
		Assert.assertEquals("PLAIN", ((PtTlsMessageValueSaslMechanisms)m.getValue()).getMechanisms().get(0).getName());
		Assert.assertEquals(data.length, m.getHeader().getLength());
	}
	
	@Test
	public void deserializeTransportMessageWithSaslMechanismSelect() throws SerializationException, ValidationException{
		byte[] data  = transport.getSaslMechanismsSelectAsByte();
		ByteBuffer b = new DefaultByteBuffer(data.length);
		
		b.write(data);
		
		TransportMessageContainer tc = bs.read(b, -1);
		
		PtTlsMessage m = (PtTlsMessage)tc.getResult();
		
		Assert.assertEquals(PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_MECHANISM_SELECTION.id(), m.getHeader().getMessageType());
		Assert.assertEquals("PLAIN", ((PtTlsMessageValueSaslMechanismSelection)m.getValue()).getMechanism().getName());
		Assert.assertArrayEquals(Arrays.copyOfRange(data,data.length-4, data.length), ((PtTlsMessageValueSaslMechanismSelection)m.getValue()).getInitialSaslMsg());
		Assert.assertEquals(data.length, m.getHeader().getLength());
	}
	
	@Test
	public void deserializeTransportMessageWithSaslAuthData() throws SerializationException, ValidationException{
		byte[] data  = transport.getSaslAuthDataAsByte();
		ByteBuffer b = new DefaultByteBuffer(data.length);
		
		b.write(data);
		
		TransportMessageContainer tc = bs.read(b, -1);
		
		PtTlsMessage m = (PtTlsMessage)tc.getResult();
		
		Assert.assertEquals(PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_AUTHENTICATION_DATA.id(), m.getHeader().getMessageType());
		Assert.assertArrayEquals(Arrays.copyOfRange(data,data.length-4, data.length), ((PtTlsMessageValueSaslAuthenticationData)m.getValue()).getAuthenticationData());
		Assert.assertEquals(data.length, m.getHeader().getLength());
	}
	
	@Test
	public void deserializeTransportMessageWithSaslResult() throws SerializationException, ValidationException{
		byte[] data  = transport.getSaslResultAsByte();
		ByteBuffer b = new DefaultByteBuffer(data.length);
		
		b.write(data);
		
		TransportMessageContainer tc = bs.read(b, -1);
		
		PtTlsMessage m = (PtTlsMessage)tc.getResult();
		
		Assert.assertEquals(PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_RESULT.id(), m.getHeader().getMessageType());
		Assert.assertEquals(PtTlsSaslResultEnum.SUCCESS, ((PtTlsMessageValueSaslResult)m.getValue()).getResult());
		Assert.assertArrayEquals(Arrays.copyOfRange(data,data.length-4, data.length), ((PtTlsMessageValueSaslResult)m.getValue()).getResultData());
		Assert.assertEquals(data.length, m.getHeader().getLength());
	}
	
	@Test
	public void deserializeTransportMessageWithError() throws SerializationException, ValidationException{
		byte[] data  = transport.getErrorAsByte();
		ByteBuffer b = new DefaultByteBuffer(data.length);
		
		b.write(data);
		
		TransportMessageContainer tc = bs.read(b, -1);
		
		PtTlsMessage m = (PtTlsMessage)tc.getResult();
		
		Assert.assertEquals(PtTlsMessageTypeEnum.IETF_PT_TLS_ERROR.id(), m.getHeader().getMessageType());
		Assert.assertEquals(PtTlsMessageErrorCodeEnum.IETF_UNSUPPORTED_VERSION.code(), ((PtTlsMessageValueError)m.getValue()).getErrorCode());
		Assert.assertArrayEquals(transport.getVersionResponseAsByte(), ((PtTlsMessageValueError)m.getValue()).getPartialMessageCopy());
		Assert.assertEquals(data.length, m.getHeader().getLength());
	}
	
	@Test
	public void deserializeTransportMessageWithTncBatch() throws SerializationException, ValidationException{
		byte[] data  = transport.getTncBatchAsByte();
		ByteBuffer b = new DefaultByteBuffer(data.length);
		
		b.write(data);
	
		TransportMessageContainer tc = bs.read(b, -1);
		
		PtTlsMessage m = (PtTlsMessage)tc.getResult();
		
		Assert.assertEquals(PtTlsMessageTypeEnum.IETF_PT_TLS_PB_BATCH.id(), m.getHeader().getMessageType());
		
		byte[] tnccsData = Arrays.copyOfRange(data, 16, data.length);
		ByteBuffer c = new DefaultByteBuffer(tnccsData.length);
		c.write(tnccsData);
		Assert.assertTrue(c.equals(((PtTlsMessageValuePbBatch)m.getValue()).getTnccsData()));
		Assert.assertEquals(data.length, m.getHeader().getLength());
	}
	
	@Test(expected=ValidationException.class)
	public void validationErrorOffsetHeaderTest() throws SerializationException, ValidationException{
		
		try{
			byte[] data  = transport.getFaultyVersionRequestAsByte();
			ByteBuffer b = new DefaultByteBuffer(data.length);
			
			b.write(data);
		
			bs.read(b, -1);
		}catch(ValidationException e){
			Assert.assertEquals(4, e.getExceptionOffset());
			throw e;
		}
	}
	
	@Test(expected=ValidationException.class)
	public void validationErrorOffsetValueTest() throws SerializationException, ValidationException{
		
		try{
			byte[] data  = transport.getFaultySaslMechanismsSelectAsByte();
			ByteBuffer b = new DefaultByteBuffer(data.length);
			
			b.write(data);
		
			bs.read(b, -1);
		}catch(ValidationException e){
			Assert.assertEquals(16, e.getExceptionOffset());
			throw e;
		}
	}
	
	@Test
	public void deserializeTransportMessageWithErrorUsingStream() throws SerializationException, ValidationException{
		byte[] data  = transport.getErrorAsByte();
		InputStream in = new ByteArrayInputStream(data);
		ByteBuffer b = new StreamedReadOnlyByteBuffer(in);

		TransportMessageContainer tc = bs.read(b, -1);
		
		PtTlsMessage m = (PtTlsMessage)tc.getResult();
		
		Assert.assertEquals(PtTlsMessageTypeEnum.IETF_PT_TLS_ERROR.id(), m.getHeader().getMessageType());
		Assert.assertEquals(PtTlsMessageErrorCodeEnum.IETF_UNSUPPORTED_VERSION.code(), ((PtTlsMessageValueError)m.getValue()).getErrorCode());
		Assert.assertArrayEquals(transport.getVersionResponseAsByte(), ((PtTlsMessageValueError)m.getValue()).getPartialMessageCopy());
		Assert.assertEquals(data.length, m.getHeader().getLength());
	}
	
}
