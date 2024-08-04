package org.camunda.bpm.getstarted.springbootrestapicamunda.controller;

import lombok.extern.slf4j.Slf4j;

import org.camunda.bpm.getstarted.springbootrestapicamunda.service.ProcessService;
import org.camunda.bpm.getstarted.springbootrestapicamunda.service.VariablesService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
public class CamundaController {

    private final ProcessService processService;

    public CamundaController(ProcessService processService) {
        this.processService = processService;
    }

    /*// display form to user and get info from him
    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("user", new User());
        return "form";
    }

    @PostMapping("/submitForm")
    public String submitForm(@ModelAttribute("user") User user, Model model) {
        String processDefinitionKey = "serviceTask"; // Ensure this matches your BPMN definition key
        String startProcessUrl = CAMUNDA_BASE_URL + "/process-definition/key/" + processDefinitionKey + "/start";

        // Start process instance and set initial variables
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", createVariableMap(user.getName(), "String"));
        variables.put("age", createVariableMap(user.getAge(), "Integer"));
        variables.put("job", createVariableMap(user.getJob(), "String"));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("variables", variables);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(startProcessUrl, request, Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (response.getStatusCode().is2xxSuccessful() && responseBody != null) {
                String processInstanceId = (String) responseBody.get("id");
                logger.info("Process instance started with ID: {}", processInstanceId);

                // Fetch the task for the process instance
                String tasksUrl = CAMUNDA_BASE_URL + "/task?processInstanceId=" + processInstanceId;
                Task[] tasks = restTemplate.getForObject(tasksUrl, Task[].class);

                if (tasks != null && tasks.length > 0) {
                    String taskId = tasks[0].getId();
                    logger.info("Task fetched with ID: {}", taskId);

                    // Complete the task
                    String completeTaskUrl = CAMUNDA_BASE_URL + "/task/" + taskId + "/complete";
                    HttpEntity<String> completeTaskRequest = new HttpEntity<>("", headers);

                    restTemplate.postForObject(completeTaskUrl, completeTaskRequest, String.class);
                    logger.info("Task completed with ID: {}", taskId);

                    model.addAttribute("processInstanceId", processInstanceId);
                    return "redirect:/displayUser?processInstanceId=" + processInstanceId;
                } else {
                    logger.error("No tasks found for process instance ID: {}", processInstanceId);
                    return "error";
                }
            } else {
                logger.error("Failed to start process instance. Response: {}", responseBody);
                return "error";
            }
        } catch (Exception e) {
            logger.error("Exception occurred: ", e);
            return "error";
        }
    }

    // display user info to user after submitting <-- unusable
    @GetMapping("/displayUser")
    public String displayUser(@RequestParam("processInstanceId") String processInstanceId,
                              Model model) {
        // init variable url to get from process space
        String variablesUrl = processService.CAMUNDA_BASE_URL +
                "/process-instance/" + processInstanceId + "/variables";
        // map variables
        Map<String, Map<String,Object>> variables =
                variablesService.variables(variablesUrl);

        Optional<Map<String, Map<String, Object>>> optional = Optional.ofNullable(variables);
        if (optional.isPresent()) {
            // Populate the model with the retrieved variables
            model.addAttribute("userName", variables.get("newName").get("value"));
            model.addAttribute("userAge", variables.get("newAge").get("value"));
            model.addAttribute("userJob", variables.get("newJob").get("value"));

            return "displayUser";
        } else {
            log.error("Exception occurred: ");
            return "error";
        }
    }

    */



    // there is no need to codes top of this comment ! just next method is usable !!!

    @PostMapping("/submit-form")
    public ResponseEntity<Map> handleFormSubmission(@RequestBody Map<String, Object> formData) {

        // initial process definition
        String processDefinitionKey = "serviceTask";// Ensure this matches your BPMN definition key
        Map<String, Object> data =
                processService.startProcessInstanceByKey(processDefinitionKey,formData);

        Optional<Map<String, Object>> optional = Optional.ofNullable(data);
        if (optional.isPresent()) {
            return ResponseEntity.ok(data);
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
