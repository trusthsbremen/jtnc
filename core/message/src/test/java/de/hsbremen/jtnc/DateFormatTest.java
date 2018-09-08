package de.hsbremen.jtnc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

public class DateFormatTest {

	@Test
	public void test(){
		//Date d = new Date(0L);
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		c.set(0, 0, 0, 0, 0, 0);
		
		String a = "1986-04-14T20:10:11Z";
		
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sd.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date d = null;
		try {
			d = sd.parse(a);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		    System.err.println(e.getMessage());
		}
		c.setTime(d);

		Assert.assertEquals(14, c.get(Calendar.DAY_OF_MONTH));
		System.out.println(sd.format(d));
	}

}
