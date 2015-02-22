package test;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

public class FileTest {

    private static final Pattern CONFIG_FILE_PATH =
            Pattern.compile("(start) (([^\\\\(){}:\\*\\?<>\\|\\\"\\'])+)");
    
    @Test
    public void test12(){
        
        String input = "start /home/sidanetdev/git/jtnc/examples/naa-example/config/tnc_config";
        Matcher m  = CONFIG_FILE_PATH.matcher(input);
        if (m.find()) {

            File file = new File(m.group(2).trim());
            System.out.println("-"+file.getAbsolutePath()+"-");
            Assert.assertTrue(file.exists());
            Assert.assertTrue(file.canRead());
        }else{
            Assert.fail();
        }
    }
    
}
