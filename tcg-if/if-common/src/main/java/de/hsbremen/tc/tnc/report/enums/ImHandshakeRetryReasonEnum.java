package de.hsbremen.tc.tnc.report.enums;

import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;

public enum ImHandshakeRetryReasonEnum {
	
	/* IMC */
	TNC_RETRY_REASON_IMC_REMEDIATION_COMPLETE (IMCConnection.TNC_RETRY_REASON_IMC_REMEDIATION_COMPLETE), // only on specific connection
	
	TNC_RETRY_REASON_IMC_SERIOUS_EVENT (IMCConnection.TNC_RETRY_REASON_IMC_SERIOUS_EVENT), // on all connections
	
	TNC_RETRY_REASON_IMC_INFORMATIONAL_EVENT (IMCConnection.TNC_RETRY_REASON_IMC_INFORMATIONAL_EVENT), // on all connections
	
	TNC_RETRY_REASON_IMC_PERIODIC (IMCConnection.TNC_RETRY_REASON_IMC_PERIODIC), // on all connections
	
	/* IMV */
	TNC_RETRY_REASON_IMV_IMPORTANT_POLICY_CHANGE(IMVConnection.TNC_RETRY_REASON_IMV_IMPORTANT_POLICY_CHANGE), // on all connections
	
	TNC_RETRY_REASON_IMV_MINOR_POLICY_CHANGE(IMVConnection.TNC_RETRY_REASON_IMV_MINOR_POLICY_CHANGE), // on all connections
	
	TNC_RETRY_REASON_IMV_SERIOUS_EVENT(IMVConnection.TNC_RETRY_REASON_IMV_SERIOUS_EVENT), // on all connections
	
	TNC_RETRY_REASON_IMV_MINOR_EVENT(IMVConnection.TNC_RETRY_REASON_IMV_MINOR_EVENT), // on all connections
	
	TNC_RETRY_REASON_IMV_PERIODIC(IMVConnection.TNC_RETRY_REASON_IMV_PERIODIC); // on all connections
	
	private final long code;
	
	private ImHandshakeRetryReasonEnum(long code){
		this.code = code;
	}
	
	public long code() {
		return this.code;
	}

	public static ImHandshakeRetryReasonEnum fromCode(long code){
		/* IMC */
		if( code == TNC_RETRY_REASON_IMC_REMEDIATION_COMPLETE.code){
			return TNC_RETRY_REASON_IMC_REMEDIATION_COMPLETE;
		}
		
		if( code == TNC_RETRY_REASON_IMC_SERIOUS_EVENT.code){
			return TNC_RETRY_REASON_IMC_SERIOUS_EVENT;
		}
		
		if( code == TNC_RETRY_REASON_IMC_INFORMATIONAL_EVENT.code){
			return TNC_RETRY_REASON_IMC_INFORMATIONAL_EVENT;
		}
		
		if( code == TNC_RETRY_REASON_IMC_PERIODIC.code){
			return TNC_RETRY_REASON_IMC_PERIODIC;
		}
		
		/* IMV */
		if( code == TNC_RETRY_REASON_IMV_IMPORTANT_POLICY_CHANGE.code){
			return TNC_RETRY_REASON_IMV_IMPORTANT_POLICY_CHANGE;
		}
		
		if( code == TNC_RETRY_REASON_IMV_MINOR_POLICY_CHANGE.code){
			return TNC_RETRY_REASON_IMV_MINOR_POLICY_CHANGE;
		}
		
		if( code == TNC_RETRY_REASON_IMV_SERIOUS_EVENT.code){
			return TNC_RETRY_REASON_IMV_SERIOUS_EVENT;
		}
		
		if( code == TNC_RETRY_REASON_IMV_MINOR_EVENT.code){
			return TNC_RETRY_REASON_IMV_MINOR_EVENT;
		}
		
		if( code == TNC_RETRY_REASON_IMV_PERIODIC.code){
			return TNC_RETRY_REASON_IMV_PERIODIC;
		}
		
		return null;
	}
}
