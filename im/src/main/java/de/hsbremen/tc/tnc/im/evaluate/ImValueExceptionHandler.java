package de.hsbremen.tc.tnc.im.evaluate;

import java.util.List;

import de.hsbremen.tc.tnc.im.adapter.data.ImFaultyObjectComponent;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;

public interface ImValueExceptionHandler {

	List<ImAttribute> handle(ImFaultyObjectComponent component);

}
