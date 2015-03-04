package de.hsbremen.tc.tnc.transport.newp.connection;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;

import org.apache.log4j.BasicConfigurator;
import org.ietf.nea.pt.DefaultTransportAttributes;
import org.ietf.nea.pt.serialize.reader.bytebuffer.PtTlsReaderFactory;
import org.ietf.nea.pt.serialize.writer.bytebuffer.PtTlsWriterFactory;
import org.ietf.nea.pt.socket.SocketTransportConnection;

import de.hsbremen.tc.tnc.message.t.enums.TcgTProtocolBindingEnum;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.message.util.DefaultByteBuffer;
import de.hsbremen.tc.tnc.transport.TnccsListener;
import de.hsbremen.tc.tnc.transport.TransportConnection;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;
import de.hsbremen.tc.tnc.transport.exception.ListenerClosedException;

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
							new DefaultTransportAttributes(socket.getInetAddress().getHostAddress(),TcgTProtocolBindingEnum.PLAIN1), 
							PtTlsWriterFactory.createProductionDefault(), 
							PtTlsReaderFactory.createProductionDefault(), 
							Executors.newSingleThreadExecutor());
					
					try {
						System.out.println("Try to open connection...");
						connection.open(new TnccsListener() {
							@Override
							public void receive(ByteBuffer b) throws ListenerClosedException {
								System.out.println("BufferReceived b");
							}

							@Override
							public void notifyClose() {
								System.out.println("Underlying transport closed.");
								
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
