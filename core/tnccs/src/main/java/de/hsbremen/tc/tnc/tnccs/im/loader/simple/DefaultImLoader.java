/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Carl-Heinz Genzel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
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

/**
 * Default loader to load IM(C/V) according to a configuration entry.
 * It handles the class path for the loaded IM(C/V) by managing a set
 * of URL class loaders.
 *
 *
 * @param <T> the loadable type (e.g. IMC or IMV)
 */
public class DefaultImLoader<T> implements ImLoader<T> {

    static final Logger LOGGER = LoggerFactory.getLogger(DefaultImLoader.class);

    private Map<String, URLClassLoader> loaders;

    /**
     * Creates the IM(C/V) loader.
     */
    public DefaultImLoader() {
        this.loaders = new HashMap<>();
    }

    @Override
    public List<T> loadIms(final List<ConfigurationEntry> configs) {
        NotNull.check("Configuration entrys cannot be null.", configs);
        List<T> imList = new ArrayList<>();

        for (ConfigurationEntry config : configs) {
            T im;
            try {
                im = this.loadIm(config);
                imList.add(im);
            } catch (LoadingException e) {
                LOGGER.error(
                        "Could not load IM(C/V). IM(C/V) will be ignored.", e);
            }
        }

        return imList;
    }

    @Override
    public T loadIm(final ConfigurationEntry config) throws LoadingException {
        NotNull.check("Configuration entry cannot be null.", config);
        if (config instanceof DefaultConfigurationEntryImJava) {
            T im = this
                    .loadImFromFile((DefaultConfigurationEntryImJava) config);
            return im;
        }

        throw new LoadingException("Configuration entry is not of type "
                + DefaultConfigurationEntryImJava.class.getCanonicalName()
                + " and will be ignored.");

    }

    @Override
    public void cleanUp(final Set<ConfigurationEntry> configs) {
        if (configs != null) {
            Set<String> paths = new HashSet<>();
            for (ConfigurationEntry cfg : configs) {
                if (cfg instanceof DefaultConfigurationEntryImJava) {
                    paths.add(((DefaultConfigurationEntryImJava) cfg).getPath()
                            .toString());
                }
            }

            Set<String> removablePaths = new HashSet<>(loaders.keySet());
            removablePaths.removeAll(paths);

            for (String removable : removablePaths) {

                URLClassLoader cl = this.loaders.remove(removable);
                try {
                    cl.close();
                } catch (IOException e) {
                    LOGGER.warn(
                            "An IOException occured while trying to close the"
                            + " ClassLoader. Usually this does not indicate a"
                            + " serious problem.", e);
                }
            }

        }
    }

    /**
     * Loads a Java based IM(C/V) as specified in the given configuration entry
     * from file and calls the default constructor.
     *
     * @param config the configuration entry
     * @return the IM(C/V)
     * @throws LoadingException if loading fails
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private T loadImFromFile(final DefaultConfigurationEntryImJava config)
            throws LoadingException {
        T im = null;

        URLClassLoader urlLoader = null;
        if (this.loaders.containsKey(config.getPath().toString())) {
            urlLoader = this.loaders.get(config.getPath().toString());
        } else {
            urlLoader = new URLClassLoader(new URL[] {config.getPath()});
        }
        Class clazz = null;
        Constructor ctor = null;
        try {
            clazz = urlLoader.loadClass(config.getMainClassName());
            ctor = clazz.getConstructor();
            im = (T) ctor.newInstance();
        } catch (ClassNotFoundException e) {
            throw new LoadingException("Class named "
                    + config.getMainClassName()
                    + " was not found. IM(C/V) will be ignored.", e);

        } catch (NoSuchMethodException e) {
            throw new LoadingException("Constructor was not found for class "
                    + clazz.getCanonicalName() + ". IM(C/V) will be ignored.",
                    e);

        } catch (SecurityException e) {
            throw new LoadingException(
                    "Constructor was not accessible due to security "
                            + "constraints. IM(C/V) will be ignored.", e);

        } catch (InstantiationException e) {
            throw new LoadingException("Class named "
                    + config.getMainClassName()
                    + " could not be instantiated."
                    + " IM(C/V) will be ignored.", e);

        } catch (IllegalAccessException e) {
            throw new LoadingException(
                    "Constructor was not accessible due to accessibility"
                            + " constraints. IM(C/V) will be ignored.", e);

        } catch (IllegalArgumentException e) {
            throw new LoadingException("Class named "
                    + config.getMainClassName()
                    + " could not be instantiated."
                    + " IM(C/V) will be ignored.", e);

        } catch (InvocationTargetException e) {
            throw new LoadingException("Class named "
                    + config.getMainClassName()
                    + " could not be instantiated,"
                    + " because the constructor has thrown"
                    + " an exception. IM(C/V) will be ignored.", e);

        }

        return im;
    }

}
