package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public interface PbMessageValue extends TnccsMessageValue{

	public boolean isOmittable();
}