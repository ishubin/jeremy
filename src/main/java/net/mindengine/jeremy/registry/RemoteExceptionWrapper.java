package net.mindengine.jeremy.registry;

public class RemoteExceptionWrapper {

    private String type;
    private Throwable error;
    public void setError(Throwable error) {
        this.error = error;
    }
    public Throwable getError() {
        return error;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
