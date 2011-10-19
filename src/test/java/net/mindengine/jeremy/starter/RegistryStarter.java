package net.mindengine.jeremy.starter;

import net.mindengine.jeremy.registry.Registry;

public class RegistryStarter extends Thread {

    private Registry registry;
    
    @Override
    public void run() {
        try {
            registry.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void startRegistry() {
        this.start();
    }
    
    
    public void stopRegistry() {
        try {
            registry.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setRegistry(Registry registry) {
        this.registry = registry;
    }


    public Registry getRegistry() {
        return registry;
    }
}
