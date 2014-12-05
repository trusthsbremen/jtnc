package de.hsbremen.tc.tnc.tnccs.im.loader;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class JarLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(JarLoader.class);
	
	@SuppressWarnings({"unchecked","rawtypes"})
	static <T> T loadIm(ImConfigurationEntry parameter){
		T im = null;

		URLClassLoader urlLoader = new URLClassLoader(new URL[]{parameter.getPath()});
		Class clazz = null;
		Constructor ctor = null;
		try {
			clazz = urlLoader.loadClass(parameter.getMainClassName());
			ctor = clazz.getConstructor();
			im = (T)ctor.newInstance();
		} catch (ClassNotFoundException e) {
			LOGGER.error("Class named "+ parameter.getMainClassName() + " was not found.\nIMC/V will be ignored.\n", e);
			im = null;
		} catch (NoSuchMethodException e) {
			LOGGER.error("Constructor was not found for class " + clazz.getCanonicalName() +".\nIMC/V will be ignored.\n", e);
			im = null;
		} catch (SecurityException e) {
			LOGGER.error("Constructor was not accessible due to security constraints.\nIMC/V will be ignored.\n", e);
			im = null;
		} catch (InstantiationException e) {
			LOGGER.error("Class named "+ parameter.getMainClassName() + " could not be instantiated.\nIMC/V will be ignored.\n", e);
			im = null;
		} catch (IllegalAccessException e) {
			LOGGER.error("Constructor was not accessible due to accessibility constraints.\nIMC/V will be ignored.\n", e);
			im = null;
		} catch (IllegalArgumentException e) {
			LOGGER.error("Class named "+ parameter.getMainClassName() + " could not be instantiated.\nIMC/V will be ignored.\n", e);
			im = null;
		} catch (InvocationTargetException e) {
			LOGGER.error("Class named "+ parameter.getMainClassName() + " could not be instantiated, because the constructor has thrown an exception.\nIMC/V will be ignored.\n", e);
			im = null;
		}
		
		try {
			urlLoader.close();
		} catch (IOException e) {
			LOGGER.warn("An IOException occured while trying to close the ClassLoader. Usually this does not indicate a serious problem.", e);
		}
		
		
		return im;
	}
	
}
