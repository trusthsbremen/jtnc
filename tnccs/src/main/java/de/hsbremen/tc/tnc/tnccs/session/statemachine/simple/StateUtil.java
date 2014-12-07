package de.hsbremen.tc.tnc.tnccs.session.statemachine.simple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ietf.nea.pb.batch.PbBatchFactoryIetf;
import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;

abstract class StateUtil {

	private StateUtil(){
		throw new IllegalAccessError("Class is abstract and static.");
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StateUtil.class);

	protected static TnccsBatch createCloseBatch(boolean server, TnccsMessage...error) {
		
		List<TnccsMessage> messages = (error != null) ? Arrays.asList(error) : new ArrayList<TnccsMessage>();
		TnccsBatch b = null;
		try {
			if(server){
				b = PbBatchFactoryIetf.createServerClose(messages);
			}else{
				b = PbBatchFactoryIetf.createClientClose(messages);
			}
		} catch (ValidationException e) {
			LOGGER.error("Error while creating a close batch, transitioning to end state, without close." ); 
		}
		
		return b;
	}
	
	protected static TnccsMessage createUnexpectedStateError() {
		try {
			return PbMessageFactoryIetf.createErrorSimple(new PbMessageErrorFlagsEnum[]{PbMessageErrorFlagsEnum.FATAL}, PbMessageErrorCodeEnum.IETF_UNEXPECTED_BATCH_TYPE);
		} catch (ValidationException e) {
			return null;
		}
	}

	protected static TnccsMessage createUnsupportedVersionError(short badVersion,
			short minVersion, short maxVersion) {
				try {
					return PbMessageFactoryIetf.createErrorVersion(new PbMessageErrorFlagsEnum[]{PbMessageErrorFlagsEnum.FATAL}, PbMessageErrorCodeEnum.IETF_UNSUPPORTED_VERSION,  badVersion, maxVersion, minVersion);
				} catch (ValidationException e) {
					return null;
				}
			}

	protected static TnccsMessage createLocalError() {
		try {
			return PbMessageFactoryIetf.createErrorSimple(new PbMessageErrorFlagsEnum[]{PbMessageErrorFlagsEnum.FATAL}, PbMessageErrorCodeEnum.IETF_LOCAL);
		} catch (ValidationException e) {
			return null;
		}
	}
}