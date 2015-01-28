
package de.hsbremen.tc.tnc.examples.nar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.BasicConfigurator;
import org.ietf.nea.pa.serialize.reader.bytebuffer.PaReaderFactory;
import org.ietf.nea.pa.serialize.writer.bytebuffer.PaWriterFactory;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;

import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.adapter.imc.ImcAdapterIetf;
import de.hsbremen.tc.tnc.im.adapter.tncc.TnccAdapterIetfFactory;
import de.hsbremen.tc.tnc.im.evaluate.example.os.OsImcEvaluatorFactory;
import de.hsbremen.tc.tnc.im.session.DefaultImcSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImcSession;
import de.hsbremen.tc.tnc.im.session.DefaultImSessionManager;

public class NarCLI {
	
	public static void main(String[] args) {
		// LOGGER
		BasicConfigurator.configure();
		
		Nar nar = new Nar();
		List<IMC> imcs = new ArrayList<>();
		imcs.add(new TestImcOs());
		nar.loadImc(imcs);
		
		String input = "";
		Scanner in = new Scanner(System.in);

			try{
				while(!(input = in.nextLine()).contains("quit")){
					System.out.print("#>");
					if(input.contains("handshake")){
						try {
							nar.startHandshake();
						} catch (IOException e) {
							System.err.println("Handshake could not be started: " + e.getMessage());
						} 
					}
					
					if(input.contains("stop")){
						nar.stopHandshake();
					}
				}
			} catch (IOException e) {
				System.err.println("Handshake could not be stopped: " + e.getMessage());
			} finally{
				in.close();
				nar.stop();
			}

		
	}

	private static class TestImcOs extends ImcAdapterIetf{
		
		public TestImcOs(){
			super(new ImParameter(), new TnccAdapterIetfFactory(),
					new DefaultImcSessionFactory(),
					new DefaultImSessionManager<IMCConnection, ImcSession>(3000),
					new OsImcEvaluatorFactory(),
					new ImcConnectionAdapterFactoryIetf(PaWriterFactory.createProductionDefault()),
					PaReaderFactory.createProductionDefault());
		}
		
	}
}
