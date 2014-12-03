package de.hsbremen.tc.tnc.session.base;

import de.hsbremen.tc.tnc.adapter.connection.DefaultImcConnectionAdapterFactory;
import de.hsbremen.tc.tnc.adapter.connection.DefaultImcConnectionContext;
import de.hsbremen.tc.tnc.adapter.connection.ImcConnectionAdapterFactory;
import de.hsbremen.tc.tnc.adapter.connection.ImcConnectionContext;
import de.hsbremen.tc.tnc.adapter.im.ImcAdapter;
import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.newp.handler.DefaultImcHandler;
import de.hsbremen.tc.tnc.newp.handler.DefaultTnccHandler;
import de.hsbremen.tc.tnc.newp.handler.DefaultValueExceptionHandler;
import de.hsbremen.tc.tnc.newp.handler.ImcHandler;
import de.hsbremen.tc.tnc.newp.handler.TnccHandler;
import de.hsbremen.tc.tnc.newp.handler.TnccsValueExceptionHandler;
import de.hsbremen.tc.tnc.newp.manager.ImAdapterManager;
import de.hsbremen.tc.tnc.session.connection.BatchSender;
import de.hsbremen.tc.tnc.session.connection.DefaultBatchSender;
import de.hsbremen.tc.tnc.session.connection.DefaultTnccsInputChannel;
import de.hsbremen.tc.tnc.session.connection.DefaultTnccsOutputChannel;
import de.hsbremen.tc.tnc.session.connection.TnccsInputChannel;
import de.hsbremen.tc.tnc.session.connection.TnccsOutputChannel;
import de.hsbremen.tc.tnc.tnccs.TcgTnccsProtocolEnum;
import de.hsbremen.tc.tnc.tnccs.TcgTnccsVersionEnum;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsReader;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsWriter;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;

public class DefaultSessionFactory {

	private final ImAdapterManager<ImcAdapter> adapterManager;
	private final TnccsReader<? extends TnccsBatchContainer> reader;
	private final TnccsWriter<? super TnccsBatch> writer;
	
	public DefaultSessionFactory(ImAdapterManager<ImcAdapter> adapterManager, TnccsReader<? extends TnccsBatchContainer> reader, TnccsWriter<? super TnccsBatch> writer){
		this.adapterManager = adapterManager;
		this.reader = reader;
		this.writer = writer;
	}
	
	public SessionBase createTnccSession(TransportConnection connection){
		
		AttributeCollection attributes = new AttributeCollection();
		attributes.add(new SessionAttributes(TcgTnccsProtocolEnum.TNCCS.value(), TcgTnccsVersionEnum.V2.value()));
		Attributed connectionAttributes = connection.getAttributes();
		if(connectionAttributes != null){
			attributes.add(connectionAttributes);
		}
		
		TnccsOutputChannel outChannel = new DefaultTnccsOutputChannel(connection, writer); 
		BatchSender out = new DefaultBatchSender();
		out.register(outChannel);
		
		TnccsInputChannel in = new DefaultTnccsInputChannel(connection, reader);

		
		Session s = new Session();
		
		
		ImcConnectionContext connectionContext = new DefaultImcConnectionContext(attributes,s);
		ImcConnectionAdapterFactory connectionFactory = new DefaultImcConnectionAdapterFactory(connectionContext);
		
		ImcHandler imcHandler = new DefaultImcHandler(adapterManager,connectionFactory , adapterManager.getRouter());
		TnccHandler tnccHandler = new DefaultTnccHandler();
		TnccsValueExceptionHandler exceptionHandler = new DefaultValueExceptionHandler();
		
		StateContext stateContext = new DefaultStateContext(imcHandler, tnccHandler, exceptionHandler, connectionContext);
		
		StateMachine machine = new TnccsStateMachine(stateContext);
		
		// finalize session and run
		s.registerStatemachine(machine);
		s.registerInput(in);
		s.registerOutput(out);
		s.start(connection.isSelfInititated());

		
		return s;
		
	}
	
	
	
}
