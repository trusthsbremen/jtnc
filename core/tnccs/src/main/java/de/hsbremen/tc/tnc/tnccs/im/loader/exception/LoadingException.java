package de.hsbremen.tc.tnc.tnccs.im.loader.exception;

public class LoadingException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1607640634202142235L;

    public LoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadingException(String message) {
        super(message);
    }

}
