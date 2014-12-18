package de.hsbremen.tc.tnc.transport.newp.connection;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;

import org.apache.log4j.BasicConfigurator;
import org.ietf.nea.pt.serialize.reader.bytebuffer.PtTlsReaderFactory;
import org.ietf.nea.pt.serialize.writer.bytebuffer.PtTlsWriterFactory;

import de.hsbremen.tc.tnc.message.t.enums.TcgTProtocolEnum;
import de.hsbremen.tc.tnc.message.t.enums.TcgTVersionEnum;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.message.util.DefaultByteBuffer;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class ClientTransportSocketCLI {

	
	
	public static void main(String[] args) {
		BasicConfigurator.configure();
		
		Socket socket = null;
		TransportConnection connection = null;
		String input = "";
		Scanner in = new Scanner(System.in);
		try{
			
			while(!(input = in.nextLine()).contains("quit")){
				System.out.print("#>");
				
				if(input.contains("start")){
					try {
						socket = new Socket("localhost", 50247);
						System.out.println("Connected to " + socket.getRemoteSocketAddress().toString());
					} catch (IOException e) {
						System.err.println(e.getMessage());
						return;
					}
					System.out.println("Create transport connection.");
					connection = new SocketTransportConnection(true, false, socket, 
							new DefaultTransportAttributes(TcgTProtocolEnum.PLAIN.value(),TcgTVersionEnum.V1.value()), 
							new SocketTransportAddress("localhost", 50251), 
							PtTlsWriterFactory.createProductionDefault(), 
							PtTlsReaderFactory.createProductionDefault(131072), 
							Executors.newSingleThreadExecutor());
					
					try {
						System.out.println("Try to open connection...");
						connection.open(new TnccsValueListener() {
							@Override
							public void receive(ByteBuffer b) throws ListenerClosedException {
								System.out.println("BufferReceived b");
							}
						});
					} catch (ConnectionException e) {
						System.err.println(e.getMessage());
						try {
							socket.close();
						} catch (IOException e1) {
							System.err.println(e.getMessage());
						}
						return;
					}
					
					System.out.println("Connection open, send TNCCS Data.");
					byte[] imBatch = new byte[]{2, -128, 0, 1, 0, 0, 0, 36, -128, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 28, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, -1, -1, 80, 87, 78, 68};
					
					ByteBuffer buf = new DefaultByteBuffer(imBatch.length);
					buf.write(imBatch);
					try {
						connection.send(buf);
					} catch (ConnectionException e) {
						System.err.println(e.getMessage());
						connection.close();
					}
				}
				
				if(input.contains("stop")){
					System.out.println("Connection will be closed.");
					if(connection.isOpen())connection.close();
				}
			}
			
		}finally{
			in.close();
		}
	}
	
}
