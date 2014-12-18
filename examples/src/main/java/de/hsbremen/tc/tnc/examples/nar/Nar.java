package de.hsbremen.tc.tnc.examples.nar;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import org.ietf.nea.pb.serialize.reader.bytebuffer.PbReaderFactory;
import org.ietf.nea.pb.serialize.writer.bytebuffer.PbWriterFactory;
import org.ietf.nea.pt.serialize.reader.bytebuffer.PtTlsReaderFactory;
import org.ietf.nea.pt.serialize.writer.bytebuffer.PtTlsWriterFactory;
import org.ietf.nea.pt.socket.SocketTransportConnectionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.IMC;

import de.hsbremen.tc.tnc.message.t.enums.TcgTProtocolEnum;
import de.hsbremen.tc.tnc.message.t.enums.TcgTVersionEnum;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImcAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.adapter.tncc.TnccAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.client.GlobalHandshakeRetryProxy;
import de.hsbremen.tc.tnc.tnccs.client.ClientFacade;
import de.hsbremen.tc.tnc.tnccs.client.DefaultClientFacade;
import de.hsbremen.tc.tnc.tnccs.client.enums.ConnectionChangeTypeEnum;
import de.hsbremen.tc.tnc.tnccs.im.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImcManager;
import de.hsbremen.tc.tnc.tnccs.im.manager.exception.ImInitializeException;
import de.hsbremen.tc.tnc.tnccs.im.manager.simple.DefaultImcManager;
import de.hsbremen.tc.tnc.tnccs.im.route.DefaultImMessageRouter;
import de.hsbremen.tc.tnc.tnccs.session.base.SessionFactory;
import de.hsbremen.tc.tnc.tnccs.session.base.simple.DefaultClientSessionFactory;
import de.hsbremen.tc.tnc.transport.TransportConnection;

public class Nar {

	private static final Logger LOGGER = LoggerFactory.getLogger(Nar.class);
	private static final long MAX_MSG_SIZE = 131072;
	
	private ClientFacade client;
	private ImcManager manager;
	private Socket socket;
	private TransportConnection connection;
	
	public Nar(){
		
		GlobalHandshakeRetryProxy retryProxy = new GlobalHandshakeRetryProxy();
		
		this.manager = new DefaultImcManager(
				new DefaultImMessageRouter(),
				new ImcAdapterFactoryIetf(), 
				new TnccAdapterFactoryIetf(retryProxy)
				);
		
		SessionFactory factory = new DefaultClientSessionFactory(
				PbReaderFactory.getTnccsProtocol(),
				PbReaderFactory.getTnccsVersion(),
				this.manager,
				PbWriterFactory.createProductionDefault(), 
				PbReaderFactory.createProductionDefault()
		);
		
		this.client = new DefaultClientFacade(factory, 3000);
		
		if(this.client instanceof GlobalHandshakeRetryListener){
			retryProxy.register((GlobalHandshakeRetryListener)this.client);
		}
		
		this.client.start();
	}
	
	public void loadImc(List<IMC> imcs){
		// only for testing, later IMC/V are loaded and managed via configuration file.
		for (IMC imc : imcs) {
			
			try {
				this.manager.add(imc);
			} catch (ImInitializeException e) {
				LOGGER.error(e.getMessage(),e);
			}
		}
	}
	
	public void startHandshake() throws UnknownHostException, IOException{
		this.socket = new Socket("localhost", 10229);
		SocketTransportConnectionBuilder builder = new SocketTransportConnectionBuilder(
				TcgTProtocolEnum.PLAIN.value(), 
				TcgTVersionEnum.V1.value(),
				PtTlsWriterFactory.createProductionDefault(), 
				PtTlsReaderFactory.createProductionDefault(MAX_MSG_SIZE));
		
		this.connection = builder.toConnection(true, false, socket);
		this.client.notifyConnectionChange(connection, ConnectionChangeTypeEnum.NEW);
	}
	
	public void stopHandshake() throws IOException{
		this.client.notifyConnectionChange(this.connection, ConnectionChangeTypeEnum.CLOSED);
		this.socket.close();
	}
	
	public void stop(){
		this.client.stop();
		this.manager.terminate();
	}
	
}
