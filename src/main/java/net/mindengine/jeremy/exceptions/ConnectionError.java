package net.mindengine.jeremy.exceptions;

public class ConnectionError extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -235821259020241229L;

    public ConnectionError() {
        super();
    }

    public ConnectionError(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionError(String message) {
        super(message);
    }

    public ConnectionError(Throwable cause) {
        super(cause);
    }

}
