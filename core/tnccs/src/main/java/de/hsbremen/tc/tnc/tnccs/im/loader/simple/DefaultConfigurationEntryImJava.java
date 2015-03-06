/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright (c) 2015, Carl-Heinz Genzel
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
