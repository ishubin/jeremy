package net.mindengine.jeremy.registry;

import java.io.Serializable;
import java.util.Map;

/**
 * Used in getting remote method arguments metadata call. Describes the type of arguments that are used in remote methods. 
 * @author Ivan Shubin
 *
 */
public class TypeDescriptor implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -6090015637397274196L;

    private String type;
    private Map<String, TypeDescriptor> fields;
}
