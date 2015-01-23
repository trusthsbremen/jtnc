package de.hsbremen.tc.tnc.im.session;

public interface ImSessionManager<K, V> {

	public abstract V getSession(K connection);

	public abstract V putSession(K connection, V session);

	public abstract void initialize();

	public abstract void terminate();

}