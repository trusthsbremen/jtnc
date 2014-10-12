package de.hsbremen.tc.tnc.im.handler;

import java.util.List;

import org.trustedcomputinggroup.tnc.ifimc.IMC;

import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapterBuilderIetf;
import de.hsbremen.tc.tnc.im.command.ImcFunctionCallBuilderIetf;
import de.hsbremen.tc.tnc.im.module.ImModule;
import de.hsbremen.tc.tnc.im.route.ImMessageRouteFactory;

public class ImModuleHandlerFactory {

	private static final long DEFAULT_TIMEOUT = 500;
	
	public static ImModuleHandler<IMC> createImcModuleHandlerIetf(List<ImModule<IMC>> imcs){
		
		ImModuleHandler<IMC> modulHandler = new ImcModuleHandlerIetf(new ImcConnectionAdapterBuilderIetf(), new ImMessageRouteFactory(), new ImcFunctionCallBuilderIetf(DEFAULT_TIMEOUT));
		return modulHandler;
	}
	
}
