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

import java.net.URL;

import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationEntry;

/**
 * Configuration entry holding the necessary parameters to
 * load an IM(C/V).
 *
 *
 */
public class DefaultConfigurationEntryImJava implements ConfigurationEntry {

    private final String name;
    private final String mainClassName;
    private final URL path;

    /**
     * Creates a configuration entry with the given values.
     * @param name the IM(C/V) name
     * @param mainClassName the IM(C/V) the name of the class to load
     * @param path the path to the IM(C/V) Java archive
     */
    public DefaultConfigurationEntryImJava(final String name,
            final String mainClassName, final URL path) {
        this.name = name;
        this.mainClassName = mainClassName;
        this.path = path;
    }

    /**
     * Returns the IM(C/V) name.
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the IM(C/V) main class name.
     * @return the name of the class to load
     */
    public String getMainClassName() {
        return this.mainClassName;
    }

    /**
     * Returns th path to the IM(C/V) Java archive.
     * @return the path to the IM(C/V) archive
     */
    public URL getPath() {
        return this.path;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((this.mainClassName == null) ? 0 : this.mainClassName
                        .hashCode());
        result = prime * result
                + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result
                + ((this.path == null) ? 0 : this.path.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DefaultConfigurationEntryImJava other =
                (DefaultConfigurationEntryImJava) obj;
        if (this.mainClassName == null) {
            if (other.mainClassName != null) {
                return false;
            }
        } else if (!this.mainClassName.equals(other.mainClassName)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.path == null) {
            if (other.path != null) {
                return false;
            }
        } else if (!this.path.equals(other.path)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ImConfigurationEntry [name=" + this.name + ", mainClassName="
                + this.mainClassName + ", path=" + this.path.toString() + "]";
    }

}
