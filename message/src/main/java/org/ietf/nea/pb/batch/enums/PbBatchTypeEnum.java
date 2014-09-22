package org.ietf.nea.pb.batch.enums;

/**
 * Reference IETF RFC 5793 section 4.1:
 * ------------------------------------
 * Number   Name     Definition
 *    ------   ----     ----------
 *
 *    1        CDATA    The Posture Broker Client may send a batch with
 *                      this Batch Type to convey messages to the
 *                      Posture Broker Server.  A Posture Broker Server
 *                      MUST NOT send this Batch Type.  A CDATA batch
 *                      may be empty (contain no messages) if the
 *                      Posture Broker Client has nothing to send.
 *
 *    2        SDATA    The Posture Broker Server may send a batch with
 *                      this Batch Type to convey messages to the
 *                      Posture Broker Client.  A Posture Broker Client
 *                      MUST NOT send this Batch Type.  An SDATA batch
 *                      may be empty (contain no messages) if the
 *                      Posture Broker Server has nothing to send.
 *                      
 *    3        RESULT   The Posture Broker Server may send a batch with
 *                      this Batch Type to indicate that it has
 *                      completed its evaluation.  The batch MUST
 *                      include a PB-Assessment-Result message and MAY
 *                      include a PB-Access-Recommendation message.
 *
 *    4        CRETRY   The Posture Broker Client may send a batch with
 *                      this Batch Type to indicate that it wishes to
 *                      restart an exchange.  A Posture Broker Server
 *                      MUST NOT send this Batch Type.  A CRETRY batch
 *                      may be empty (contain no messages) if the
 *                      Posture Broker Client has nothing else to send.
 *                      
 *    5        SRETRY  The Posture Broker Server may send a batch with
 *                     this Batch Type to indicate that it wishes to
 *                     restart the exchange.  A Posture Broker Client
 *                     MUST NOT send this Batch Type.  A SRETRY batch
 *                     may be empty (contain no messages) if the
 *                     Posture Broker Server has nothing else to send.
 *                     
 *    6        CLOSE   The Posture Broker Server or Posture Broker
 *                     Client may send a batch with this Batch Type to
 *                     indicate that it is about to terminate the
 *                     underlying PT connection.  A CLOSE batch may be
 *                     empty (contain no messages) if there is nothing
 *                     to send.  However, if the termination is due to a
 *                     fatal error, then the CLOSE batch MUST contain a
 *                     PB-Error message.
 */

public enum PbBatchTypeEnum {
    CDATA  ((byte)1),
    SDATA  ((byte)2),
    RESULT ((byte)3),
    CRETRY ((byte)4),
    SRETRY ((byte)5),
    CLOSE  ((byte)6),
    UNKNOWN ((byte)0);
    
    private byte number;
    
    private PbBatchTypeEnum(byte number){
        this.number = number;
    }
    
    public byte number(){
        return this.number;
    }
    
}
