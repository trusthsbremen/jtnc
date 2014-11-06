package de.hsbremen.tc.tnc.imhandler.route;

import de.hsbremen.tc.tnc.imhandler.module.TnccsImModuleHolder;

public class ImMessageRouteFactory {

	public <T> ImMessageRouteComponent<TnccsImModuleHolder<T>> createRoutingTable(){
		return new ImMessageRoute<T>();
	}
	
}
