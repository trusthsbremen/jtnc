package org.ietf.nea.pt.socket.sasl;

import java.nio.charset.Charset;

import javax.security.sasl.SaslException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;




public class PlainClientTest {

    private PlainClient client;
    private String[] credentials = Dummy.getJoeCredentials();
    
    @BeforeClass
    public static void logSetup(){
        Dummy.setLogSettings();
    }
    
    @Before
    public void setUp() throws SaslException{
        this.client = new PlainClient(Dummy.getPlainCallBackHandler(credentials, true));
    }
    
    @Test
    public void testMechanismName(){
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test getMechanismName()."));
        Assert.assertEquals(this.client.getMechanismName(),"PLAIN");
    }
    
    @Test
    public void testAuthentication() throws SaslException{
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test Authentication."));
        byte[] response = this.client.evaluateChallenge(new byte[0]);
        String[] tokens = new String(response, Charset.forName("UTF8")).split("\0");
        Assert.assertEquals(tokens.length, 3);
        Assert.assertTrue(tokens[0].isEmpty());
        Assert.assertEquals(tokens[1],credentials[0]);
        Assert.assertEquals(tokens[2],credentials[1]);
        Assert.assertTrue(client.isComplete());
    }
    
}
