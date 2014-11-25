package de.hsbremen.tc.tnc.adapter.im;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;
import org.trustedcomputinggroup.tnc.ifimv.IMVLong;
import org.trustedcomputinggroup.tnc.ifimv.IMVTNCSFirst;
import org.trustedcomputinggroup.tnc.ifimv.TNCException;

import de.hsbremen.tc.tnc.connection.ImConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public class ImvAdapterIetf implements ImvAdapter{

	private static final Logger LOGGER = LoggerFactory.getLogger(ImvAdapterIetf.class);
	private IMV imv;
	private final long primaryId;
//	private final ImParameter parameter;

	public ImvAdapterIetf(IMV imv, long primaryId /*, ImParameter parameter*/) {
		this.imv = imv;
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
	 * @see de.hsbremen.tc.tnc.adapter.im.ImAdapter#notifyConnectionChange(org.trustedcomputinggroup.tnc.ifimv.IMVConnection, de.hsbremen.tc.tnc.connection.ImConnectionState)
	 */
	@Override
	public void notifyConnectionChange(IMVConnection connection, ImConnectionState state) throws TncException, TerminatedException{
		try {
			this.imv.notifyConnectionChange(connection, state.state());
		} catch (TNCException e) {
			throw new TncException(e);
		}catch(NullPointerException e){
			throw new TerminatedException();
		}
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.adapter.im.ImcAdapter#beginHandshake(org.trustedcomputinggroup.tnc.ifimv.IMVConnection)
	 */
	@Override
	public void beginHandshake (IMVConnection connection) throws TncException, TerminatedException{
		try {
			if(this.imv instanceof IMVTNCSFirst){
				((IMVTNCSFirst)this.imv).beginHandshake(connection);
			}else{
				throw new UnsupportedOperationException("The underlying IMV is not of type " + IMVTNCSFirst.class.getCanonicalName() + ".");
			}
		} catch (TNCException e) {
			throw new TncException(e);
		}catch(NullPointerException e){
			throw new TerminatedException();
		}
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.adapter.im.ImAdapter#handleMessage(org.trustedcomputinggroup.tnc.ifimv.IMVConnection, de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue)
	 */
	@Override
	public void handleMessage (IMVConnection connection, TnccsMessageValue message) throws TncException, TerminatedException{
		try{
			this.dispatchMessageToImc(connection, message);
		}catch(NullPointerException e){
			throw new TerminatedException();
		}
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.adapter.im.ImAdapter#batchEnding(org.trustedcomputinggroup.tnc.ifimv.IMVConnection)
	 */
	@Override
	public void batchEnding (IMVConnection connection) throws TncException, TerminatedException{

		try{
			this.imv.batchEnding(connection);
		}catch(TNCException e){
			throw new TncException(e);
		}catch(NullPointerException e){
			throw new TerminatedException();
		}
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.adapter.im.ImAdapter#terminate()
	 */
	@Override
	public void terminate() throws TerminatedException{

		try {
			this.imv.terminate();
		} catch (TNCException e) {
			LOGGER.warn("An error occured, while terminating IMC/V " + this.imv + ". IMC/V will be removed anyway.",e);
		}catch(NullPointerException e){
			throw new TerminatedException();
		}
		this.imv = null;
	}
	
	@Override
	public void solicitRecommendation(IMVConnection connection)
			throws TncException, TerminatedException {

		try{
			this.imv.solicitRecommendation(connection);
		}catch(TNCException e){
			throw new TncException(e);
		}catch(NullPointerException e){
			throw new TerminatedException();
		}
		
	}
	
	private void dispatchMessageToImc(IMVConnection connection, TnccsMessageValue value) throws TncException{
		
		if(value instanceof PbMessageValueIm){
			PbMessageValueIm pbValue = (PbMessageValueIm) value;
			
			if(this.imv instanceof IMVLong){
				pbValue.getImFlags();
				
				byte bFlags = 0;
				for (PbMessageImFlagsEnum pbMessageImFlagsEnum : pbValue.getImFlags()) {
					bFlags |= pbMessageImFlagsEnum.bit();
				}
				try{
					((IMVLong) this.imv).receiveMessageLong(connection, bFlags, pbValue.getSubVendorId(), pbValue.getSubType(), pbValue.getMessage(), pbValue.getValidatorId(), pbValue.getCollectorId());
				}catch(TNCException e){
					throw new TncException(e);
				}
			}else{
				long msgType = (long)(pbValue.getSubVendorId() << 8) | (pbValue.getSubType() & 0xFF);
				try{
					this.imv.receiveMessage(connection,msgType,pbValue.getMessage());
				}catch(TNCException e){
					throw new TncException(e);
				}
			}
		} else {
			throw new TncException("Message values of type other than " + PbMessageValueIm.class.getCanonicalName() + " are not supported.", TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER, PbMessageValueIm.class.getCanonicalName());
		}
	}
}
