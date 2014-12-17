package org.ietf.nea.pa.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.nio.charset.Charset;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttributeValueInstalledPackages;
import org.ietf.nea.pa.attribute.util.PackageEntry;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeInstalledPackagesValueWriter implements ImWriter<PaAttributeValueInstalledPackages>{

	private static final byte[] RESERVED = new byte[]{0,0}; 
	
	@Override
	public void write(final PaAttributeValueInstalledPackages data, final ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		if(buffer == null){
			throw new NullPointerException("Buffer cannot be NULL.");
		}
		
		PaAttributeValueInstalledPackages aValue = data;
		
		try{
			/* reserved 16 bit(s) */
			buffer.write(RESERVED);
		
			/* package count 16 bit(s) */
			buffer.writeUnsignedShort(aValue.getPackageCount());
			
			List<PackageEntry> entries = aValue.getPackages();
			if(entries != null && entries.size() > 0){
				
				for (PackageEntry entry : entries) {
					
					/* package name length 8 bit(s)*/
					buffer.writeUnsignedByte(entry.getPackageNameLength());
					
					/* package name String */
					buffer.write(entry.getPackageName().getBytes(Charset.forName("UTF-8")));
					
					/* version length 8 bit(s)*/
					buffer.writeUnsignedByte(entry.getPackageVersionLength());
					
					/* package version String */
					buffer.write(entry.getPackageVersion().getBytes(Charset.forName("UTF-8")));
					
				}
			}
			
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
	}

}
