package org.ietf.nea.pt.serialize;

import java.io.UnsupportedEncodingException;

import org.ietf.nea.pt.message.PtTlsMessage;
import org.ietf.nea.pt.serialize.writer.bytebuffer.PtTlsWriterFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.message.util.DefaultByteBuffer;

public class WriterTest {

	TestData transport;
	TransportWriter<TransportMessage> bs;
	
	@Before
	public void setUp(){
		transport = new TestData();
		bs = PtTlsWriterFactory.createProductionDefault();
	}
	
	@Test
	public void serializeTransportMessageWithVersionRequest() throws SerializationException, ValidationException{
		
		PtTlsMessage m = transport.getVersionRequest();
		ByteBuffer out = new DefaultByteBuffer(m.getHeader().getLength());
		
		bs.write(m, out);
		
		byte[] data  = transport.getVersionRequestAsByte();
		ByteBuffer b = new DefaultByteBuffer(data.length);
		
		b.write(data);
		
		Assert.assertTrue(out.equals(b));
		
		
	}
	
	@Test
	public void serializeTransportMessageWithVersionResponse() throws SerializationException, ValidationException{
		
		PtTlsMessage m = transport.getVersionResponse();
		ByteBuffer out = new DefaultByteBuffer(m.getHeader().getLength());
		
		bs.write(m, out);
		
		byte[] data  = transport.getVersionResponseAsByte();
		ByteBuffer b = new DefaultByteBuffer(data.length);
		
		b.write(data);
		
		Assert.assertTrue(out.equals(b));
		
	}
	
	@Test
	public void serializeTransportMessageWithSaslMechanisms() throws SerializationException, ValidationException{
		
		PtTlsMessage m = transport.getSaslMechanisms();
		ByteBuffer out = new DefaultByteBuffer(m.getHeader().getLength());
		
		bs.write(m, out);
		
		byte[] data  = transport.getSaslMechanismsAsByte();
		ByteBuffer b = new DefaultByteBuffer(data.length);
		
		b.write(data);
		
		Assert.assertTrue(out.equals(b));
		
	}
	
	@Test
	public void serializeTransportMessageWithSaslMechanismSelect() throws SerializationException, ValidationException, UnsupportedEncodingException{
		
		PtTlsMessage m = transport.getSaslMechanismsSelect();
		ByteBuffer out = new DefaultByteBuffer(m.getHeader().getLength());
		
		bs.write(m, out);
		
		byte[] data  = transport.getSaslMechanismsSelectAsByte();
		ByteBuffer b = new DefaultByteBuffer(data.length);
		
		b.write(data);
		
		Assert.assertTrue(out.equals(b));
	}
	
	@Test
	public void serializeTransportMessageWithSaslAuthData() throws SerializationException, ValidationException, UnsupportedEncodingException{
		
		PtTlsMessage m = transport.getSaslAuthData();
		ByteBuffer out = new DefaultByteBuffer(m.getHeader().getLength());
		
		bs.write(m, out);
		
		byte[] data  = transport.getSaslAuthDataAsByte();
		ByteBuffer b = new DefaultByteBuffer(data.length);
		
		b.write(data);
		
	}
	
	@Test
	public void serializeTransportMessageWithSaslResult() throws SerializationException, ValidationException, UnsupportedEncodingException{
		
		PtTlsMessage m = transport.getSaslResult();
		ByteBuffer out = new DefaultByteBuffer(m.getHeader().getLength());
		
		bs.write(m, out);
		
		byte[] data  = transport.getSaslResultAsByte();
		ByteBuffer b = new DefaultByteBuffer(data.length);
		
		b.write(data);
		
		Assert.assertTrue(out.equals(b));
		
	}
	
	@Test
	public void serializeTransportMessageWithError() throws SerializationException, ValidationException{
		
		PtTlsMessage m = transport.getError();
		ByteBuffer out = new DefaultByteBuffer(m.getHeader().getLength());
		
		bs.write(m, out);
		
		byte[] data  = transport.getErrorAsByte();
		ByteBuffer b = new DefaultByteBuffer(data.length);
		
		b.write(data);
		
		Assert.assertTrue(out.equals(b));
		
	}
	
	@Test
	public void serializeTransportMessageWithTncBatch() throws SerializationException, ValidationException{
		
		PtTlsMessage m = transport.getTncBatch();
		ByteBuffer out = new DefaultByteBuffer(m.getHeader().getLength());
		
		bs.write(m, out);
		
		byte[] data  = transport.getTncBatchAsByte();
		ByteBuffer b = new DefaultByteBuffer(data.length);
		
		b.write(data);
		
		Assert.assertTrue(out.equals(b));
	
	}
}
