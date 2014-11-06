package de.hsbremen.tc.tnc.im.evaluate.enums;

public enum ImTypeEnum {

	// IETF
    IETF_PA_TESTING            	(0),
    IETF_PA_OPERATING_SYSTEM    (1),
    IETF_PA_ANTI_VIRUS       	(2),
    IETF_PA_ANTI_SPYWARE   		(3),
    IETF_PA_ANTI_MALWARE  		(4),
    IETF_PA_FIREWALL            (5),
    IETF_PA_IDPS     			(6),
    IETF_PA_VPN          		(7),
    IETF_PA_NEA_CLIENT 			(8),

    // RESERVED (not specified in RFC)
    IETF_PA_RESERVED            (0xffffffff);
    
    private long type;
    
    private ImTypeEnum(long type){
        this.type = type;
    }

    public long type(){
        return this.type;
    }
}
