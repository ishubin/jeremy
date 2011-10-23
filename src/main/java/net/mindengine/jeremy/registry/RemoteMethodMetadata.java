package net.mindengine.jeremy.registry;

import java.io.Serializable;
import java.util.List;

public class RemoteMethodMetadata implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6295260579836788636L;
    private String method;
    private String object;
    private List<TypeDescriptor> arguments;
    private TypeDescriptor returns;
    public void setReturns(TypeDescriptor returns) {
        this.returns = returns;
    }
    public TypeDescriptor getReturns() {
        return returns;
    }
    public void setArguments(List<TypeDescriptor> arguments) {
        this.arguments = arguments;
    }
    public List<TypeDescriptor> getArguments() {
        return arguments;
    }
    public void setObject(String object) {
        this.object = object;
    }
    public String getObject() {
        return object;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public String getMethod() {
        return method;
    }
}
