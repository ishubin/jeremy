package net.mindengine.jeremy.objects;

import net.mindengine.jeremy.bin.RemoteFile;


public class MyObject implements MyAnotherRemoteInterface, MyRemoteInterface{

    private Long id = 1L;
    private String name = "name";
    private SerialObject serialObject;
    private RemoteFile file;
    
    
    
    @Override
    public SerialObject getSerialObject() {
        return serialObject;
    }
    
    @Override
    public void setSerialObject(SerialObject serialObject) {
        this.serialObject = serialObject; 
    }
    
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setLong(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void uploadFile(RemoteFile file) {
        this.file = file;
    }

    @Override
    public RemoteFile downloadFile() {
        return this.file;
    }
    
    @Override
    public void someTestMethod() {
        throw new NullPointerException("Some test message");
    }
}
