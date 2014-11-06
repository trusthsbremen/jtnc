package de.hsbremen.tc.tnc.imhandler.module;



public interface TnccsImModuleHolderBuilder<T> {

	public TnccsImModuleHolder<T> createImModule(long primaryId, T imc);

}
