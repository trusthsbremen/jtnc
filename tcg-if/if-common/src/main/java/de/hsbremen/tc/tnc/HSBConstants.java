package de.hsbremen.tc.tnc;

public class HSBConstants {
	private HSBConstants(){
		throw new AssertionError();
	}
	
	public static final long HSB_PEN_VENDORID = 9649L;
	public static final long HSB_CONNECTION_STATE_UNKNOWN = -1L;
	public static final long HSB_IM_ID_UNKNOWN = -1L;
	public static final String HSB_DEFAULT_LANGUAGE = "Accept-Language: en";
	public static final long TCG_IM_MAX_MESSAGE_SIZE_UNKNOWN = 0;
	public static final long TCG_IM_MAX_MESSAGE_SIZE_UNLIMITED = 0xFFFFFFFF;
}
