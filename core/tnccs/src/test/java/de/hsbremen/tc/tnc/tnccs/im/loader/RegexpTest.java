package de.hsbremen.tc.tnc.tnccs.im.loader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class RegexpTest {
    
    @Test
    public void testpath(){
        
        List<String> ps = new ArrayList<>();
        ps.add("C:\\ABC\\A\\imc.jar");
        ps.add("\\\\ABC\\A\\imc.jar");
        ps.add("C:/ABC/A/imc.jar");
        ps.add("/ABC/A/imc.jar");

        for (String string : ps) {
            File f = new File(string);
            try {
                URI i = f.toURI();
                System.out.println(i.toString());
                URL u = i.toURL();
                System.out.println(u.toString());
                
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        } 
    }

}
