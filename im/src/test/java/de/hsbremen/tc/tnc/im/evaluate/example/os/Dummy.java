package de.hsbremen.tc.tnc.im.evaluate.example.os;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ietf.nea.pa.attribute.PaAttribute;
import org.ietf.nea.pa.attribute.PaAttributeFactoryIetf;

import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.connection.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.connection.ImTncConnectionStateEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.im.AbstractDummy;
import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.im.adapter.tncc.TnccAdapter;
import de.hsbremen.tc.tnc.im.adapter.tncs.TncsAdapter;
import de.hsbremen.tc.tnc.im.evaluate.example.os.exception.PatternNotFoundException;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.natives.CLibrary;
import de.hsbremen.tc.tnc.natives.CLibrary.UTSNAME;
import de.hsbremen.tc.tnc.report.SupportedMessageType;

public class Dummy extends AbstractDummy{

	public static ImSessionContext getSessionContext(){
		return new ImSessionContext() {
			
			@Override
			public void requestConnectionHandshakeRetry(
					ImHandshakeRetryReasonEnum reason) {
				System.out.println("Connection handshake retry requested.");
				
			}
			
			@Override
			public ImTncConnectionStateEnum getConnectionState() {
				return ImTncConnectionStateEnum.HSB_CONNECTION_STATE_UNKNOWN;
			}

			@Override
			public Object getAttribute(TncAttributeType type) {
				throw new UnsupportedOperationException("Operation is in dummy not supported.");
			}
		};
	}
	
	public static GlobalHandshakeRetryListener getHandshakeListener(){
		
		return new GlobalHandshakeRetryListener() {
			
			@Override
			public void requestGlobalHandshakeRetry(ImHandshakeRetryReasonEnum reason)
					throws TncException {
				System.out.println("Globale handshake retry requested.");
				
			}
		};
		
	}
	
	public static TnccAdapter getTnccAdapter(){
		
		return new TnccAdapter() {
			
			private long i = new Random().nextInt(100);
			
			private GlobalHandshakeRetryListener listener = getHandshakeListener();
			
			@Override
			public long reserveAdditionalId() throws TncException {
				
				return ++i;
			}
			
			@Override
			public void reportMessageTypes(Set<SupportedMessageType> supportedTypes)
					throws TncException {
				System.out.println(Arrays.toString(supportedTypes.toArray()));
				
			}

			@Override
			public GlobalHandshakeRetryListener getHandshakeRetryListener() {
				// TODO Auto-generated method stub
				return listener;
			}
		};
		
	}
	
	public static TncsAdapter getTncsAdapter(){
		
		return new TncsAdapter() {
			
			private long i = new Random().nextInt(100);
			
			private GlobalHandshakeRetryListener listener = getHandshakeListener();
			
			@Override
			public long reserveAdditionalId() throws TncException {
				
				return ++i;
			}
			
			@Override
			public void reportMessageTypes(Set<SupportedMessageType> supportedTypes)
					throws TncException {
				System.out.println(Arrays.toString(supportedTypes.toArray()));
				
			}

			@Override
			public GlobalHandshakeRetryListener getHandshakeRetryListener() {
				// TODO Auto-generated method stub
				return listener;
			}
		};
		
	}
	

	public static PaAttribute getAttributeStringVersion() throws ValidationException {
		UTSNAME systemDescription = new UTSNAME();
		CLibrary.INSTANCE.uname(systemDescription);
		return PaAttributeFactoryIetf.createStringVersion(new String(systemDescription.release).trim(),null,new String(systemDescription.machine).trim());
	}

	public static PaAttribute getAttributeNumericVersion() throws NumberFormatException, ValidationException, PatternNotFoundException {
		UTSNAME systemDescription = new UTSNAME();
		CLibrary.INSTANCE.uname(systemDescription);
		String release = new String(systemDescription.release).trim();
		Pattern p = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)-(\\d+)");
		Matcher m = p.matcher(release);
		if(m.find()){
			long majorVersion = Long.parseLong(m.group(1));
			long minorVersion = Long.parseLong(m.group(2));
			long buildVersion = Long.parseLong(m.group(3));
			int servicePackVersion =  Integer.parseInt(m.group(4));
			int servicePackVersionMinor = 0;
			return PaAttributeFactoryIetf.createNumericVersion(majorVersion,minorVersion,buildVersion,servicePackVersion, servicePackVersionMinor);
		}else{
			throw new PatternNotFoundException("Version pattern " + p.toString() +" was not found.", release, p.toString());
		}
	}

	public static PaAttribute getAttributeProductInformation() throws ValidationException {
		UTSNAME systemDescription = new UTSNAME();
		CLibrary.INSTANCE.uname(systemDescription);
		// RFC 5792 Vendor ID unknown = 0 => Product ID  = 0
		return PaAttributeFactoryIetf.createProductInformation(0,0, new String(systemDescription.version).trim());
	}
}
