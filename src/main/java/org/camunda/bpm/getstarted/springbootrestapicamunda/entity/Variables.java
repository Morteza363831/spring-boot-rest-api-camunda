package org.camunda.bpm.getstarted.springbootrestapicamunda.entity;


import org.camunda.bpm.getstarted.springbootrestapicamunda.validation.VariableValue;

import java.util.Map;

public class Variables {
    private Map<String, VariableValue> variables;

    // Getters and Setters
    public Map<String, VariableValue> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, VariableValue> variables) {
        this.variables = variables;
    }
}