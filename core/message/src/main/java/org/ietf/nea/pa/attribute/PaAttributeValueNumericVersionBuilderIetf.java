/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Carl-Heinz Genzel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
package org.ietf.nea.pa.attribute;

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

public class PaAttributeValueNumericVersionBuilderIetf implements
	PaAttributeValueNumericVersionBuilder {

	private long length;
	private long majorVersion;
	private long minorVersion;
	private long buildVersion;
	private int servicePackMajor;
	private int servicePackMinor;
	
	public PaAttributeValueNumericVersionBuilderIetf(){
		this.length = PaAttributeTlvFixedLengthEnum.NUM_VER.length();
		this.majorVersion = 0L;
		this.minorVersion = 0L;
		this.buildVersion = 0L;
		this.servicePackMajor = 0;
		this.servicePackMinor = 0;
	}

	@Override
	public void setMajorVersion(long majorVersion) {
		this.majorVersion = majorVersion;
	}

	@Override
	public void setMinorVersion(long minorVersion) {
		this.minorVersion = minorVersion;
	}

	@Override
	public void setBuildVersion(long buildVersion) {
		this.buildVersion = buildVersion;
	}

	@Override
	public void setServicePackMajor(int servicePackMajor) {
		this.servicePackMajor = servicePackMajor;
	}

	@Override
	public void setServicePackMinor(int servicePackMinor) {
		this.servicePackMinor = servicePackMinor;
	}

	@Override
	public PaAttributeValueNumericVersion toObject(){
		
		return new PaAttributeValueNumericVersion(this.length, this.majorVersion, this.minorVersion, this.buildVersion, this.servicePackMajor, this.servicePackMinor);
	}

	@Override
	public PaAttributeValueNumericVersionBuilder newInstance() {
		return new PaAttributeValueNumericVersionBuilderIetf();
	}

}
