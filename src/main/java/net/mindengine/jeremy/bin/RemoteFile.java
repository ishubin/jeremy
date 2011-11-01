package net.mindengine.jeremy.bin;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.io.FileUtils;

/**
 * Used when it is needed to send the file contents to a remote server
 * @author Ivan Shubin
 *
 */
public class RemoteFile implements Binary, Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -1800931527219318420L;
    private String name;
    private byte[]bytes;
    
    
    public RemoteFile() {
    }
    
    public RemoteFile(File file) throws IOException {
        loadFile(file);
    }

    private void loadFile(File file) throws IOException {
        this.setName(file.getName());
        this.setBytes(FileUtils.readFileToByteArray(file));
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
