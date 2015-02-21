package org.ietf.nea.pt.message.enums;

/**
 * Reference IETF RFC 6876 section 3.6:
 * ------------------------------------
 * Message Type   Definition
 * ------------   ----------
 * 0              PT-Experimental - Reserved for experimental use.
 * 
 * 1              PT-Version Request - Version negotiation request 
 * 			      including the range of versions supported by the 
 * 				  sender.
 * 
 * 2              PT-Version Response - PT-TLS protocol version 
 * 				  selected by the responder.
 * 
 * 3              PT-SASL Mechanisms - Sent by the NEA Server to 
 * 				  indicate what SASL mechanisms it is willing to 
 * 				  use for authentication on this session.
 * 
 * 4              PT-SASL Mechanism Selection - Sent by the NEA 
 * 				  Client to select a SASL mechanism from the list
 * 				  offered by the NEA Server.
 * 
 * 5              PT-SASL Authentication Data - Opaque octets 
 * 				  exchanged between the NEA Client and NEA Server's
 * 				  SASL mechanisms to perform the client authentication.
 * 
 * 6              PT-SASL Result - Indicates the result code of 
 *  			  the SASL mechanism authentication.
 *  				
 * 7              PT-PB-TNC Batch - Contains a PB-TNC batch.
 * 
 * 8			  PT-Error - PT-TLS Error message.
 */

public enum PtTlsMessageTypeEnum {

	IETF_PT_TLS_EXERIMENTAL(0),
	IETF_PT_TLS_VERSION_REQUEST(1),
	IETF_PT_TLS_VERSION_RESPONSE(2),
	IETF_PT_TLS_SASL_MECHANISMS(3),
	IETF_PT_TLS_SASL_MECHANISM_SELECTION(4),
	IETF_PT_TLS_SASL_AUTHENTICATION_DATA(5),
	IETF_PT_TLS_SASL_RESULT(6),
	IETF_PT_TLS_PB_BATCH(7),
	IETF_PT_TLS_ERROR (8);
	
	private long messagType;
	
	private PtTlsMessageTypeEnum(long messageType){
		this.messagType = messageType;
	}
	
	public long messageType(){
		return this.messagType;
	}
	
}
