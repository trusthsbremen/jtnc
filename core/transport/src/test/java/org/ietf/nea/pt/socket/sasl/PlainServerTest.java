package org.ietf.nea.pt.socket.sasl;

import javax.security.sasl.SaslException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;




public class PlainServerTest {

    private PlainServer server;
    private String[] credentials = Dummy.getJoeCredentials();
    
    @BeforeClass
    public static void logSetup(){
        Dummy.setLogSettings();
    }
    
    @Before
    public void setUp() throws SaslException{

        this.server = new PlainServer(Dummy.getPlainCallBackHandler(credentials));
    }
    
    @Test
    public void testMechanismName(){
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test getMechanismName()."));
        Assert.assertEquals(this.server.getMechanismName(),"PLAIN");
    }
    
    @Test
    public void testAuthentication() throws SaslException{
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test Authentication."));
        byte[] challenge = this.server.evaluateResponse(Dummy.getResponse(this.credentials));
        Assert.assertNull(challenge);
        Assert.assertTrue(server.isComplete());
    }
    
    @Test(expected=SaslException.class)
    public void testAuthenticationWrongPasssword() throws SaslException{
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test Authentication with wrong Password."));
        this.server.evaluateResponse(Dummy.getResponse(new String[]{this.credentials[0],"Test"}));

    }
    
    @Test(expected=SaslException.class)
    public void testAuthenticationWrongUsername() throws SaslException{
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test Authentication with wrong Username."));
        this.server.evaluateResponse(Dummy.getResponse(new String[]{"James",this.credentials[1]}));

    }
    
    @Test(expected=SaslException.class)
    public void testAuthenticationWrongUsernameAndPassword() throws SaslException{
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test Authentication with wrong Username and Password."));
        this.server.evaluateResponse(Dummy.getResponse(Dummy.getMollyCredentials()));

    }
    
    @Test(expected=SaslException.class)
    public void testAuthenticationEmptyPasssword() throws SaslException{
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test Authentication with empty Password."));
        this.server.evaluateResponse(Dummy.getResponse(new String[]{this.credentials[0],""}));

    }
    
    @Test(expected=SaslException.class)
    public void testAuthenticationEmptyUsername() throws SaslException{
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test Authentication with empty Username."));
        this.server.evaluateResponse(Dummy.getResponse(new String[]{"",this.credentials[1]}));

    }
    
    @Test(expected=SaslException.class)
    public void testAuthenticationEmptyUsernameAndPassword() throws SaslException{
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test Authentication with empty Password and Username."));
        this.server.evaluateResponse(Dummy.getResponse(new String[]{"",""}));

    }
    
    @Test(expected=SaslException.class)
    public void testAuthenticationNullPasssword() throws SaslException{
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test Authentication with NULL Password."));
        this.server.evaluateResponse(Dummy.getResponse(new String[]{this.credentials[0],null}));

    }
    
    @Test(expected=SaslException.class)
    public void testAuthenticationNullUsername() throws SaslException{
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test Authentication with NULL Username."));
        this.server.evaluateResponse(Dummy.getResponse(new String[]{null,this.credentials[1]}));

    }
    
    @Test(expected=SaslException.class)
    public void testAuthenticationNullUsernameAndPassword() throws SaslException{
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test Authentication with NULL Username and NULL Password."));
        this.server.evaluateResponse(Dummy.getResponse(new String[]{null,null}));

    }
    
    @Test(expected=SaslException.class)
    public void testAuthenticationEmptyChallenge() throws SaslException{
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test Authentication with empty challenge."));
        this.server.evaluateResponse(new byte[0]);

    }

    @Test(expected=SaslException.class)
    public void testAuthenticationNullChallenge() throws SaslException{
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test Authentication with NULL challenge."));
        this.server.evaluateResponse(null);

    }
}
