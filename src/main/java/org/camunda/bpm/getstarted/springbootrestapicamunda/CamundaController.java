package org.camunda.bpm.getstarted.springbootrestapicamunda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CamundaController {

    private static final Logger logger = LoggerFactory.getLogger(CamundaController.class);

    @Autowired
    private RestTemplate restTemplate;

    private final String CAMUNDA_BASE_URL = "http://localhost:8080/engine-rest";

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("user", new User());
        return "form";
    }

    /*@PostMapping("/submitForm")
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
*/

    @GetMapping("/displayUser")
    public String displayUser(@RequestParam("processInstanceId") String processInstanceId, Model model) {
        String url = CAMUNDA_BASE_URL + "/process-instance/" + processInstanceId + "/variables";

        try {
            Map<String, Map<String, Object>> variables = restTemplate.getForObject(url, Map.class);

            // Populate the model with the retrieved variables
            model.addAttribute("userName", variables.get("newName").get("value"));
            model.addAttribute("userAge", variables.get("newAge").get("value"));
            model.addAttribute("userJob", variables.get("newJob").get("value"));

            return "displayUser";
        } catch (Exception e) {
            logger.error("Exception occurred: ", e);
            return "error";
        }
    }
    private Map<String, Object> createVariableMap(Object value, String type) {
        Map<String, Object> variable = new HashMap<>();
        variable.put("value", value);
        variable.put("type", type);
        return variable;
    }


    @PostMapping("/submit-form")
    public ResponseEntity<Map<String,Object>> handleFormSubmission(@RequestBody Map<String, Object> formData) {
        // get data
        String name = formData.get("name").toString();
        Integer age = Integer.valueOf(formData.get("age").toString());
        String job = formData.get("job").toString();

        // initial process definition 
        String processDefinitionKey = "serviceTask"; // Ensure this matches your BPMN definition key
        String startProcessUrl = CAMUNDA_BASE_URL + "/process-definition/key/" + processDefinitionKey + "/start";

        // Start process instance and set initial variables
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", createVariableMap(name, "String"));
        variables.put("age", createVariableMap(age, "Integer"));
        variables.put("job", createVariableMap(job, "String"));

        // add variables to http entity and then send it to start process url
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("variables", variables);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            // call rest template to send rest api to uri
            ResponseEntity<Map> response = restTemplate.postForEntity(startProcessUrl, request, Map.class);
            Map<String, Object> responseBody = response.getBody();

            // response statement
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

                    // get all variables form process
                    String url = CAMUNDA_BASE_URL + "/process-instance/" + processInstanceId + "/variables";
                    Map<String, Map<String, Object>> newVariables = restTemplate.getForObject(url, Map.class);
                    Map<String,Object> data = new HashMap<>();
                    data.put("userName", newVariables.get("newName").get("value"));
                    data.put("userAge", newVariables.get("newAge").get("value"));
                    data.put("userJob", newVariables.get("newJob").get("value"));

                    return ResponseEntity.ok(data);

                } else {
                    logger.error("No tasks found for process instance ID: {}", processInstanceId);
                    return ResponseEntity.noContent().build();
                }
            } else {
                logger.error("Failed to start process instance. Response: {}", responseBody);
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            logger.error("Exception occurred: ", e);
            return ResponseEntity.noContent().build();
        }
    }
}
