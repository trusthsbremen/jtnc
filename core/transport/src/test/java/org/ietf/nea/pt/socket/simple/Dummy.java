package org.ietf.nea.pt.socket.simple;

import java.io.IOException;
import java.util.Queue;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.AuthorizeCallback;

import org.ietf.nea.pt.message.PtTlsMessageFactoryIetf;
import org.ietf.nea.pt.message.PtTlsMessageHeader;
import org.ietf.nea.pt.socket.TransportMessenger;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.transport.AbstractDummy;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class Dummy extends AbstractDummy {
    
    public static TransportMessenger createDummyMessenger(final Queue<TransportMessage> src, final Queue<TransportMessage> dest){

        return new TransportMessenger() {
            
            private final Queue<TransportMessage> sBuffer = src;
            private final Queue<TransportMessage> dBuffer = dest;
            private int id = 0;


            @Override
            public long getIdentifier() {
                return id = ((id + 1) <= Integer.MAX_VALUE) ? (id + 1) : 0 ;
            }

            @Override
            public void writeToStream(TransportMessage message)
                    throws ConnectionException, SerializationException {
                System.out.println("Message send to Queue: " + message.toString());
                this.dBuffer.offer(message);
                
            }

            @Override
            public TransportMessage readFromStream() throws SerializationException,
                    ValidationException, ConnectionException {
               
               TransportMessage m =  this.sBuffer.poll();
               if(m !=  null){
                   System.out.println("Message received from Queue: " + m.toString());
               }
               return m;
            }

            @Override
            public TransportMessage createValidationErrorMessage(
                    ValidationException exception) throws ValidationException {
                if (exception.getReasons() != null
                        || exception.getReasons().size() >= 0) {

                    Object firstReason = exception.getReasons().get(0);
                    if (firstReason instanceof byte[]) {
                        return PtTlsMessageFactoryIetf.createError(
                                this.getIdentifier(), IETFConstants.IETF_PEN_VENDORID,
                                exception.getCause().getErrorCode(),
                                (byte[]) firstReason);
                    }

                    if (firstReason instanceof PtTlsMessageHeader) {
                        return PtTlsMessageFactoryIetf.createError(
                                this.getIdentifier(), IETFConstants.IETF_PEN_VENDORID,
                                exception.getCause().getErrorCode(),
                                (PtTlsMessageHeader) firstReason);
                    }

                }

                return PtTlsMessageFactoryIetf.createError(this.getIdentifier(),
                        IETFConstants.IETF_PEN_VENDORID,
                        exception.getCause().getErrorCode(),
                        new byte[0]);
            }
        };
    }
    
    public static final String[] getJoeCredentials(){
        return new String[]{"Joe", "Test1234"};
    }
    
    public static final String[] getMollyCredentials(){
        return new String[]{"Molly", "1234Test"};
    }
    
    public static final CallbackHandler getPlainCallBackHandler(final String[] credentials){
        return new CallbackHandler() {

            
            private final String[] creds = credentials;
            
            @Override
            public void handle(Callback[] callbacks) throws IOException,
                    UnsupportedCallbackException {
                for (Callback callback : callbacks) {
                    System.out.print(Dummy.getTestDescriptionHead(
                            this.getClass().getSimpleName(),"Callback received: " 
                                    + callback.toString()));
                    
                    if (callback instanceof NameCallback) {

                        System.out.println(Dummy.getTestDescriptionHead(this
                                .getClass().getSimpleName(),
                                ((NameCallback) callback).getPrompt()));
                        ((NameCallback) callback).setName(creds[0]);

                    }

                    if (callback instanceof PasswordCallback) {
                        System.out.println(Dummy.getTestDescriptionHead(
                                this.getClass().getSimpleName(),((PasswordCallback) callback)
                                .getPrompt()));
                        ((PasswordCallback) callback).setPassword(creds[1]
                                .toCharArray());
                    }
                    
                    if(callback instanceof AuthorizeCallback){
                        String authcid = ((AuthorizeCallback) callback)
                                .getAuthenticationID();
                        
                        String authzid = ((AuthorizeCallback) callback)
                                .getAuthorizationID();
                        
                        
                        System.out.println(Dummy.getTestDescriptionHead(
                                this.getClass().getSimpleName(),"Authentication ID: " + authcid ));
                        System.out.println(Dummy.getTestDescriptionHead(
                                this.getClass().getSimpleName(),"Authorization ID: " + authzid ));
                        
                        if(authcid.equals(authzid)){
                            ((AuthorizeCallback) callback)
                            .setAuthorized(true);
                            
                            System.out.println(Dummy.getTestDescriptionHead(
                                    this.getClass().getSimpleName(),"Authorized ID: "
                                            + ((AuthorizeCallback) callback)
                                                .getAuthorizedID() ));
                        }
                    }
                }

            }
        };
    }
}
