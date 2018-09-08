package org.ietf.nea.pt.socket.simple;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;

import org.ietf.nea.pt.socket.Authenticator;
import org.ietf.nea.pt.socket.SocketMessenger;
import org.ietf.nea.pt.socket.sasl.ExternalClient;
import org.ietf.nea.pt.socket.sasl.ExternalServer;
import org.ietf.nea.pt.socket.sasl.PlainClient;
import org.ietf.nea.pt.socket.sasl.PlainServer;
import org.ietf.nea.pt.socket.sasl.SaslClientMechansims;
import org.ietf.nea.pt.socket.sasl.SaslServerMechansims;
import org.ietf.nea.pt.socket.simple.DefaultAuthenticationClient;
import org.ietf.nea.pt.socket.simple.DefaultAuthenticationServer;
import org.ietf.nea.pt.value.enums.PtTlsSaslResultEnum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class AuthenticationTest {

    private Queue<TransportMessage> serverQ;
    private Queue<TransportMessage> clientQ;

    private SocketMessenger messengerS;
    private SocketMessenger messengerC;
    
    @BeforeClass
    public static void logSetup() {
        Dummy.setLogSettings();
    }

    @Before
    public void initateMessaging() {
        serverQ = new ArrayBlockingQueue<>(4);
        clientQ = new ArrayBlockingQueue<>(4);

        messengerS = Dummy.createDummyMessenger(serverQ, clientQ);
        messengerC = Dummy.createDummyMessenger(clientQ, serverQ);
    }
    
    @Test
    public void successfullPlainAuthentication() throws SaslException{
        
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test plain authentication."));
        
        ExecutorService runner = Executors.newFixedThreadPool(2);

        SaslClient pc = new PlainClient(null,Dummy.getPlainCallBackHandler(Dummy.getJoeCredentials()));
        SaslClientMechansims clientMechs = new SaslClientMechansims();
        clientMechs.addMechanism(pc);
        final Authenticator client = new DefaultAuthenticationClient(clientMechs);
        
        SaslServer ps = new PlainServer(Dummy.getPlainCallBackHandler(Dummy.getJoeCredentials()));
        SaslServerMechansims serverMechs  = new SaslServerMechansims(); 
        serverMechs.addMechanismToStage((byte)1, ps);
        final Authenticator server = new DefaultAuthenticationServer(serverMechs);
        
        runner.execute(new Runnable() {
            
            @Override
            public void run() {
                try {
                    server.authenticate(messengerS);

                } catch (SerializationException | ValidationException
                        | ConnectionException e) {
                    // TODO Auto-generated catch block
                    System.err.println(e.getMessage());
                    
                }
            }
        });
        
        
        runner.execute(new Runnable() {
            
            @Override
            public void run() {
                try {
                    client.authenticate(messengerC);

                } catch (SerializationException | ValidationException
                        | ConnectionException e) {
                    // TODO Auto-generated catch block
                    System.err.println(e.getMessage());
                }
                
            }
        });
        
        try {
            runner.shutdown();
            runner.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            System.err.println(e.getMessage());
        }

        Assert.assertEquals(PtTlsSaslResultEnum.SUCCESS, server.getAuthenticationResult());
        Assert.assertEquals(PtTlsSaslResultEnum.SUCCESS, client.getAuthenticationResult());
        
    }
    
    @Test
    public void successfullExternalAuthentication() throws SaslException{
        
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test external authentication."));
        
        ExecutorService runner = Executors.newFixedThreadPool(2);

        SaslClient pc = new PlainClient(null,Dummy.getPlainCallBackHandler(Dummy.getJoeCredentials()));
        SaslClientMechansims clientMechs = new SaslClientMechansims();
        clientMechs.addMechanism(pc);
        final Authenticator client = new DefaultAuthenticationClient(clientMechs);
        
        SaslServer ps = new PlainServer(Dummy.getPlainCallBackHandler(Dummy.getJoeCredentials()));
        SaslServerMechansims serverMechs  = new SaslServerMechansims(); 
        serverMechs.addMechanismToStage((byte)1, ps);
        final Authenticator server = new DefaultAuthenticationServer(serverMechs);
        
        runner.execute(new Runnable() {
            
            @Override
            public void run() {
                try {
                    server.authenticate(messengerS);

                } catch (SerializationException | ValidationException
                        | ConnectionException e) {
                    // TODO Auto-generated catch block
                    System.err.println(e.getMessage());
                    
                }
            }
        });
        
        
        runner.execute(new Runnable() {
            
            @Override
            public void run() {
                try {
                    client.authenticate(messengerC);

                } catch (SerializationException | ValidationException
                        | ConnectionException e) {
                    // TODO Auto-generated catch block
                    System.err.println(e.getMessage());
                }
                
            }
        });
        
        try {
            runner.shutdown();
            runner.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            System.err.println(e.getMessage());
        }

        Assert.assertEquals(PtTlsSaslResultEnum.SUCCESS, server.getAuthenticationResult());
        Assert.assertEquals(PtTlsSaslResultEnum.SUCCESS, client.getAuthenticationResult());
        
    }
    
    @Test
    public void successfullPlainExternalAuthenticationChain() throws SaslException{
        
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test chained plain and external authentication."));
        
        ExecutorService runner = Executors.newFixedThreadPool(2);

        SaslClient pc = new PlainClient(null,Dummy.getPlainCallBackHandler(Dummy.getJoeCredentials()));
        SaslClient pc2 = new ExternalClient(Dummy.getMollyCredentials()[0]);
        SaslClientMechansims clientMechs = new SaslClientMechansims();
        clientMechs.addMechanism(pc);
        clientMechs.addMechanism(pc2);
        final Authenticator client = new DefaultAuthenticationClient(clientMechs);
        
        SaslServer ps = new PlainServer(Dummy.getPlainCallBackHandler(Dummy.getJoeCredentials()));
        SaslServer ps2 = new ExternalServer(Dummy.getPlainCallBackHandler(Dummy.getMollyCredentials()));
        SaslServerMechansims serverMechs  = new SaslServerMechansims(); 
        serverMechs.addMechanismToStage((byte)1, ps);
        serverMechs.addMechanismToStage((byte)2, ps2);
        final Authenticator server = new DefaultAuthenticationServer(serverMechs);
        
        runner.execute(new Runnable() {
            
            @Override
            public void run() {
                try {
                    server.authenticate(messengerS);

                } catch (SerializationException | ValidationException
                        | ConnectionException e) {
                    // TODO Auto-generated catch block
                    System.err.println(e.getMessage());
                    
                }
            }
        });
        
        
        runner.execute(new Runnable() {
            
            @Override
            public void run() {
                try {
                    client.authenticate(messengerC);

                } catch (SerializationException | ValidationException
                        | ConnectionException e) {
                    // TODO Auto-generated catch block
                    System.err.println(e.getMessage());
                }
                
            }
        });
        
        try {
            runner.shutdown();
            runner.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            System.err.println(e.getMessage());
        }

        Assert.assertEquals(PtTlsSaslResultEnum.SUCCESS, server.getAuthenticationResult());
        Assert.assertEquals(PtTlsSaslResultEnum.SUCCESS, client.getAuthenticationResult());
        
    }
    
    @Test
    public void failedAuthenticationCredentialsMismatch() throws SaslException{
        
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test plain authentication with wrong credentials."));
        
        ExecutorService runner = Executors.newFixedThreadPool(2);

        SaslClient pc = new PlainClient(null,Dummy.getPlainCallBackHandler(Dummy.getJoeCredentials()));
        SaslClientMechansims clientMechs = new SaslClientMechansims();
        clientMechs.addMechanism(pc);
        final Authenticator client = new DefaultAuthenticationClient(clientMechs);
        
        SaslServer ps = new PlainServer(Dummy.getPlainCallBackHandler(Dummy.getJoeCredentials()));
        SaslServerMechansims serverMechs  = new SaslServerMechansims(); 
        serverMechs.addMechanismToStage((byte)1, ps);
        final Authenticator server = new DefaultAuthenticationServer(serverMechs);
        
        FutureTask<Boolean> s = new FutureTask<>( new Callable<Boolean>() {
            
            @Override
            public Boolean call() throws SerializationException, ValidationException, ConnectionException {
                try{
                    server.authenticate(messengerS);
                }catch(ValidationException e){
                    clientQ.offer(messengerS.createValidationErrorMessage(e));
                    throw e;
                }
                return true;
               
            }
        });
        
        runner.execute(s);

        FutureTask<Boolean> c = new FutureTask<>(new Callable<Boolean>() {
            
            @Override
            public Boolean call() throws SerializationException, ValidationException, ConnectionException {
                try{
                    client.authenticate(messengerC);
                }catch(ValidationException e){
                    serverQ.offer(messengerC.createValidationErrorMessage(e));
                    throw e;
                }

                return true;
            }
        });
        
        runner.execute(c);
        
        try {
            runner.shutdown();
            runner.awaitTermination(2, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            System.err.println(e.getMessage());
        }
        
        try{
            s.get(2, TimeUnit.SECONDS);
        }catch(Exception e){
            
            System.err.println(e.getMessage());
            
            if(!(e instanceof ExecutionException)){
                Assert.fail();
            }else{
                if(!(e.getCause() instanceof ConnectionException)){
                    Assert.fail();
                }
            }
      
        }
        
        try{
            c.get(2, TimeUnit.SECONDS);
        }catch(Exception e){
            
            System.err.println(e.getMessage());
            
            if(!(e instanceof ExecutionException)){
                Assert.fail();
            }else{
               if(!(e.getCause() instanceof ConnectionException)){
                   Assert.fail();
               }
            }
        }
    }
    
    @Test
    public void failedChainedAuthenticationCredentialsMismatch() throws SaslException{
        
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test chained plain and external authentication with wrong credentials last."));
        
        ExecutorService runner = Executors.newFixedThreadPool(2);

        SaslClient pc = new PlainClient(null,Dummy.getPlainCallBackHandler(Dummy.getJoeCredentials()));
        SaslClient pc2 = new ExternalClient(Dummy.getMollyCredentials()[0]);
        SaslClientMechansims clientMechs = new SaslClientMechansims();
        clientMechs.addMechanism(pc);
        clientMechs.addMechanism(pc2);
        final Authenticator client = new DefaultAuthenticationClient(clientMechs);
        
        SaslServer ps = new PlainServer(Dummy.getPlainCallBackHandler(Dummy.getJoeCredentials()));
        SaslServer ps2 = new ExternalServer(Dummy.getPlainCallBackHandler(Dummy.getJoeCredentials()));
        SaslServerMechansims serverMechs  = new SaslServerMechansims(); 
        serverMechs.addMechanismToStage((byte)1, ps);
        serverMechs.addMechanismToStage((byte)2, ps2);
        final Authenticator server = new DefaultAuthenticationServer(serverMechs);
        
        FutureTask<Boolean> s = new FutureTask<>( new Callable<Boolean>() {
            
            @Override
            public Boolean call() throws SerializationException, ValidationException, ConnectionException {
                try{
                    server.authenticate(messengerS);
                }catch(ValidationException e){
                    clientQ.offer(messengerS.createValidationErrorMessage(e));
                    throw e;
                }
                return true;
               
            }
        });
        
        runner.execute(s);

        FutureTask<Boolean> c = new FutureTask<>(new Callable<Boolean>() {
            
            @Override
            public Boolean call() throws SerializationException, ValidationException, ConnectionException {
                try{
                    client.authenticate(messengerC);
                }catch(ValidationException e){
                    serverQ.offer(messengerC.createValidationErrorMessage(e));
                    throw e;
                }

                return true;
            }
        });
        
        runner.execute(c);
        
        try {
            runner.shutdown();
            runner.awaitTermination(2, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            System.err.println(e.getMessage());
        }
        
        try{
            s.get(2, TimeUnit.SECONDS);
        }catch(Exception e){
            
            System.err.println(e.getMessage());
            
            if(!(e instanceof ExecutionException)){
                Assert.fail();
            }else{
                if(!(e.getCause() instanceof ConnectionException)){
                    Assert.fail();
                }
            }
      
        }
        
        try{
            c.get(2, TimeUnit.SECONDS);
        }catch(Exception e){
            
            System.err.println(e.getMessage());
            
            if(!(e instanceof ExecutionException)){
                Assert.fail();
            }else{
               if(!(e.getCause() instanceof ConnectionException)){
                   Assert.fail();
               }
            }
        }
    }
    
    @Test
    public void failedChainedAuthenticationCredentialsMismatch2() throws SaslException{
        
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test chained plain and external authentication with wrong credentials first."));
        
        ExecutorService runner = Executors.newFixedThreadPool(2);

        SaslClient pc = new PlainClient(null,Dummy.getPlainCallBackHandler(Dummy.getJoeCredentials()));
        SaslClient pc2 = new ExternalClient(Dummy.getMollyCredentials()[0]);
        SaslClientMechansims clientMechs = new SaslClientMechansims();
        clientMechs.addMechanism(pc);
        clientMechs.addMechanism(pc2);
        final Authenticator client = new DefaultAuthenticationClient(clientMechs);
        
        SaslServer ps = new PlainServer(Dummy.getPlainCallBackHandler(Dummy.getMollyCredentials()));
        SaslServer ps2 = new ExternalServer(Dummy.getPlainCallBackHandler(Dummy.getMollyCredentials()));
        SaslServerMechansims serverMechs  = new SaslServerMechansims(); 
        serverMechs.addMechanismToStage((byte)1, ps);
        serverMechs.addMechanismToStage((byte)2, ps2);
        final Authenticator server = new DefaultAuthenticationServer(serverMechs);
        
        FutureTask<Boolean> s = new FutureTask<>( new Callable<Boolean>() {
            
            @Override
            public Boolean call() throws SerializationException, ValidationException, ConnectionException {
                try{
                    server.authenticate(messengerS);
                }catch(ValidationException e){
                    clientQ.offer(messengerS.createValidationErrorMessage(e));
                    throw e;
                }
                return true;
               
            }
        });
        
        runner.execute(s);

        FutureTask<Boolean> c = new FutureTask<>(new Callable<Boolean>() {
            
            @Override
            public Boolean call() throws SerializationException, ValidationException, ConnectionException {
                try{
                    client.authenticate(messengerC);
                }catch(ValidationException e){
                    serverQ.offer(messengerC.createValidationErrorMessage(e));
                    throw e;
                }

                return true;
            }
        });
        
        runner.execute(c);
        
        try {
            runner.shutdown();
            runner.awaitTermination(2, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            System.err.println(e.getMessage());
        }
        
        try{
            s.get(2, TimeUnit.SECONDS);
        }catch(Exception e){
            
            System.err.println(e.getMessage());
            
            if(!(e instanceof ExecutionException)){
                Assert.fail();
            }else{
                if(!(e.getCause() instanceof ConnectionException)){
                    Assert.fail();
                }
            }
      
        }
        
        try{
            c.get(2, TimeUnit.SECONDS);
        }catch(Exception e){
            
            System.err.println(e.getMessage());
            
            if(!(e instanceof ExecutionException)){
                Assert.fail();
            }else{
               if(!(e.getCause() instanceof ConnectionException)){
                   Assert.fail();
               }
            }
        }
    }
    
    @Test
    public void failedAuthenticationMechanismMismatch() throws SaslException{
        
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test mismatching authentication mechanisms."));
        
        ExecutorService runner = Executors.newFixedThreadPool(2);

        SaslClient pc = new PlainClient(null,Dummy.getPlainCallBackHandler(Dummy.getJoeCredentials()));
        SaslClientMechansims clientMechs = new SaslClientMechansims();
        clientMechs.addMechanism(pc);
        final Authenticator client = new DefaultAuthenticationClient(clientMechs);
        
        SaslServer ps = new ExternalServer(Dummy.getPlainCallBackHandler(Dummy.getMollyCredentials()));
        SaslServerMechansims serverMechs  = new SaslServerMechansims(); 
        serverMechs.addMechanismToStage((byte)1, ps);
        final Authenticator server = new DefaultAuthenticationServer(serverMechs);
        
        FutureTask<Boolean> s = new FutureTask<>( new Callable<Boolean>() {
            
            @Override
            public Boolean call() throws SerializationException, ValidationException, ConnectionException {
                try{
                    server.authenticate(messengerS);
                }catch(ValidationException e){
                    clientQ.offer(messengerS.createValidationErrorMessage(e));
                    throw e;
                }
                return true;
               
            }
        });
        
        runner.execute(s);

        FutureTask<Boolean> c = new FutureTask<>(new Callable<Boolean>() {
            
            @Override
            public Boolean call() throws SerializationException, ValidationException, ConnectionException {
                try{
                    client.authenticate(messengerC);
                }catch(ValidationException e){
                    serverQ.offer(messengerC.createValidationErrorMessage(e));
                    throw e;
                }

                return true;
            }
        });
        
        runner.execute(c);
        
        try {
            runner.shutdown();
            runner.awaitTermination(2, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            System.err.println(e.getMessage());
        }
        
        try{
            s.get(2, TimeUnit.SECONDS);
        }catch(Exception e){
            
            System.err.println(e.getMessage());
            
            if(!(e instanceof ExecutionException)){
                Assert.fail();
            }else{
                if(!(e.getCause() instanceof ConnectionException)){
                    Assert.fail();
                }
            }
      
        }
        
        try{
            c.get(2, TimeUnit.SECONDS);
        }catch(Exception e){
            
            System.err.println(e.getMessage());
            
            if(!(e instanceof ExecutionException)){
                Assert.fail();
            }else{
               if(!(e.getCause() instanceof ValidationException)){
                   Assert.fail();
               }
            }
        }
    }
}
