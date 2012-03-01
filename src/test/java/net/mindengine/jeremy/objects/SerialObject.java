/*******************************************************************************
* Copyright 2012 Ivan Shubin http://mindengine.net
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*   http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
******************************************************************************/
package net.mindengine.jeremy.objects;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SerialObject implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 2947083003293495299L;
    
    private List<SerialObject> listOfObjects;
    private Map<String, SerialObject> mapOfObjects;
    private SerialObject nestedObject;
    private Date date;
    private Double doubleField;
    private Float floatField;
    private Long longField;
    private String stringField;
    
    
    public SerialObject(){
        
    }
    
    public SerialObject(String stringField) {
        this.stringField = stringField;
    }
    
    public List<SerialObject> getListOfObjects() {
        return listOfObjects;
    }
    public void setListOfObjects(List<SerialObject> listOfObjects) {
        this.listOfObjects = listOfObjects;
    }
    public Map<String, SerialObject> getMapOfObjects() {
        return mapOfObjects;
    }
    public void setMapOfObjects(Map<String, SerialObject> mapOfObjects) {
        this.mapOfObjects = mapOfObjects;
    }
    public SerialObject getNestedObject() {
        return nestedObject;
    }
    public void setNestedObject(SerialObject nestedObject) {
        this.nestedObject = nestedObject;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public Double getDoubleField() {
        return doubleField;
    }
    public void setDoubleField(Double doubleField) {
        this.doubleField = doubleField;
    }
    public Float getFloatField() {
        return floatField;
    }
    public void setFloatField(Float floatField) {
        this.floatField = floatField;
    }
    public Long getLongField() {
        return longField;
    }
    public void setLongField(Long longField) {
        this.longField = longField;
    }
    public String getStringField() {
        return stringField;
    }
    public void setStringField(String stringField) {
        this.stringField = stringField;
    }
    
}
