package de.hsbremen.tc.tnc.example.nar;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.translate.MessageContentTranslatorFactoryBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.nar.connection.IfTConnection;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsMessageTranslator;

public class PlainIfTConnection implements IfTConnection {

	private static final Logger LOGGER = LoggerFactory.getLogger(PlainIfTConnection.class);
	private final String host;
	private final int port;
	private Socket s;
	private final boolean selfInitiated;
	private TnccsMessageTranslator<PbBatch, byte[]> translator = MessageContentTranslatorFactoryBatch.createByteTranslator();
	
	
	public PlainIfTConnection(String host, int port){
		host = (host != null) ? host : "127.0.0.1"; 
		this.host = host;
		this.port = port;
		this.selfInitiated = true;
	}
	
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSelfInititated() {
		
		return selfInitiated;
	}

	@Override
	public boolean isOpen() {
		if(s == null) return false;
		return (s.isConnected() && !s.isClosed());
	}

	@Override
	public void open() {
		try {
			s.connect(new InetSocketAddress(this.host, this.port));
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}

	}

	@Override
	public void close() {
		if(s != null){
			try {
				s.close();
			} catch (IOException e) {
				LOGGER.error(e.getMessage());
			}
		}

	}

	@Override
	public void send(TnccsBatch data) {
		byte[] b = translator.encode((PbBatch) data);
		try {
			s.getOutputStream().write(b);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}

	}

	@Override
	public TnccsBatch receive() {
		byte[] buffer = new byte[1024];
		List<byte[]> chunks = new ArrayList<>();
		
		int bytesRead = 0;
		try {
			InputStream in = s.getInputStream();
		
			do{
			
				bytesRead = s.getInputStream().read(buffer);
				
				chunks.add(Arrays.copyOf(buffer, bytesRead));
				Arrays.fill(buffer, (byte)0);
			}while(in.available() > 0 && bytesRead != -1);
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage() + "\n Getting last bytes and stop reading.");
			if(bytesRead > 0){
				chunks.add(Arrays.copyOf(buffer, bytesRead));
				Arrays.fill(buffer, (byte)0);
			}
		}
		
		byte[] b = new byte[0];
		byte[] temp;
		for (byte[] c : chunks) {
			temp = new byte[b.length];
			System.arraycopy(b, 0, temp, 0, b.length);
			
			b = new byte[temp.length+c.length];
			System.arraycopy(temp, 0, b, 0, temp.length);
			System.arraycopy(c, 0, b, temp.length, c.length);
		}
		
		PbBatch batch = translator.decode(b);
		
		return batch;
	}

}
