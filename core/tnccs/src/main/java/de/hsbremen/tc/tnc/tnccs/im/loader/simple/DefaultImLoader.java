package de.hsbremen.tc.tnc.tnccs.im.loader.simple;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationEntry;
import de.hsbremen.tc.tnc.tnccs.im.loader.ImLoader;
import de.hsbremen.tc.tnc.tnccs.im.loader.exception.LoadingException;
import de.hsbremen.tc.tnc.util.NotNull;

public class DefaultImLoader<T> implements ImLoader<T>{

    static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultImLoader.class);
    
    private Map<String, URLClassLoader> loaders;

    public DefaultImLoader() {
        this.loaders = new HashMap<>();
    }

    @Override
    public List<T> loadIms(List<ConfigurationEntry> configs) {
        NotNull.check("Configuration entrys cannot be null.", configs);
        List<T> imList = new ArrayList<>();
    
        for (ConfigurationEntry config : configs) {
            T im;
            try {
                im = this.loadIm(config);
                imList.add(im);
            } catch (LoadingException e) {
                LOGGER.error("Could not load IM(C/V). IM(C/V) will be ignored.", e); 
            }
        }
    
        return imList;
    }

    @Override
    public T loadIm(ConfigurationEntry config) throws LoadingException {
        NotNull.check("Configuration entry cannot be null.", config);
        if(config instanceof DefaultConfigurationEntryImJava){
            T im = this.loadImFromFile((DefaultConfigurationEntryImJava)config);
            return im;
        }
        
        throw new LoadingException("Configuration entry is not of type "
                + DefaultConfigurationEntryImJava.class.getCanonicalName()
                + " and will be ignored.");
       
    }

    public void cleanUp(Set<ConfigurationEntry> configs) {
        if(configs != null){
            Set<String> paths = new HashSet<>();
            for (ConfigurationEntry cfg : configs) {
                if(cfg instanceof DefaultConfigurationEntryImJava){
                    paths.add(((DefaultConfigurationEntryImJava)cfg)
                            .getPath().toString());
                }
            }
    
            Set<String> removablePaths = new HashSet<>(loaders.keySet());
            removablePaths.removeAll(paths);
    
            for (String removable : removablePaths) {
                
                URLClassLoader  cl = this.loaders.remove(removable);
                try {
                    cl.close();
                } catch (IOException e) {
                    LOGGER.warn(
                            "An IOException occured while trying to close the"
                            + " ClassLoader. Usually this does not indicate a"
                            + " serious problem.",
                            e);
                }
            }           
            
        }
    }

    private T loadImFromFile(final DefaultConfigurationEntryImJava parameter) throws LoadingException {
        T im = null;
    
        URLClassLoader urlLoader = null;
        if(this.loaders.containsKey(parameter.getPath().toString())){
            urlLoader = this.loaders.get(parameter.getPath().toString());
        }else{
            urlLoader = new URLClassLoader(
                new URL[] {parameter.getPath()});
        }
        Class clazz = null;
        Constructor ctor = null;
        try {
            clazz = urlLoader.loadClass(parameter.getMainClassName());
            ctor = clazz.getConstructor();
            im = (T) ctor.newInstance();
        } catch (ClassNotFoundException e) {
            throw new LoadingException("Class named " + parameter.getMainClassName()
                    + " was not found. IM(C/V) will be ignored.", e);

        } catch (NoSuchMethodException e) {
            throw new LoadingException(
                    "Constructor was not found for class "
                            + clazz.getCanonicalName()
                            + ". IM(C/V) will be ignored.", e);

        } catch (SecurityException e) {
            throw new LoadingException("Constructor was not accessible due to security "
                    + "constraints. IM(C/V) will be ignored.", e);

        } catch (InstantiationException e) {
            throw new LoadingException("Class named " + parameter.getMainClassName()
                    + " could not be instantiated."
                    + " IM(C/V) will be ignored.", e);

        } catch (IllegalAccessException e) {
            throw new LoadingException("Constructor was not accessible due to accessibility"
                    + " constraints. IM(C/V) will be ignored.", e);

        } catch (IllegalArgumentException e) {
            throw new LoadingException("Class named " + parameter.getMainClassName()
                    + " could not be instantiated."
                    + " IM(C/V) will be ignored.", e);

        } catch (InvocationTargetException e) {
            throw new LoadingException(
                    "Class named "
                            + parameter.getMainClassName()
                            + " could not be instantiated,"
                            + " because the constructor has thrown"
                            + " an exception. IM(C/V) will be ignored.",
                    e);

        }
        
        return im;
    }

}