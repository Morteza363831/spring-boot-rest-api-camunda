package org.camunda.bpm.getstarted.springbootrestapicamunda;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ExternalTaskSubscription(topicName = "userInfoService")
public class UserInfoService implements ExternalTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoService.class);

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        LOGGER.info("Started executing external task: {}", externalTask.getId());

        try {
            // Retrieve process variables
            Map<String, Object> variables = externalTask.getAllVariables();
            LOGGER.info("Process variables: {}", variables);

            String name = (String) variables.get("name");
            Integer age = (Integer) variables.get("age");
            String job = (String) variables.get("job");

            // Your business logic here
            String newName = name + " changed second";
            int newAge = age + 10;
            String newJob = job.toLowerCase();

            Map<String, Object> newVariables = new HashMap<>();
            newVariables.put("userName", newName);
            newVariables.put("userAge", newAge);
            newVariables.put("userJob", newJob);
            System.out.println(name + " " + age + " " + job);
            externalTaskService.complete(externalTask, newVariables);
            LOGGER.info("Successfully completed external task: {}", externalTask.getId());

        } catch (Exception e) {
            LOGGER.error("Failed to execute external task: {}", externalTask.getId(), e);

            // Handle the exception
            externalTaskService.handleFailure(externalTask, "Service Task Execution Error", e.getMessage(), 0, 0);
        }
    }
}
