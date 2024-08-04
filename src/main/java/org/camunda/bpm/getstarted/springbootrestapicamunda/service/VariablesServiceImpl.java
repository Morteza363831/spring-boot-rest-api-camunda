package org.camunda.bpm.getstarted.springbootrestapicamunda.service;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class VariablesServiceImpl implements VariablesService {

    private final RestTemplate restTemplate;

    public VariablesServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Map variables(String variablesUrl) {

        return restTemplate.getForObject(variablesUrl, Map.class);
    }
}
