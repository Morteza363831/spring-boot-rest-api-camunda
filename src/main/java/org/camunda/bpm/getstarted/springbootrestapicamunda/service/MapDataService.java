package org.camunda.bpm.getstarted.springbootrestapicamunda.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MapDataService {


    public Map<String,Object> mapData(Map<String, Map<String, Object>> variables) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("userName",variables.get("newName").get("value"));
        result.put("userAge",variables.get("newAge").get("value"));
        result.put("userJob",variables.get("newJob").get("value"));
        return result;
    }
}
