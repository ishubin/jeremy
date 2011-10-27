package net.mindengine.jeremy.exceptions;

public class RemoteObjectIsNotFoundException extends Exception{

    /**
     * 
     */
    private static final long serialVersionUID = -55968612011916780L;

    public RemoteObjectIsNotFoundException() {
        super();
    }

    public RemoteObjectIsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteObjectIsNotFoundException(String message) {
        super(message);
    }

    public RemoteObjectIsNotFoundException(Throwable cause) {
        super(cause);
    }

}
