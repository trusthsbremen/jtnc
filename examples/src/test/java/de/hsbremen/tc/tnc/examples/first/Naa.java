package de.hsbremen.tc.tnc.examples.first;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.ietf.nea.pb.serialize.reader.PbReaderFactory;
import org.ietf.nea.pb.serialize.writer.PbWriterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimv.IMV;

import de.hsbremen.tc.tnc.message.tnccs.enums.TcgTnccsProtocolEnum;
import de.hsbremen.tc.tnc.message.tnccs.enums.TcgTnccsVersionEnum;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImvAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.adapter.tncc.TncsAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.client.Client;
import de.hsbremen.tc.tnc.tnccs.client.DefaultClient;
import de.hsbremen.tc.tnc.tnccs.client.GlobalHandshakeRetryProxy;
import de.hsbremen.tc.tnc.tnccs.client.enums.ConnectionChangeTypeEnum;
import de.hsbremen.tc.tnc.tnccs.im.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImvManager;
import de.hsbremen.tc.tnc.tnccs.im.manager.exception.ImInitializeException;
import de.hsbremen.tc.tnc.tnccs.im.manager.simple.DefaultImvManager;
import de.hsbremen.tc.tnc.tnccs.im.route.DefaultImMessageRouter;
import de.hsbremen.tc.tnc.tnccs.session.base.SessionFactory;
import de.hsbremen.tc.tnc.tnccs.session.base.simple.DefaultServerSessionFactory;
import de.hsbremen.tc.tnc.tnccs.session.connection.simple.DefaultTnccsChannelFactory;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;
import de.hsbremen.tc.tnc.transport.example.SocketTransportConnectionBuilder;

public class Naa {
	private static final Logger LOGGER = LoggerFactory.getLogger(Naa.class);
	private Client client;
	private ImvManager manager;
	private ServerSocket serverSocket;
	private TransportConnection connection;
	
	public Naa(){
		
		GlobalHandshakeRetryProxy retryProxy = new GlobalHandshakeRetryProxy();
		
		this.manager = new DefaultImvManager(
				new DefaultImMessageRouter(),
				new ImvAdapterFactoryIetf(), 
				new TncsAdapterFactoryIetf(retryProxy)
				);
		
		SessionFactory factory = new DefaultServerSessionFactory(this.manager,
				new DefaultTnccsChannelFactory(TcgTnccsProtocolEnum.TNCCS.value(), 
						TcgTnccsVersionEnum.V2.value(), 
						PbReaderFactory.createProductionDefault(), 
						PbWriterFactory.createProductionDefault())
		);
		
		this.client = new DefaultClient(factory, 3000);
		
		if(this.client instanceof GlobalHandshakeRetryListener){
			retryProxy.register((GlobalHandshakeRetryListener)this.client);
		}
		
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
		try{
			this.serverSocket = new ServerSocket(10229);
		
			while(true){
				LOGGER.info("Listening...");
				Socket socket = this.serverSocket.accept();
				
				LOGGER.info("Socket accepted " + socket.toString());
				if(socket != null){
					SocketTransportConnectionBuilder builder = new SocketTransportConnectionBuilder(socket);
					this.connection = builder.toConnection(false);
					
					this.client.notifyConnectionChange(this.connection, ConnectionChangeTypeEnum.NEW);
				}
			}
		}catch(IOException e){
			LOGGER.error(e.getMessage(),e);
		}finally{
			this.stop();
		}
	}
	
	public void stop(){
		this.client.notifyGlobalConnectionChange(ConnectionChangeTypeEnum.CLOSED);
		
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			LOGGER.error(e.getMessage(),e);
		}
		
		this.client.stop();
		this.manager.terminate();
	}
	
}
