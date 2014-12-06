package de.hsbremen.tc.tnc.tnccs.session.connection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.PbBatchFactoryIetf;
import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.ietf.nea.pb.serialize.reader.PbReaderFactory;
import org.ietf.nea.pb.serialize.writer.PbWriterFactory;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.ComprehensibleException;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.enums.TcgTProtocolEnum;
import de.hsbremen.tc.tnc.message.t.enums.TcgTVersionEnum;
import de.hsbremen.tc.tnc.message.tnccs.enums.TcgTnccsProtocolEnum;
import de.hsbremen.tc.tnc.message.tnccs.enums.TcgTnccsVersionEnum;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.tnccs.AbstractDummy;
import de.hsbremen.tc.tnc.transport.connection.TransportAddress;
import de.hsbremen.tc.tnc.transport.connection.TransportAttributes;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class Dummy extends AbstractDummy{

	public static TransportConnection getSelfInitiatedTransportConnection() {
		return new TransportConnection() {
			
			private TransportAttributes attributes;
			private TransportAddress address;
			private ByteArrayInputStream in;
			private ByteArrayOutputStream out;
			private boolean open;
			@Override
			public void open() throws ConnectionException {
				 in = new ByteArrayInputStream(Dummy.getBatchWithImMessageAsByte());
				 out = new ByteArrayOutputStream();
				 open = true;
				 address = Dummy.getTransportAddress();
				 attributes = Dummy.getTransportAttributes();
			}
			
			@Override
			public boolean isSelfInititated() {
				System.out.println("isSelfInitiated() called. TRUE");
				return true;
			}
			
			@Override
			public boolean isOpen() {
				System.out.println("isOpen() called. " + (open && in != null && out != null));
				return (open && in != null && out != null);
			}
			
			@Override
			public OutputStream getOutputStream() {
				System.out.println("getOutputStream() called.");
				return out;
			}
			
			@Override
			public InputStream getInputStream() {
				System.out.println("getInputStream() called.");
				return in;
			}
			
			@Override
			public TransportAddress getId() {
				System.out.println("getId() called. " + this.address.toString());
				return this.address;
			}
			
			@Override
			public Attributed getAttributes() {
				System.out.println("getAttributes() called.");
				return this.attributes;
			}
			
			@Override
			public void close() {
				try{
					in.close();
					out.close();
				}catch(IOException e){
					System.out.println("Exception occured while shutting down the streams.");
				}finally{
					this.open = false;
				}
				
			}
		};
	}

	protected static TransportAttributes getTransportAttributes() {
		return new TransportAttributes() {
			
			private String tProtocol = TcgTProtocolEnum.TLS.value();
			private String tVersion = TcgTVersionEnum.V1.value();
			private Long maxMessageSize = new Long(512);
			private Long maxRoundtrips = new Long(HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNKNOWN);
			
			@Override
			public Object getAttribute(TncAttributeType type) throws TncException {
				System.out.println("getAttribute() called with type " + type.toString() +".");
				if(type.equals(TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_IFT_PROTOCOL)){
					return this.tProtocol;
				}
				
				if(type.equals(TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_IFT_VERSION)){
					return this.tVersion;
				}
				
				if(type.equals(TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_MAX_MESSAGE_SIZE)){
					return this.maxMessageSize;
				}
				
				if(type.equals(TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_MAX_ROUND_TRIPS)){
					return this.maxRoundtrips;
				}
				
				throw new TncException("The attribute with ID " + type.id() + " is unknown.", TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
			}

			@Override
			public void setAttribute(TncAttributeType type, Object value)
					throws TncException {
				System.out.println("setAttribute() called with type " + type.toString() +" and value "+ value.toString()+".");
				throw new UnsupportedOperationException("Operation not supported because there is no attribute to set.");
			}
			
			
		};
	}

	protected static TransportAddress getTransportAddress() {
		return new TransportAddress() {
			
			private int i = new Random().nextInt(100);

			/* (non-Javadoc)
			 * @see java.lang.Object#toString()
			 */
			@Override
			public String toString() {
				return super.toString() + "[id: "+i+"]";
			}
			
			
		};
	}

	protected static TnccsInputChannelListener getInputChannelListener() {
		
		return new Dummy.TestTnccsInputChannelListener();
	}
	
	protected static class TestTnccsInputChannelListener implements TnccsInputChannelListener{
		
		public TnccsBatchContainer bc;
		public ComprehensibleException e;

		/**
		 * @return the bc
		 */
		public TnccsBatchContainer getBatch() {
			return this.bc;
		}

		/**
		 * @return the e
		 */
		public ComprehensibleException getException() {
			return this.e;
		}

		@Override
		public void receive(TnccsBatchContainer bc) {
			System.out.println("Batch container received: " + bc.toString() );
			this.bc = bc;
		}
		
		@Override
		public void handle(ComprehensibleException e) {
			System.out.println("Exception received: " + e.getMessage() );
			this.e = e;
			
		}
	}
	
	protected static PbBatch getBatchWithImMessage() throws ValidationException{
		
		
		PbMessageImFlagsEnum[] imFlags = new PbMessageImFlagsEnum[0];
		long subVendorId = IETFConstants.IETF_PEN_VENDORID;
		long subType = 1L;
		short collectorId = 1;
		short validatorId = (short)0xFFFF;
		byte[] message = "PWND".getBytes(Charset.forName("US-ASCII"));
		List<TnccsMessage> messages = new ArrayList<>();
		messages.add(PbMessageFactoryIetf.createIm(imFlags, subVendorId, subType, collectorId, validatorId, message));
		return PbBatchFactoryIetf.createClientData(messages);
	}
	
	protected static byte[] getBatchWithImMessageAsByte(){
		return new byte[]{2, -128, 0, 1, 0, 0, 0, 36, -128, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 28, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, -1, -1, 80, 87, 78, 68};
	}
	
	protected static byte[] getBatchWithImMessageAsByteShortenedFaulty(){
		return new byte[]{2, -128, 0, 1, 0, 0, 0, 36, -128, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 28, 0, 0, 0, 0, 0, 0, 0, 1, 0};
	}

	protected static TnccsChannelFactory getChannelFactory() {
		DefaultTnccsChannelFactory channelFactory = new DefaultTnccsChannelFactory(TcgTnccsProtocolEnum.TNCCS.value(),
				TcgTnccsVersionEnum.V2.value(),
				PbReaderFactory.createExperimentalDefault(),
				PbWriterFactory.createExperimentalDefault());
		
		return channelFactory;
	}

}
