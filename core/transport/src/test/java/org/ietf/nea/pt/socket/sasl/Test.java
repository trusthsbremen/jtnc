package org.ietf.nea.pt.socket.sasl;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.AuthorizeCallback;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;

public class Test {

    public static void main(String[] args) {
        String[] mechs = new String[] { "PLAIN" };
        String protocol = "nea-pt-tls";

        

        try {
            SaslClient sc;
            
            
            sc = new PlainClient(new Cbh(SaslClient.class.toString()));
//
//            sc = Sasl.createSaslClient(mechs, null, protocol, "localhost",
//                    null, new Cbh(SaslClient.class.toString()));

            
            
            SaslServer ss;
            
            ss = new PlainServer(new Cbh(SaslServer.class.toString()), null);

//            ss = new PlainServer(protocol, "localhost", 
//            //ss = Sasl.createSaslServer(mechs[0], protocol, "localhost", null,
//                    new Cbh(SaslServer.class.toString()));

            byte[] response = (sc.hasInitialResponse() ? sc
                    .evaluateChallenge(new byte[0]) : new byte[0]);
            response = (response == null) ? new byte[0] : response;

            String mechanism = sc.getMechanismName();
            
            while(!ss.isComplete()){
                byte[] challenge = ss.evaluateResponse(response);
    
                challenge = (challenge == null) ? new byte[0] : challenge;
    
                if(!sc.isComplete()){
                    response = sc.evaluateChallenge(challenge);
                    response = (response == null) ? new byte[0] : response;
                }
            }

        } catch (SaslException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

//    public static class CustomSaslProvider extends Provider {
//
//        private static final String info = "Sun SASL provider"
//                + "(implements client mechanisms for: "
//                + " server mechanisms for: PLAIN)";
//
//        public CustomSaslProvider() {
//            super("CSASL", 1.5, info);
//
//            AccessController.doPrivileged(new PrivilegedAction<Void>() {
//                public Void run() {
//
//                    // Server mechanisms
//                    put("SaslServerFactory.PLAIN",
//                            "org.ietf.nea.pt.socket.sasl.Test#PlainSaslServerFactory");
//                    return null;
//                }
//            });
//        }
//    }
//
//    public static class PlainSaslServerFactory implements SaslServerFactory {
//
//        private static final String myMechs[] = { "PLAIN", //
//        };
//
//        private static final int mechPolicies[] = {};
//
//        private static final int PLAIN = 0;
//
//        public SaslServer createSaslServer(String mech, String protocol,
//                String serverName, Map<String, ?> props, CallbackHandler cbh)
//                throws SaslException {
//
//            if (mech.equals(myMechs[PLAIN])) {
//
//                if (cbh == null) {
//                    throw new SaslException(
//                            "Callback handler with support for AuthorizeCallback required");
//                }
//                return new PlainServer(protocol, serverName, cbh);
//            }
//            return null;
//        };
//
//        public String[] getMechanismNames(Map<String, ?> props) {
//            return myMechs;
//        }
//
//    }
//
//    private static class PlainServer implements SaslServer {
//
//        private final static int MAX_MESSAGE_LENGTH = 65536;
//        private boolean completed = false;
//        private String authzId;
//        private String serverName;
//        private CallbackHandler cbh;
//
//        public PlainServer(String authzId, String serverName,
//                CallbackHandler cbh) {
//            this.completed = false;
//            this.authzId = authzId;
//            this.serverName = serverName;
//            this.cbh = cbh;
//        }
//
//        @Override
//        public String getMechanismName() {
//
//            return "PLAIN";
//        }
//
//        @Override
//        public byte[] evaluateResponse(byte[] response) throws SaslException {
//
//            if (completed) {
//                throw new IllegalStateException(
//                        "PLAIN authentication already completed");
//            }
//
//            if (response != null && response.length >= 0) {
//                if (response.length >= MAX_MESSAGE_LENGTH) {
//                    throw new SaslException(
//                            "Message to long. This implementation"
//                                    + " does support a maximum length of "
//                                    + MAX_MESSAGE_LENGTH + " byte.");
//                }
//                String authMessage = new String(response,
//                        Charset.forName("UTF-8"));
//                String[] tokens = authMessage.split("\0");
//                if (tokens == null || tokens.length < 2) {
//                    throw new SaslException("PLAIN: Message format error. "
//                            + "Authorization identity, authentication "
//                            + "identity or password missing");
//                } else {
//
//                    String authcId = "";
//                    String passwd = "";
//
//                    if (tokens.length > 2) {
//                        this.authzId = tokens[0];
//                        authcId = tokens[1];
//                        passwd = tokens[2];
//                    } else {
//                        authcId = tokens[0];
//                        passwd = tokens[1];
//                    }
//                    NameCallback ncb = new NameCallback(
//                            "PLAIN authentication ID: ", authcId);
//                    PasswordCallback pcb = new PasswordCallback(
//                            "PLAIN password: ", false);
//
//                    try {
//                        cbh.handle(new Callback[] { ncb, pcb });
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    } catch (UnsupportedCallbackException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    char[] pwChars = pcb.getPassword();
//                    pcb.clearPassword();
//
//                    if (pwChars == null || pwChars.length == 0) {
//                        completed = true;
//                        throw new SaslException(
//                                "PLAIN: authentication not successfull.");
//                    }
//                    String expectedPasswd = new String(pwChars);
//                    if (passwd.equals(expectedPasswd)) {
//                        completed = true;
//
//                        AuthorizeCallback acb = new AuthorizeCallback(authcId,
//                                authzId);
//                        try {
//                            cbh.handle(new Callback[] { acb });
//                        } catch (IOException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        } catch (UnsupportedCallbackException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                        if (acb.isAuthorized()) {
//                            authzId = acb.getAuthorizedID();
//                        } else {
//                            throw new SaslException(
//                                    "PLAIN: user not authorized: " + authcId);
//                        }
//
//                    }
//
//                    passwd = "0";
//                    passwd = null;
//                    expectedPasswd = "0";
//                    expectedPasswd = null;
//                }
//            }
//
//            return new byte[0];
//        }
//
//        @Override
//        public boolean isComplete() {
//            return completed;
//        }
//
//        @Override
//        public String getAuthorizationID() {
//
//            return this.authzId;
//        }
//
//        @Override
//        public byte[] unwrap(byte[] incoming, int offset, int len)
//                throws SaslException {
//            if (completed) {
//                throw new SaslException(
//                        "PLAIN supports neither integrity nor privacy");
//            } else {
//                throw new IllegalStateException(
//                        "PLAIN authentication not completed");
//            }
//        }
//
//        @Override
//        public byte[] wrap(byte[] outgoing, int offset, int len)
//                throws SaslException {
//            if (completed) {
//                throw new SaslException(
//                        "PLAIN supports neither integrity nor privacy");
//            } else {
//                throw new IllegalStateException(
//                        "PLAIN authentication not completed");
//            }
//        }
//
//        @Override
//        public Object getNegotiatedProperty(String propName) {
//            if (completed) {
//                if (propName.equals(Sasl.QOP)) {
//                    return "auth";
//                } else {
//                    return null;
//                }
//            } else {
//                throw new IllegalStateException(
//                        "PLAIN authentication not completed");
//            }
//        }
//
//        @Override
//        public void dispose() throws SaslException {
//            // do nothing
//
//        }
//
//    }

    private static class Cbh implements CallbackHandler {

        String type;

        public Cbh(String type) {
            this.type = type;
        }

        @Override
        public void handle(Callback[] callbacks) throws IOException,
                UnsupportedCallbackException {
            for (Callback callback : callbacks) {
                System.out.print(type + ": ");
                System.out.println(callback.toString());
                if (callback instanceof NameCallback) {
                    System.out.println(((NameCallback) callback).getPrompt());
                    ((NameCallback) callback).setName("Joe");

                }

                if (callback instanceof PasswordCallback) {
                    System.out.println(((PasswordCallback) callback)
                            .getPrompt());
                    ((PasswordCallback) callback).setPassword("Test1234"
                            .toCharArray());
                }
                
                if(callback instanceof AuthorizeCallback){
                    System.out.println(((AuthorizeCallback) callback)
                            .getAuthorizedID());
                    System.out.println(((AuthorizeCallback) callback)
                            .getAuthorizationID());
                    System.out.println(((AuthorizeCallback) callback)
                            .getAuthenticationID());
                    ((AuthorizeCallback) callback)
                    .setAuthorized(true);
                }
            }

        }

    }
}
