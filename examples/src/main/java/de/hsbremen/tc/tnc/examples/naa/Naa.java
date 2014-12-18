package de.hsbremen.tc.tnc.examples.naa;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ietf.nea.pb.serialize.reader.bytebuffer.PbReaderFactory;
import org.ietf.nea.pb.serialize.writer.bytebuffer.PbWriterFactory;
import org.ietf.nea.pt.serialize.reader.bytebuffer.PtTlsReaderFactory;
import org.ietf.nea.pt.serialize.writer.bytebuffer.PtTlsWriterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimv.IMV;

import de.hsbremen.tc.tnc.message.t.enums.TcgTProtocolEnum;
import de.hsbremen.tc.tnc.message.t.enums.TcgTVersionEnum;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImvAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.adapter.tncc.TncsAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.client.GlobalHandshakeRetryProxy;
import de.hsbremen.tc.tnc.tnccs.client.NewClient;
import de.hsbremen.tc.tnc.tnccs.client.NewDefaultClient;
import de.hsbremen.tc.tnc.tnccs.client.enums.ConnectionChangeTypeEnum;
import de.hsbremen.tc.tnc.tnccs.im.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImvManager;
import de.hsbremen.tc.tnc.tnccs.im.manager.exception.ImInitializeException;
import de.hsbremen.tc.tnc.tnccs.im.manager.simple.DefaultImvManager;
import de.hsbremen.tc.tnc.tnccs.im.route.DefaultImMessageRouter;
import de.hsbremen.tc.tnc.tnccs.session.base.NewSessionFactory;
import de.hsbremen.tc.tnc.tnccs.session.base.simple.DefaultServerSessionRunnableFactory;
import de.hsbremen.tc.tnc.transport.newp.connection.SocketTransportConnectionBuilder;
import de.hsbremen.tc.tnc.transport.newp.connection.TransportConnection;

public class Naa {
	private static final Logger LOGGER = LoggerFactory.getLogger(Naa.class);
	private static final long MAX_MSG_SIZE = 131072;
	
	private NewClient client;
	private ImvManager manager;
	private ServerSocket serverSocket;
	private SocketTransportConnectionBuilder connectionBuilder;
	private ExecutorService runner;
	private boolean stopped;
	
	public Naa(){
		
		GlobalHandshakeRetryProxy retryProxy = new GlobalHandshakeRetryProxy();
		
		this.manager = new DefaultImvManager(
				new DefaultImMessageRouter(),
				new ImvAdapterFactoryIetf(), 
				new TncsAdapterFactoryIetf(retryProxy)
				);
		
		this.connectionBuilder = new SocketTransportConnectionBuilder(
				TcgTProtocolEnum.PLAIN.value(), 
				TcgTVersionEnum.V1.value(),
				PtTlsWriterFactory.createProductionDefault(), 
				PtTlsReaderFactory.createProductionDefault(MAX_MSG_SIZE));
		
		this.connectionBuilder.setMessageLength(MAX_MSG_SIZE).setImMessageLength(MAX_MSG_SIZE/10);
		
		NewSessionFactory factory = new DefaultServerSessionRunnableFactory(
				PbReaderFactory.getTnccsProtocol(), 
				PbReaderFactory.getTnccsVersion(),
				this.manager,
				PbWriterFactory.createProductionDefault(), 
				PbReaderFactory.createProductionDefault()
		);
		
		this.client = new NewDefaultClient(factory, 3000);
		
		if(this.client instanceof GlobalHandshakeRetryListener){
			retryProxy.register((GlobalHandshakeRetryListener)this.client);
		}
		
		this.stopped = true;
		
		this.runner = Executors.newSingleThreadExecutor();
		this.client.start();
	}
	
	public void loadImv(List<IMV> imvs){
		// only for testing, later IMC/V are loaded and managed via configuration file.
		for (IMV imv : imvs) {
			
			try {
				this.manager.add(imv);
			} catch (ImInitializeException e) {
				LOGGER.error(e.getMessage(),e);
			}
		}
	}
	
	public void start(){
		this.stopped = false;
		this.runner.execute(new ServerRunner());
	}
	
	public synchronized void stop(){
		
		if(!stopped){
		
			this.stopped = true;
			
			this.client.notifyGlobalConnectionChange(ConnectionChangeTypeEnum.CLOSED);
			
			this.runner.shutdownNow();
			
			try {
				this.serverSocket.close();
			} catch (IOException e) {
				LOGGER.error(e.getMessage(),e);
			}
			
			this.client.stop();
			this.manager.terminate();
		}
	}
	
	private class ServerRunner implements Runnable{
	

		@Override
		public void run() {
			try{
				serverSocket = new ServerSocket(10229);
				while(!Thread.currentThread().isInterrupted()){
					LOGGER.info("Listening...");
					Socket socket = serverSocket.accept();
					
					LOGGER.info("Socket accepted " + socket.toString());
					if(socket != null){
						TransportConnection connection = connectionBuilder.toConnection(false, true, socket);
						client.notifyConnectionChange(connection, ConnectionChangeTypeEnum.NEW);
					}
				}
			}catch(IOException e){
				LOGGER.error(e.getMessage(),e);
				stop();
			}
			
		}
		
	}
	
}
