package org.ietf.nea.pb.message.enums;


/**
 * Reference IETF RFC 5793 section 4.3:
 * ------------------------------------
 * Message Type   Definition
 * ------------   ----------
 * 0              PB-Experimental - reserved for experimental use
 * 1              PB-PA - contains a PA message
 * 2              PB-Assessment-Result - the overall assessment result
 *                computed by the Posture Broker Server
 * 3              PB-Access-Recommendation - includes Posture Broker
 *                Server access recommendation
 * 4              PB-Remediation-Parameters - includes Posture Broker
 *                Server remediation parameters
 * 5              PB-Error - error indicator
 * 6              PB-Language-Preference - sender's preferred
 *                language(s) for human-readable strings
 * 7              PB-Reason-String - string explaining reason for
 *                Posture Broker Server access recommendation
 */

public enum PbMessageTypeEnum{

    // IETF
    IETF_PB_EXPERIMENTAL            (0),
    IETF_PB_PA                      (1),
    IETF_PB_ASSESSMENT_RESULT       (2),
    IETF_PB_ACCESS_RECOMMENDATION   (3),
    IETF_PB_REMEDIATION_PARAMETERS  (4),
    IETF_PB_ERROR                   (5),
    IETF_PB_LANGUAGE_PREFERENCE     (6),
    IETF_PB_REASON_STRING           (7),

    // RESERVED
    IETF_PB_RESERVED               (0xffffffff);
    
    private long messageType;
    
    private PbMessageTypeEnum(long messageType){
        this.messageType = messageType;
    }

    public long messageType(){
        return this.messageType;
    }
    
}
