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
