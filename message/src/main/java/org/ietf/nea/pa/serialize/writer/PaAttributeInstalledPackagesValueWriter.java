package org.ietf.nea.pa.serialize.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttributeValueInstalledPackages;
import org.ietf.nea.pa.attribute.util.PackageEntry;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.m.serialize.ImWriter;

class PaAttributeInstalledPackagesValueWriter implements ImWriter<PaAttributeValueInstalledPackages>{

	private static final byte[] RESERVED = new byte[]{0,0}; 
	
	@Override
	public void write(final PaAttributeValueInstalledPackages data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		PaAttributeValueInstalledPackages aValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* Reserved 16 bit(s) */
		try {
			buffer.write(RESERVED);
		} catch (IOException e) {
			throw new SerializationException("Reserved space could not be written to the buffer.",e,false);
		}
		
		/* package count 16 bit(s) */
		byte[] pkgCount = Arrays.copyOfRange(ByteBuffer.allocate(4).putInt(aValue.getPackageCount()).array(),2,4);
		try {
			buffer.write(pkgCount);
		} catch (IOException e) {
			throw new SerializationException(
					"Package count could not be written to the buffer.", e, false,
					Integer.toString(aValue.getPackageCount()));
		}
		
		List<PackageEntry> entries = aValue.getPackages();
		if(entries != null && entries.size() > 0){
			for (PackageEntry entry : entries) {

				/* package name length 8 bit(s)*/
				buffer.write(entry.getPackageNameLength());
				
				/* package name String */
				try{
					buffer.write(entry.getPackageName().getBytes(Charset.forName("UTF-8")));
				} catch (IOException e) {
					throw new SerializationException(
							"Package name could not be written to the buffer.", e, false,
							entry.getPackageName());
				}
				
				/* version length 8 bit(s)*/
				buffer.write(entry.getPackageVersionLength());
				
				/* version String */
				try{
					buffer.write(entry.getPackageVersion().getBytes(Charset.forName("UTF-8")));
				} catch (IOException e) {
					throw new SerializationException(
							"Package version could not be written to the buffer.", e, false,
							entry.getPackageVersion());
				}
			}
		}
		
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Attribute could not be written to the OutputStream.",e, true);
		}
	}

}
