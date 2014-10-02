package org.ietf.nea.pb.batch;

import org.ietf.nea.pb.exception.RuleException;

public interface PbBatchHeaderBuilder {

	public abstract PbBatchHeaderBuilder setVersion(byte version)
			throws RuleException;

	public abstract PbBatchHeaderBuilder setDirection(byte direction)
			throws RuleException;

	public abstract PbBatchHeaderBuilder setType(byte type)
			throws RuleException;

	public abstract PbBatchHeaderBuilder setLength(long length)
			throws RuleException;

	public abstract PbBatchHeader toBatchHeader() throws RuleException;

	public abstract PbBatchHeaderBuilder clear();

}