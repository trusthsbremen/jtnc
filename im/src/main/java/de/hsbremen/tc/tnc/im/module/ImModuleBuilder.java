package de.hsbremen.tc.tnc.im.module;


public interface ImModuleBuilder<T> {

	public ImModule<T> createImModule(long primaryId, T imc);

}
