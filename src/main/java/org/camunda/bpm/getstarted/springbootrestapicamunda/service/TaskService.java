package org.camunda.bpm.getstarted.springbootrestapicamunda.service;

import org.camunda.bpm.getstarted.springbootrestapicamunda.entity.Task;
import org.springframework.http.ResponseEntity;

public interface TaskService {

    Task[] getAllTasks(String processTasksUrl);
    Task getTask(Task[] tasks,int index);
    ResponseEntity<String> completeTask(String taskUrl, String body);
}
