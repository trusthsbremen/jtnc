package de.hsbremen.tc.tnc.tnccs.im.loader.exception;

/**
 * Exception signals that an IM(C/V) could not be loaded.
 * @author Carl-Heinz Genzel
 *
 */
public class LoadingException extends Exception {

    private static final long serialVersionUID = 1607640634202142235L;

    /**
     * Creates an exception with a specified message based on
     * an existing exception.
     *
     * @param message the specified exception message
     * @param exception the existing exception, which has to be encapsulated
     */
    public LoadingException(final String message, final Throwable exception) {
        super(message, exception);
    }

    /**
     * Creates an exception with a specified message.
     *
     * @param message the specified exception message
     */
    public LoadingException(final String message) {
        super(message);
    }

}
