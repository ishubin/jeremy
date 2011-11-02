package net.mindengine.jeremy.messaging.binary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.mindengine.jeremy.client.Client;
import net.mindengine.jeremy.exceptions.DeserializationException;
import net.mindengine.jeremy.exceptions.SerializationException;
import net.mindengine.jeremy.messaging.LanguageHandler;

public class DefaultBinaryLanguageHandler implements LanguageHandler {
    
    
    @Override
    public Object deserializeObject(String serializedString, Class<?> type) throws DeserializationException {
        if(serializedString!=null) {
            byte[] bytes = serializedString.getBytes();
            return this.deserializeObject(bytes, type);
        }
        return null;
    }

    @Override
    public String serializeResponse(Object object) throws SerializationException {
        byte[] bytes = this.serializeResponseToBytes(object);
        if(bytes!=null) {
            return new String(bytes);
        }
        return null;
    }

    @Override
    public String getMimeType() {
        return Client.APPLICATION_BINARY;
    }

    @Override
    public byte[] serializeResponseToBytes(Object object) throws SerializationException {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
            ObjectOutputStream oos = new ObjectOutputStream(bos) ;
            oos.writeObject(object);
            oos.close();
            return bos.toByteArray();
        }
        catch (Exception e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public Object deserializeObject(byte[] content, Class<?> type) throws DeserializationException {
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(content));
            return ois.readObject();
        }
        catch (Exception e) {
            throw new DeserializationException(e);
        }
    }

}
