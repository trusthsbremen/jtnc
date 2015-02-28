package de.hsbremen.tc.tnc.message.m.message;

import java.util.List;

import de.hsbremen.tc.tnc.message.m.ImData;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;

/**
 * Generic integrity measurement component message base
 * consisting of a message header and a set of integrity
 * measurement attributes.
 *
 * @author Carl-Heinz Genzel
 *
 */
public interface ImMessage extends ImData {

    /**
     * Returns the header.
     *
     * @return the header
     */
    ImMessageHeader getHeader();

    /**
     * Returns the contained attributes.
     *
     * @return a list of attributes
     */
    List<? extends ImAttribute> getAttributes();
}
