/*******************************************************************************
 * 2011 Ivan Shubin http://mindengine.net
 * 
 * This file is part of Mind-Engine Jeremy.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Mind-Engine Jeremy.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.mindengine.jeremy.registry;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
    
    
    /**
     * Creates descriptor of class with all accessible fields (either public fields either fields with getter setters). 
     * @param clazz
     * @return
     */
    public static TypeDescriptor createDescriptor(Class<?>clazz) {
        return createDescriptor(clazz, new HashSet<String>());
    }
    
    /**
     * 
     * @param clazz
     * @param describedTypes List of types that are already described in this branch of types. This is mainly to avoid infinite looping.
     * @return
     */
    @SuppressWarnings("unchecked")
    private static TypeDescriptor createDescriptor(Class<?> clazz, HashSet<String> describedTypes) {
        TypeDescriptor typeDescriptor = new TypeDescriptor();
        typeDescriptor.setType(clazz.getName());
        
        
        if(!describedTypes.contains(clazz.getName())) {
            describedTypes.add(clazz.getName());
            
            //Fetching types for all fields
            List<Field> fields = getListOfAccessibleFields(clazz);
            
            if(fields.size()>0) {
                Map<String, TypeDescriptor> fieldsMap = new HashMap<String, TypeDescriptor>();
                for(Field field : fields) {
                    fieldsMap.put(field.getName(), createDescriptor(field.getType(), (HashSet<String>)describedTypes.clone()));
                }
                typeDescriptor.setFields(fieldsMap);
            }
        }
        return typeDescriptor;
    }
    
    /**
     * Fetches all fields that are public or private fields with getters and setters declared in the specified class and all super-classes.
     * @param clazz
     * @return
     */
    private static List<Field> getListOfAccessibleFields(Class<?> clazz) {
        List<Field> fields = new LinkedList<Field>();
        
        Field[] declaredFields = clazz.getDeclaredFields();
        for(Field field : declaredFields) {
            if(!Modifier.isTransient(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
                if(Modifier.isPublic(field.getModifiers())) {
                   fields.add(field); 
                }
                else {
                  //Check if there are getters and setters for this field
                    String fieldName = field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1);
                    boolean checkPassed = true;
                    try {
                        clazz.getMethod("set"+fieldName, field.getType());
                        clazz.getMethod("get"+fieldName);
                    }
                    catch (Exception e) {
                        checkPassed = false;
                    }
                    if(checkPassed) {
                        fields.add(field);
                    }
                }
            }
        }
        return fields;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, TypeDescriptor> getFields() {
        return fields;
    }

    public void setFields(Map<String, TypeDescriptor> fields) {
        this.fields = fields;
    }
}
