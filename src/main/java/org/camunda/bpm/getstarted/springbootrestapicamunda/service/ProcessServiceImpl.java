package org.camunda.bpm.getstarted.springbootrestapicamunda.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.getstarted.springbootrestapicamunda.entity.Task;
import org.camunda.bpm.getstarted.springbootrestapicamunda.validation.CreateVariableMap;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ProcessServiceImpl  implements ProcessService{

    private final String CAMUNDA_BASE_URL = "http://localhost:8080/engine-rest";
    private final CreateVariableMap createVariableMap;
    private final RestTemplate restTemplate;
    private final TaskService taskService;
    private final MapDataService mapDataService;
    private final VariablesService variablesService;

    public ProcessServiceImpl(CreateVariableMap createVariableMap,
                              RestTemplate restTemplate,
                              TaskService taskService,
                              MapDataService mapDataService,
                              VariablesService variablesService) {
        this.createVariableMap = createVariableMap;
        this.restTemplate = restTemplate;
        this.taskService = taskService;
        this.mapDataService = mapDataService;
        this.variablesService = variablesService;
    }

    @Override
    public Map<String, Object> startProcessInstanceByKey(String processDefinitionKey,
                                                         Map<String, Object> formData) {

        String startProcessUrl = CAMUNDA_BASE_URL + "/process-definition/key/" + processDefinitionKey + "/start";
        try {
            // call rest template to send rest api to uri
            ResponseEntity<Map> response = startProcess(startProcessUrl, formData);
            Map<String, Object> responseBody = response.getBody();
            // response statement
            Optional<Map<String, Object>> optionalTask = Optional.ofNullable(responseBody);
            if (optionalTask.isPresent() && response.getStatusCode().is2xxSuccessful()) {
                String processInstanceId = (String) responseBody.get("id");
                log.info("Process instance started with ID: {}", processInstanceId);
                // Fetch the task for the process instance
                String processTasksUrl = CAMUNDA_BASE_URL + "/task?processInstanceId=" + processInstanceId;
                Task[] tasks = taskService.getAllTasks(processTasksUrl);
                if (tasks != null && tasks.length > 0) {
                    String taskId = taskService.getTask(tasks, 0).getId();
                    log.info("Task fetched with ID: {}", taskId);
                    // Complete the task
                    String completeTaskUrl = CAMUNDA_BASE_URL + "/task/" + taskId + "/complete";
                    // complete task
                    taskService.completeTask(completeTaskUrl, "");
                    log.info("Task completed with ID: {}", taskId);
                    // get all variables from process
                    String variablesUrl = CAMUNDA_BASE_URL + "/process-instance/" + processInstanceId + "/variables";
                    Map<String, Map<String, Object>> newVariables = variablesService.variables(variablesUrl);
                    return mapDataService.mapData(newVariables);
                } else {
                        log.error("No tasks found for process instance ID: {}", processInstanceId);
                        return null;
                }
            } else {
                log.error("Failed to start process instance. Response: {}", responseBody);
                return null;
            }
        } catch (Exception e) {
            log.error("Exception occurred: ", e);
            return null;
        }
    }

    @Override
    public ResponseEntity<Map> startProcess(String startProcessUrl,
                                            Map<String, Object> formData) {

        // get data
        String name = formData.get("name").toString();
        Integer age = Integer.valueOf(formData.get("age").toString());
        String job = formData.get("job").toString();

        // Start process instance and set initial variables
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", createVariableMap.MapVariables(name, "String"));
        variables.put("age", createVariableMap.MapVariables(age, "Integer"));
        variables.put("job", createVariableMap.MapVariables(job, "String"));

        // add variables to http entity and then send it to start process url
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("variables", variables);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);


        return restTemplate.postForEntity(startProcessUrl, request, Map.class);
    }
}
