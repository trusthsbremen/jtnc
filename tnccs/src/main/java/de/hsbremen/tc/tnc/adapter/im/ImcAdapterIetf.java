package de.hsbremen.tc.tnc.adapter.im;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.IMCLong;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.connection.ImConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public class ImcAdapterIetf implements ImcAdapter{

	private static final Logger LOGGER = LoggerFactory.getLogger(ImcAdapterIetf.class);
	private IMC imc;
	private final long primaryId;
//	private final ImParameter parameter;

	public ImcAdapterIetf(IMC imc, long primaryId /*, ImParameter parameter*/) {
		this.imc = imc;
		this.primaryId = primaryId;
		/* this.parameter = parameter; */
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.adapter.im.ImAdapter#getPrimaryId()
	 */
	@Override
	public long getPrimaryId() {
		return this.primaryId;
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.adapter.im.ImAdapter#notifyConnectionChange(org.trustedcomputinggroup.tnc.ifimc.IMCConnection, de.hsbremen.tc.tnc.connection.ImConnectionState)
	 */
	@Override
	public void notifyConnectionChange(IMCConnection connection, ImConnectionState state) throws TncException, TerminatedException{
		try {
			this.imc.notifyConnectionChange(connection, state.state());
		} catch (TNCException e) {
			throw new TncException(e);
		}catch(NullPointerException e){
			throw new TerminatedException();
		}
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.adapter.im.ImcAdapter#beginHandshake(org.trustedcomputinggroup.tnc.ifimc.IMCConnection)
	 */
	@Override
	public void beginHandshake (IMCConnection connection) throws TncException, TerminatedException{
		try {
			this.imc.beginHandshake(connection);
		} catch (TNCException e) {
			throw new TncException(e);
		}catch(NullPointerException e){
			throw new TerminatedException();
		}
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.adapter.im.ImAdapter#handleMessage(org.trustedcomputinggroup.tnc.ifimc.IMCConnection, de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue)
	 */
	@Override
	public void handleMessage (IMCConnection connection, TnccsMessageValue message) throws TncException, TerminatedException{
		try{
			this.dispatchMessageToImc(connection, message);
		}catch(NullPointerException e){
			throw new TerminatedException();
		}
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.adapter.im.ImAdapter#batchEnding(org.trustedcomputinggroup.tnc.ifimc.IMCConnection)
	 */
	@Override
	public void batchEnding (IMCConnection connection) throws TncException, TerminatedException{
		
		try{
			this.imc.batchEnding(connection);
		}catch(TNCException e){
			throw new TncException(e);
		}catch(NullPointerException e){
			throw new TerminatedException();
		}
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.adapter.im.ImcAdapter#getAttribute(de.hsbremen.tc.tnc.attribute.TncAttributeType)
	 */
	@Override
	public Object getAttribute(TncAttributeType type) throws TncException {

		if(this.imc != null && this.imc instanceof AttributeSupport){
			try{
				return ((AttributeSupport) this.imc).getAttribute(type.id());
			}catch(TNCException e){
				throw new TncException(e);
			}catch(NullPointerException e){
				throw new UnsupportedOperationException(e.getMessage());
			}
		}else{
			throw new UnsupportedOperationException("The underlying IMC is not of type " + AttributeSupport.class.getCanonicalName() + ".");
		}
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.attribute.Attributed#setAttribute(de.hsbremen.tc.tnc.attribute.TncAttributeType, java.lang.Object)
	 */
	@Override
	public void setAttribute(TncAttributeType type, Object value)
			throws TncException {
		
		
		if(this.imc != null && this.imc instanceof AttributeSupport){
			try{
				((AttributeSupport) this.imc).setAttribute(type.id(), value);
			}catch(TNCException e){
				throw new TncException(e);
			}catch(NullPointerException e){
				throw new UnsupportedOperationException(e.getMessage());
			}
		}else{
			throw new UnsupportedOperationException("The underlying IMC is not of type " + AttributeSupport.class.getCanonicalName() + ".");
		}
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.adapter.im.ImAdapter#terminate()
	 */
	@Override
	public void terminate() throws TerminatedException{
		
		try {
			this.imc.terminate();
		} catch (TNCException e) {
			LOGGER.warn("An error occured, while terminating IMC/V " + this.imc + ". IMC/V will be removed anyway.",e);
		}catch(NullPointerException e){
			throw new TerminatedException();
		}
		
		this.imc = null;
	}

	private void dispatchMessageToImc(IMCConnection connection, TnccsMessageValue value) throws TncException{
		
		if(value instanceof PbMessageValueIm){
			PbMessageValueIm pbValue = (PbMessageValueIm) value;
			
			if(this.imc instanceof IMCLong){
				pbValue.getImFlags();
				
				byte bFlags = 0;
				for (PbMessageImFlagsEnum pbMessageImFlagsEnum : pbValue.getImFlags()) {
					bFlags |= pbMessageImFlagsEnum.bit();
				}
				try{
					((IMCLong) this.imc).receiveMessageLong(connection, bFlags, pbValue.getSubVendorId(), pbValue.getSubType(), pbValue.getMessage(), pbValue.getValidatorId(), pbValue.getCollectorId());
				}catch(TNCException e){
					throw new TncException(e);
				}
			}else{
				long msgType = (long)(pbValue.getSubVendorId() << 8) | (pbValue.getSubType() & 0xFF);
				try{
					this.imc.receiveMessage(connection,msgType,pbValue.getMessage());
				}catch(TNCException e){
					throw new TncException(e);
				}
			}
		} // else do nothing, because is not deliverable.
	}
}
