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
