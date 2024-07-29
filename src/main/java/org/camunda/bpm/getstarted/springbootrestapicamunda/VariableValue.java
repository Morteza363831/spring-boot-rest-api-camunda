package org.camunda.bpm.getstarted.springbootrestapicamunda;

public class VariableValue {

    private Object value;
    private String type;

    // Getters and Setters
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
