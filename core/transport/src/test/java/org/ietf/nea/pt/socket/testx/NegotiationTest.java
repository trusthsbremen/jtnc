package org.ietf.nea.pt.socket.testx;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.ietf.nea.pt.socket.testx.simple.DefaultNegotiationInitiator;
import org.ietf.nea.pt.socket.testx.simple.DefaultNegotiationResponder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class NegotiationTest {
    
    private Queue<TransportMessage> initiatorQ;
    private Queue<TransportMessage> responderQ;
    
    private TransportMessenger messengerI;
    private TransportMessenger messengerR;
    
    
    @BeforeClass
    public static void logSetup(){
        Dummy.setLogSettings();
    }
    

    @Before
    public void initateMessaging(){
        initiatorQ = new ArrayBlockingQueue<>(4);
        responderQ = new ArrayBlockingQueue<>(4);
        
        messengerI = Dummy.createDummyMessenger(initiatorQ, responderQ);
        messengerR = Dummy.createDummyMessenger(responderQ, initiatorQ);
    }
    
    @Test
    public void testSuccessfullNegotiation(){
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test simple negotiation."));
        
        ExecutorService runner = Executors.newFixedThreadPool(2);

        final Negotiator initiator = new DefaultNegotiationInitiator((short)1, (short)1, (short)1);
        final Negotiator responder = new DefaultNegotiationResponder((short)1, (short)1, (short)1);
        
        runner.execute(new Runnable() {
            
            @Override
            public void run() {
                try {
                    initiator.negotiate(messengerI);

                } catch (SerializationException | ValidationException
                        | ConnectionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    
                }
            }
        });
        
        
        runner.execute(new Runnable() {
            
            @Override
            public void run() {
                try {
                    responder.negotiate(messengerR);

                } catch (SerializationException | ValidationException
                        | ConnectionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
        });
        
        try {
            runner.shutdown();
            runner.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Assert.assertEquals(1, initiator.getNegotiatedVersion());
        Assert.assertEquals(1, responder.getNegotiatedVersion());
        
    }
    
   
    
    @Test
    public void testSuccessfullNegotiationRangeOverlapp(){
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test negotiation with range and preferred version mismatch."));
        ExecutorService runner = Executors.newFixedThreadPool(2);

        final Negotiator initiator = new DefaultNegotiationInitiator((short)1, (short)4, (short)2);
        final Negotiator responder = new DefaultNegotiationResponder((short)3, (short)6, (short)5);
        
        runner.execute(new Runnable() {
            
            @Override
            public void run() {
                try {
                    initiator.negotiate(messengerI);

                } catch (SerializationException | ValidationException
                        | ConnectionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    
                }
            }
        });
        
        
        runner.execute(new Runnable() {
            
            @Override
            public void run() {
                try {
                    responder.negotiate(messengerR);

                } catch (SerializationException | ValidationException
                        | ConnectionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
        });
        
        try {
            runner.shutdown();
            runner.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // four is the highest version of both
        Assert.assertEquals(4, initiator.getNegotiatedVersion());
        Assert.assertEquals(4, responder.getNegotiatedVersion());
        
    }
    
    @Test
    public void testSuccessfullNegotiationRangeOverlappPreferredMatch(){
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test negotiation with range an matching preferred version."));
        
        ExecutorService runner = Executors.newFixedThreadPool(2);

        final Negotiator initiator = new DefaultNegotiationInitiator((short)1, (short)4, (short)3);
        final Negotiator responder = new DefaultNegotiationResponder((short)2, (short)6, (short)3);
        
        runner.execute(new Runnable() {
            
            @Override
            public void run() {
                try {
                    initiator.negotiate(messengerI);

                } catch (SerializationException | ValidationException
                        | ConnectionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    
                }
            }
        });
        
        
        runner.execute(new Runnable() {
            
            @Override
            public void run() {
                try {
                    responder.negotiate(messengerR);

                } catch (SerializationException | ValidationException
                        | ConnectionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
        });
        
        try {
            runner.shutdown();
            runner.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // three is preferred
        Assert.assertEquals(3, initiator.getNegotiatedVersion());
        Assert.assertEquals(3, responder.getNegotiatedVersion());
        
    }
    
    @Test
    public void versionMissmatch() throws Exception{
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test version missmatch."));
        
        ExecutorService runner = Executors.newFixedThreadPool(2);
       
       
        final Negotiator initiator = new DefaultNegotiationInitiator((short)2, (short)2, (short)2);
        final Negotiator responder = new DefaultNegotiationResponder((short)1, (short)1, (short)1);
        
        FutureTask<Boolean> i = new FutureTask<>( new Callable<Boolean>() {
            
            @Override
            public Boolean call() throws SerializationException, ValidationException, ConnectionException {
                try{
                    initiator.negotiate(messengerI);
                }catch(ValidationException e){
                    responderQ.offer(messengerI.createValidationErrorMessage(e));
                    throw e;
                }
                return true;
               
            }
        });
        
        runner.execute(i);
        
        
        FutureTask<Boolean> r = new FutureTask<>(new Callable<Boolean>() {
            
            @Override
            public Boolean call() throws SerializationException, ValidationException, ConnectionException {
                try{
                    responder.negotiate(messengerR);
                }catch(ValidationException e){
                    initiatorQ.offer(messengerR.createValidationErrorMessage(e));
                    throw e;
                }

                return true;
            }
        });
        
        runner.execute(r);
        
        try {
            runner.shutdown();
            runner.awaitTermination(2, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        try{
            i.get(2, TimeUnit.SECONDS);
        }catch(Exception e){
            
            e.printStackTrace();
            
            if(!(e instanceof ExecutionException)){
                Assert.fail();
            }else{
                if(!(e.getCause() instanceof ConnectionException)){
                    Assert.fail();
                }
            }
      
        }
        
        try{
            r.get(2, TimeUnit.SECONDS);
        }catch(Exception e){
            
            e.printStackTrace();
            
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
