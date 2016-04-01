package de.hsbremen.tc.tnc.transport.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.security.sasl.SaslException;

import org.apache.log4j.BasicConfigurator;
import org.ietf.nea.pt.serialize.reader.bytebuffer.PtTlsReaderFactory;
import org.ietf.nea.pt.serialize.writer.bytebuffer.PtTlsWriterFactory;
import org.ietf.nea.pt.socket.sasl.PlainServer;
import org.ietf.nea.pt.socket.sasl.SaslServerMechansims;
import org.ietf.nea.pt.socket.simple.DefaultAuthenticationServer;
import org.ietf.nea.pt.socket.simple.DefaultSocketTransportConnectionBuilder;
import org.ietf.nea.pt.socket.simple.Dummy;

import de.hsbremen.tc.tnc.message.t.enums.TcgTProtocolBindingEnum;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.transport.TransportConnection;
import de.hsbremen.tc.tnc.transport.TransportListener;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;
import de.hsbremen.tc.tnc.transport.exception.ListenerClosedException;

public class ServerTransportSocketCLI {

	public static void main(String[] args) {
		BasicConfigurator.configure();
		
		ExecutorService s = Executors.newFixedThreadPool(2);
		ServerSocket sSocket =  null;
		ServerThread t = null;
		try {
			sSocket = new ServerSocket(50247);
		} catch (IOException e1) {
			System.err.println(e1.getMessage());
			return;
		}
		
		String input = "";
		Scanner in = new Scanner(System.in);
		try{
		
			while(!(input = in.nextLine()).contains("quit")){
				System.out.print("#>");
				if(input.contains("start")){
					t = new ServerThread(sSocket);
					s.execute(t);
				}
				
				if(input.contains("stop")){
					if(t != null) t.close();
					s.shutdown();
					try {
						s.awaitTermination(2, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						System.err.println(e.getMessage());
					}
				}
			}
			
		}finally{
			in.close();
		}
		
		
		
	}
	
	public static class ServerThread implements Runnable {
		
		List<TransportConnection> connection;
		ServerSocket sSocket;
		ExecutorService runner;
		
		public ServerThread(ServerSocket socket){
			sSocket = socket;
		}
		
		public void close(){
		
			for (TransportConnection con : connection) {
				con.close();
			}
			try {
				sSocket.close();
			} catch (IOException e) {
				///	ignore
				System.err.println(e.getMessage());
			}
	
		}
		
		public void run() {
			connection = new ArrayList<>();
			try{
				while(!sSocket.isClosed()){
					System.out.println("Start listening...");
					Socket socket = sSocket.accept();
					System.out.println("Socket accepted.");
					
					DefaultSocketTransportConnectionBuilder builder
					    = new DefaultSocketTransportConnectionBuilder(true,
					            TcgTProtocolBindingEnum.PLAIN1,
							PtTlsWriterFactory.createProductionDefault(), 
							PtTlsReaderFactory.createProductionDefault());
				
					SaslServerMechansims mechanisms = new SaslServerMechansims();
					
					try{
                        mechanisms.addMechanismToStage((byte)1, new PlainServer(
                                Dummy.getPlainCallBackHandler(Dummy.getJoeCredentials())));
                        
                    }catch(SaslException e1){
                        System.err.println(e1.getMessage());
                    }
					
					TransportConnection con = builder.addAuthenticator(new DefaultAuthenticationServer(mechanisms))
					        .toConnection(socket.getInetAddress().getHostAddress(),false, socket);
					    
					try {
						System.out.println("Try to open connection.");
						con.activate(new TransportListener() {
							@Override
							public void receive(ByteBuffer b) throws ListenerClosedException {
								System.out.println("BufferReceived b");
							}
							

							@Override
							public void notifyClose() {
								System.out.println("Underlying transport closed.");
								
							}
						});
						this.connection.add(con);
					} catch (ConnectionException e) {
						System.err.println(e.getMessage());
						con.close();
					}
				}
			}catch(IOException e){
				System.err.println(e.getMessage());
			}finally{
				this.close();
			}
		}
	}
	
}
