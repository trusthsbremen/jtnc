package org.ietf.nea.pb.batch;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatchHeaderBuilder;

public interface PbBatchHeaderBuilder extends TnccsBatchHeaderBuilder {

	public abstract PbBatchHeaderBuilder setVersion(short version)
			throws RuleException;

	public abstract PbBatchHeaderBuilder setDirection(byte direction)
			throws RuleException;

	public abstract PbBatchHeaderBuilder setType(byte type)
			throws RuleException;

	public abstract PbBatchHeaderBuilder setLength(long length)
			throws RuleException;

}