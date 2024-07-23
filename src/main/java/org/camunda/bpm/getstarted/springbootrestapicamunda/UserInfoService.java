package org.camunda.bpm.getstarted.springbootrestapicamunda;


import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
@ExternalTaskSubscription("check-number")
public class UserInfoService implements ExternalTaskHandler {
    private final static Logger LOGGER = Logger.getLogger(UserInfoService.class.getName());

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String name = (String) externalTask.getVariable("name");
        Integer age = Integer.parseInt(externalTask.getVariable("age").toString());
        String job = (String) externalTask.getVariable("job");
        // Log the user information
        LOGGER.info("User Info:");
        LOGGER.info("Name: " + name);
        LOGGER.info("Age: " + age);
        LOGGER.info("Job Position: " + job);

        externalTaskService.complete(externalTask);

    }
}
