package org.ietf.nea.pa.serialize.reader.stream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttributeValueInstalledPackages;
import org.ietf.nea.pa.attribute.PaAttributeValueInstalledPackagesBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.PackageEntry;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.stream.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteArrayHelper;

class PaAttributeInstalledPackagesValueReader implements ImReader<PaAttributeValueInstalledPackages>{

	private PaAttributeValueInstalledPackagesBuilder baseBuilder;
	
	PaAttributeInstalledPackagesValueReader(PaAttributeValueInstalledPackagesBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PaAttributeValueInstalledPackages read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PaAttributeValueInstalledPackages value = null;
		PaAttributeValueInstalledPackagesBuilder builder = (PaAttributeValueInstalledPackagesBuilder)baseBuilder.newInstance();

		try{
			
			try{
				int byteSize = 0;
				byte[] buffer = new byte[byteSize];
				
				/* ignore reserved */
				byteSize = 2;
				ByteArrayHelper.arrayFromStream(in, byteSize);
				errorOffset += byteSize;
				
				/* pkg count */
				int pkgCount = 0;
				
				byteSize = 2;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				pkgCount = ByteArrayHelper.toInt(buffer);
				errorOffset += byteSize;
				
				while(pkgCount > 0 && (messageLength - errorOffset) > 0){
					
					long count = 0;
					
					byteSize = 1;
					buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
					long nameLength = ByteArrayHelper.toLong(buffer);
					count += byteSize;
					
					String packageName = this.readString(nameLength, in, Charset.forName("UTF-8"));
					count += nameLength;
					
					byteSize = 1;
					buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
					long versionLength = ByteArrayHelper.toLong(buffer);
					count += byteSize;
					
					String packageVersion = this.readString(versionLength, in, Charset.forName("UTF-8"));
					count += versionLength;
					
					builder.addPackages(new PackageEntry(packageName, packageVersion));
					errorOffset += count;
					
					// TODO error offset is vague because it cannot be calculated to the exact offset.  
					
					--pkgCount;
					
				};

			}catch (IOException e){
				throw new SerializationException("Returned data for attribute value is to short or stream may be closed.",e,true);
			}

			value = (PaAttributeValueInstalledPackages)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	private String readString(final long length, final InputStream in, final Charset charset) throws IOException{
		
		String returnValue = "";

		byte[] buffer = new byte[0];
		byte[] temp = new byte[0];
		
		for(long l = length; l > 0; l -= buffer.length){

			buffer = ByteArrayHelper.arrayFromStream(in, ((l < 65535) ? (int)l : 65535));
			temp = ByteArrayHelper.mergeArrays(temp, Arrays.copyOfRange(buffer,0, buffer.length));

		}
		
		if(temp != null && temp.length > 0){
			returnValue = new String(temp, charset);
		}
		
		return returnValue;
	}
	
	@Override
	public byte getMinDataLength() {
	
		return PaAttributeTlvFixedLengthEnum.PORT_FT.length(); 
	}
}
