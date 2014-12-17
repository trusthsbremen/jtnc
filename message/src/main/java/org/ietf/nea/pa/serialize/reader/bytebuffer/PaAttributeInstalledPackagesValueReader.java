package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttributeValueInstalledPackages;
import org.ietf.nea.pa.attribute.PaAttributeValueInstalledPackagesBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.PackageEntry;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeInstalledPackagesValueReader implements ImReader<PaAttributeValueInstalledPackages>{

	private PaAttributeValueInstalledPackagesBuilder baseBuilder;
	
	PaAttributeInstalledPackagesValueReader(PaAttributeValueInstalledPackagesBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PaAttributeValueInstalledPackages read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PaAttributeValueInstalledPackages value = null;
		PaAttributeValueInstalledPackagesBuilder builder = (PaAttributeValueInstalledPackagesBuilder)baseBuilder.newInstance();

		try{
			
			try{
				
				/* ignore reserved */
				errorOffset = buffer.bytesRead();
				buffer.read(2);
				
				/* pkg count */
				errorOffset = buffer.bytesRead();
				int pkgCount = buffer.readInt((byte)2);
				
				long length = messageLength - PaAttributeTlvFixedLengthEnum.INS_PKG.length();
				
				long counter = 0;
				while(pkgCount > 0 && (length - counter) > 0){
					// TODO error offset is vague because it cannot be calculated to the exact offset.  
					errorOffset = buffer.bytesRead();
					
					int nameLength = buffer.readInt((byte)1);
					counter++;
					
					byte[] pnData = buffer.read(nameLength);
					String packageName = new String(pnData, Charset.forName("UTF-8"));
					counter += nameLength;

					int versionLength = buffer.readInt((byte)1);
					counter++;
					
					byte[] pvData = buffer.read(versionLength);
					String packageVersion = new String(pvData, Charset.forName("UTF-8"));
					counter += versionLength;
					
					builder.addPackages(new PackageEntry(packageName, packageVersion));

					--pkgCount;
				};

			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			value = (PaAttributeValueInstalledPackages)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}
	
	@Override
	public byte getMinDataLength() {
	
		return PaAttributeTlvFixedLengthEnum.PORT_FT.length(); 
	}
}
