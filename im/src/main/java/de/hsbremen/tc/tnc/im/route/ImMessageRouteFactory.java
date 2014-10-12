package de.hsbremen.tc.tnc.im.route;

import de.hsbremen.tc.tnc.im.module.ImModule;

public class ImMessageRouteFactory {

	public <T> ImMessageRouteComponent<ImModule<T>> createRoutingTable(){
		return new ImMessageRoute<T>();
	}
	
}
