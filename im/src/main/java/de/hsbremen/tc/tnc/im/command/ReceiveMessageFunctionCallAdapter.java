package de.hsbremen.tc.tnc.im.command;

import java.util.Set;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.IMCLong;
import org.trustedcomputinggroup.tnc.ifimc.IMCSOH;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.command.FunctionCall;
import de.hsbremen.tc.tnc.im.connection.ImConnection;
import de.hsbremen.tc.tnc.im.container.ImContainer;

public class ReceiveMessageFunctionCallAdapter<T> implements FunctionCall{
	
    private final ImContainer<T> imContainer;
    private final ImConnection imConnection;
    private final PbMessageValueIm message;

    
    public ReceiveMessageFunctionCallAdapter(final ImContainer<T> im, final ImConnection imConnection, PbMessageValueIm message){
        this.imContainer = im;
        this.imConnection = imConnection;
        this.message = message;
    }
    
	@Override
	public void call() throws TNCException{
        T im = imContainer.getIm();
        if(im instanceof IMC && imConnection instanceof IMCConnection){
        	
        	IMCConnection c = (IMCConnection) imConnection;
        	
	        if (im instanceof IMCLong) {
	        	Set<PbMessageImFlagsEnum> flags = message.getImFlags();
	    		byte bFlags = 0;
	    		for (PbMessageImFlagsEnum pbMessageImFlagsEnum : flags) {
	    			bFlags |= pbMessageImFlagsEnum.bit();
	    		}
	            ((IMCLong) im).receiveMessageLong(c, bFlags, message.getSubVendorId(), message.getSubType(), message.getMessage(), message.getValidatorId(), message.getCollectorId());
	            		
	        } else if (im instanceof IMCSOH) {
	            // TODO implement SOH support.
	            throw new UnsupportedOperationException("SOH not support at the moment, will be added in the future.");
	            
	        } else {
	        	
	        	long vId = message.getSubVendorId();
	        	long tId = message.getSubType();
	        	// a bit blurry because it does not check if the real bit sizes would fit into a 32bit number.
	            if(!(vId > TNCConstants.TNC_VENDORID_ANY || tId > TNCConstants.TNC_SUBTYPE_ANY)){
	            	long messageType = ((vId & TNCConstants.TNC_VENDORID_ANY) << 2) | (tId & TNCConstants.TNC_SUBTYPE_ANY);
	            	((IMC) im).receiveMessage(c, messageType, message.getMessage());
	            }
	        }
        }
	}
}
