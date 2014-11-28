package de.hsbremen.tc.tnc.imhandler.handler;

import java.util.List;

import org.trustedcomputinggroup.tnc.ifimc.IMC;

import de.hsbremen.tc.tnc.imhandler.command.ImcFunctionCallBuilder;
import de.hsbremen.tc.tnc.imhandler.command.ImcFunctionCallBuilderIetf;
import de.hsbremen.tc.tnc.imhandler.module.TnccsImModuleHolder;
import de.hsbremen.tc.tnc.newp.adapter.connection.ImcConnectionAdapterBuilder;
import de.hsbremen.tc.tnc.newp.adapter.connection.ImcConnectionAdapterBuilderIetf;
import de.hsbremen.tc.tnc.newp.route.ImMessageRouteComponent;
import de.hsbremen.tc.tnc.newp.route.ImMessageRouterFactory;

public class ImModuleHandlerFactory {

	private static final long DEFAULT_TIMEOUT = 500;
	
	public static ImModuleHandler<IMC> createImcModuleHandlerIetf(List<TnccsImModuleHolder<IMC>> imcs){
		
		ImMessageRouteComponent<TnccsImModuleHolder<IMC>> router = new ImMessageRouterFactory().createRoutingTable();
		ImConnectionMessageQueue queue 				  = new ImConnectionMessageQueueIetf();
		ImcConnectionAdapterBuilder connectionBuilder = new ImcConnectionAdapterBuilderIetf(); 
		ImcFunctionCallBuilder functionBuilder 		  = new ImcFunctionCallBuilderIetf(DEFAULT_TIMEOUT);
		
		ImModuleHandler<IMC> modulHandler = new ImcModuleHandlerIetf(queue,router,connectionBuilder,functionBuilder);
		return modulHandler;
	}
	
}
