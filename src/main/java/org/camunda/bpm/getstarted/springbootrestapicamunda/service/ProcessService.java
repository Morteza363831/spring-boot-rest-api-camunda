package org.camunda.bpm.getstarted.springbootrestapicamunda.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ProcessService {

    String CAMUNDA_BASE_URL = "http://localhost:8080/engine-rest";

    Map<String, Object> startProcessInstanceByKey(String processDefinitionKey,
                                                  Map<String, Object> formData);

    ResponseEntity<Map> startProcess(String startProcessUrl,
                                     Map<String, Object> variables);
}
