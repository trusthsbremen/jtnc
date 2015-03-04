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
package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

public class PaAttributeValueErrorInformationInvalidParamBuilderIetf implements
		PaAttributeValueErrorInformationInvalidParamBuilder {
	
	
	private long length;
	private MessageHeaderDump messageHeader;
	private long offset;
	
	public PaAttributeValueErrorInformationInvalidParamBuilderIetf(){
		this.length = PaAttributeTlvFixedLengthEnum.ERR_INF.length() + PaAttributeTlvFixedLengthEnum.MESSAGE.length() + 4; // 4 = offset
		this.messageHeader = new MessageHeaderDump((short)0, new byte[0], 0L);
		this.offset = 0;
	}

	@Override
	public void setMessageHeader(MessageHeaderDump messageHeader) {
		if(messageHeader != null){
			this.messageHeader = messageHeader;
		}
	}

	@Override
	public void setOffset(long offset) {
		this.offset = offset;
	}

	@Override
	public PaAttributeValueErrorInformationInvalidParam toObject(){
		
		return new PaAttributeValueErrorInformationInvalidParam(length, messageHeader, offset);
	}

	@Override
	public PaAttributeValueErrorInformationInvalidParamBuilder newInstance() {
		// TODO Auto-generated method stub
		return new PaAttributeValueErrorInformationInvalidParamBuilderIetf();
	}

}
