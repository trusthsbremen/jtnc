package de.hsbremen.tc.tnc.im.evaluate.example.os;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;

import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.im.AbstractDummy;
import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.im.adapter.ImConnectionStateEnum;
import de.hsbremen.tc.tnc.im.adapter.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.im.adapter.tncc.TnccAdapter;
import de.hsbremen.tc.tnc.im.module.SupportedMessageType;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;

public class Dummy extends AbstractDummy{

	public static ImSessionContext getSessionContext(){
		return new ImSessionContext() {
			
			@Override
			public void requestConnectionHandshakeRetry(
					ImHandshakeRetryReasonEnum reason) {
				System.out.println("Connection handshake retry requested.");
				
			}
			
			@Override
			public ImConnectionStateEnum getConnectionState() {
				return ImConnectionStateEnum.HSB_CONNECTION_STATE_UNKNOWN;
			}
		};
	}
	
	public static GlobalHandshakeRetryListener getHandshakeListener(){
		
		return new GlobalHandshakeRetryListener() {
			
			@Override
			public void requestGlobalHandshakeRetry(ImHandshakeRetryReasonEnum reason)
					throws TNCException {
				System.out.println("Globale handshake retry requested.");
				
			}
		};
		
	}
	
	public static TnccAdapter getTnccAdapter(){
		
		return new TnccAdapter() {
			
			private long i = new Random().nextInt(100);
			
			private GlobalHandshakeRetryListener listener = new GlobalHandshakeRetryListener() {
				
				@Override
				public void requestGlobalHandshakeRetry(ImHandshakeRetryReasonEnum reason)
						throws TNCException {
					System.out.println("Globale handshake retry requested.");
					
				}
			};
			
			@Override
			public long reserveAdditionalId() throws TNCException {
				
				return ++i;
			}
			
			@Override
			public void reportMessageTypes(Set<SupportedMessageType> supportedTypes)
					throws TNCException {
				System.out.println(Arrays.toString(supportedTypes.toArray()));
				
			}

			@Override
			public GlobalHandshakeRetryListener getHandshakeRetryListener() {
				// TODO Auto-generated method stub
				return listener;
			}
		};
		
	}
}
