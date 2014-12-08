package de.hsbremen.tc.tnc.examples.first;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.BasicConfigurator;
import org.ietf.nea.pa.serialize.reader.PaReaderFactory;
import org.ietf.nea.pa.serialize.writer.PaWriterFactory;
import org.trustedcomputinggroup.tnc.ifimv.IMV;

import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImvConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.adapter.imv.ImvAdapterIetf;
import de.hsbremen.tc.tnc.im.adapter.tncs.TncsAdapterIetfFactory;
import de.hsbremen.tc.tnc.im.evaluate.example.os.OsImvEvaluatorFactory;
import de.hsbremen.tc.tnc.im.session.DefaultImvSessionFactory;

public class NaaCLI {
	
	public static void main(String[] args) {
		// LOGGER
		BasicConfigurator.configure();
		
		Naa naa = new Naa();
		List<IMV> imvs = new ArrayList<>();
		imvs.add(new TestImvOs());
		naa.loadImv(imvs);
		
		String input = "";
		Scanner in = new Scanner(System.in);
		try{
		
			while(!(input = in.nextLine()).contains("quit")){
				System.out.print("#>");
				if(input.contains("start")){
					naa.start();
				}
				
				if(input.contains("quit")){
					naa.stop();
				}
			}
			
		}finally{
			in.close();
		}
		
		
		
	}

	private static class TestImvOs extends ImvAdapterIetf{
		
		public TestImvOs(){
			super(new ImParameter(), new TncsAdapterIetfFactory(),
					new DefaultImvSessionFactory(),
					OsImvEvaluatorFactory.getInstance(),
					new ImvConnectionAdapterFactoryIetf(PaWriterFactory.createProductionDefault()),
					PaReaderFactory.createProductionDefault());
		}
		
	}
}
