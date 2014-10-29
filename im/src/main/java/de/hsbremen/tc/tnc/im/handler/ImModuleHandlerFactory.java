package de.hsbremen.tc.tnc.im.handler;

import java.util.List;

import org.trustedcomputinggroup.tnc.ifimc.IMC;

import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapterBuilder;
import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapterBuilderIetf;
import de.hsbremen.tc.tnc.im.command.ImcFunctionCallBuilder;
import de.hsbremen.tc.tnc.im.command.ImcFunctionCallBuilderIetf;
import de.hsbremen.tc.tnc.im.module.ImModule;
import de.hsbremen.tc.tnc.im.route.ImMessageRouteComponent;
import de.hsbremen.tc.tnc.im.route.ImMessageRouteFactory;

public class ImModuleHandlerFactory {

	private static final long DEFAULT_TIMEOUT = 500;
	
	public static ImModuleHandler<IMC> createImcModuleHandlerIetf(List<ImModule<IMC>> imcs){
		
		ImMessageRouteComponent<ImModule<IMC>> router = new ImMessageRouteFactory().createRoutingTable();
		ImConnectionMessageQueue queue 				  = new ImConnectionMessageQueueIetf();
		ImcConnectionAdapterBuilder connectionBuilder = new ImcConnectionAdapterBuilderIetf(); 
		ImcFunctionCallBuilder functionBuilder 		  = new ImcFunctionCallBuilderIetf(DEFAULT_TIMEOUT);
		
		ImModuleHandler<IMC> modulHandler = new ImcModuleHandlerIetf(queue,router,connectionBuilder,functionBuilder);
		return modulHandler;
	}
	
}
