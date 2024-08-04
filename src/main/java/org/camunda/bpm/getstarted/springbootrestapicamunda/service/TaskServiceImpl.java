package org.camunda.bpm.getstarted.springbootrestapicamunda.service;

import org.camunda.bpm.getstarted.springbootrestapicamunda.entity.Task;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TaskServiceImpl implements TaskService {

    private final RestTemplate restTemplate;

    public TaskServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Task[] getAllTasks(String processTasksUrl) {

        return restTemplate.getForObject(processTasksUrl, Task[].class);
    }

    @Override
    public Task getTask(Task[] tasks,int index) {
        return tasks[index];
    }

    @Override
    public ResponseEntity<String> completeTask(String completeTaskUrl, String body) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> completeTaskRequest = new HttpEntity<>("", headers);
        restTemplate.postForObject(completeTaskUrl, completeTaskRequest, String.class);
        return null;
    }
}
