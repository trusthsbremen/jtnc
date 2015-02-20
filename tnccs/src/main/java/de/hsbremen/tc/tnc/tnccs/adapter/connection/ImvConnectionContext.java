package de.hsbremen.tc.tnc.tnccs.adapter.connection;

import java.util.Map;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;

/**
 * Generic IMV connection context.
 *
 * @author Carl-Heinz Genzel
 *
 */
public interface ImvConnectionContext extends ImConnectionContext {

    /**
     * Adds a recommendation of an IMV with the given ID to the context for
     * the overall result of an integrity check handshake.
     *
     * @param id the IMV ID
     * @param recommendationPair the recommendation pair
     * @throws TncException if recommendation cannot be added to the context
     */
    void addRecommendation(long id,
            ImvRecommendationPair recommendationPair) throws TncException;

    /**
     * Returns all recommendations, which were added to the context and
     * clears the context recommendation buffer.
     *
     * @return a map of recommendations (key:IMV ID, value:recommendation)
     */
    Map<Long, ImvRecommendationPair> clearRecommendations();
}
