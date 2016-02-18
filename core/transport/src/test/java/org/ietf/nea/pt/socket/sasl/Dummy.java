package org.ietf.nea.pt.socket.sasl;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.AuthorizeCallback;

import de.hsbremen.tc.tnc.transport.AbstractDummy;

public class Dummy extends AbstractDummy {

    public static final String[] getJoeCredentials(){
        return new String[]{"Joe", "Test1234"};
    }
    
    public static final String[] getMollyCredentials(){
        return new String[]{"Molly", "1234Test"};
    }
    
    public static final byte[] getResponse(String[] credentials){
        
        
        final byte SEP = '\000'; // NUL
        String authorizationId = null;
        
        byte[] authzid = null;
        byte[] authcid = new byte[0];
        byte[] password = new byte[0];
        
        try {
            authzid = (authorizationId != null) ? authorizationId
                    .getBytes("UTF8") : null;

            authcid = (credentials[0] != null) ? credentials[0].getBytes("UTF8") : null;
            
            password = (credentials[1] != null) ? credentials[1].getBytes("UTF8") : null;
            
        } catch (java.io.UnsupportedEncodingException e) {
            
            throw new IllegalStateException("PLAIN no UTF-8 encoding", e);
        }

        
        byte[] response = new byte[((password == null) ? 0 : password.length + 1) + ((authcid == null) ? 0 : authcid.length + 1)
                + (authzid == null ? 0 : authzid.length)];

        int pos = 0;
        if (authzid != null) {
            System.arraycopy(authzid, 0, response, 0, authzid.length);
            pos = authzid.length;
        }
        
        if(authcid != null){
            response[pos++] = SEP;
            System.arraycopy(authcid, 0, response, pos, authcid.length);
            pos += authcid.length;
        }
        
        if(password != null){
            response[pos++] = SEP;
            System.arraycopy(password, 0, response, pos, password.length);
        }

        return response;
    }
    
    public static final CallbackHandler getPlainCallBackHandler(final String[] credentials, final boolean doubleNameCb){
        return new CallbackHandler() {

            private boolean authCallBackDone = (doubleNameCb) ? false : true;
            private final String[] creds = credentials;
            
            @Override
            public void handle(Callback[] callbacks) throws IOException,
                    UnsupportedCallbackException {
                for (Callback callback : callbacks) {
                    System.out.print(Dummy.getTestDescriptionHead(
                            this.getClass().getSimpleName(),"Callback received: " 
                                    + callback.toString()));
                    
                    if (callback instanceof NameCallback) {
                        if(!authCallBackDone){
                            System.out.println(Dummy.getTestDescriptionHead(
                                    this.getClass().getSimpleName(),((NameCallback) callback).getPrompt()));
                            ((NameCallback) callback).setName(null);
                            authCallBackDone = true;
                        }else{
                            System.out.println(Dummy.getTestDescriptionHead(
                                    this.getClass().getSimpleName(),((NameCallback) callback).getPrompt()));
                            ((NameCallback) callback).setName(creds[0]);
                        }

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
