package org.ietf.nea.pa.serialize.writer.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.ietf.nea.pa.attribute.PaAttributeValueNumericVersion;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.stream.ImWriter;

class PaAttributeNumericVersionValueWriter implements ImWriter<PaAttributeValueNumericVersion>{

	@Override
	public void write(final PaAttributeValueNumericVersion data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		PaAttributeValueNumericVersion aValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* major version 32 bit(s) */
		byte[] major = Arrays.copyOfRange(ByteBuffer.allocate(8).putLong(aValue.getMajorVersion()).array(),4,8);
		try {
			buffer.write(major);
		} catch (IOException e) {
			throw new SerializationException(
					"Major version could not be written to the buffer.", e, false,
					Long.toString(aValue.getMajorVersion()));
		}
		
		/* minor version 32 bit(s) */
		byte[] minor = Arrays.copyOfRange(ByteBuffer.allocate(8).putLong(aValue.getMinorVersion()).array(),4,8);
		try {
			buffer.write(minor);
		} catch (IOException e) {
			throw new SerializationException(
					"Minor version could not be written to the buffer.", e, false,
					Long.toString(aValue.getMinorVersion()));
		}
		
		/* build number 32 bit(s) */
		byte[] build = Arrays.copyOfRange(ByteBuffer.allocate(8).putLong(aValue.getBuildVersion()).array(),4,8);
		try {
			buffer.write(build);
		} catch (IOException e) {
			throw new SerializationException(
					"Build number could not be written to the buffer.", e, false,
					Long.toString(aValue.getBuildVersion()));
		}
		
		/* service pack major 16 bit(s) */
		byte[] svcPackMajor = Arrays.copyOfRange(ByteBuffer.allocate(4).putInt(aValue.getServicePackMajor()).array(),2,4);
		try {
			buffer.write(svcPackMajor);
		} catch (IOException e) {
			throw new SerializationException(
					"Service pack major could not be written to the buffer.", e, false,
					Integer.toString(aValue.getServicePackMajor()));
		}
		
		/* service pack minor 16 bit(s) */
		byte[] svcPackMinor = Arrays.copyOfRange(ByteBuffer.allocate(4).putInt(aValue.getServicePackMinor()).array(),2,4);
		try {
			buffer.write(svcPackMinor);
		} catch (IOException e) {
			throw new SerializationException(
					"Service pack minor could not be written to the buffer.", e, false,
					Integer.toString(aValue.getServicePackMinor()));
		}
		
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Attribute could not be written to the OutputStream.",e, true);
		}
	}

}
