package org.camunda.bpm.getstarted.springbootrestapicamunda.service;

import java.util.Map;

public interface VariablesService {

    Map<String, Map<String, Object>> variables(String variablesUrl);
}
