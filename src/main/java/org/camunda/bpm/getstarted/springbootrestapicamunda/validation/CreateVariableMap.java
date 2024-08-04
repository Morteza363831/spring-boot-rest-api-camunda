package org.camunda.bpm.getstarted.springbootrestapicamunda.validation;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CreateVariableMap {

    public Map<String, Object> MapVariables(Object value, String type) {
        java.util.Map<String, Object> variable = new HashMap<>();
        variable.put("value", value);
        variable.put("type", type);
        return variable;
    }
}
