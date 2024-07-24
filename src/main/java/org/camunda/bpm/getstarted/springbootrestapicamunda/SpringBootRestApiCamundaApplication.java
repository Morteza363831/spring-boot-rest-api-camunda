package org.camunda.bpm.getstarted.springbootrestapicamunda;

import org.camunda.bpm.client.spring.annotation.EnableExternalTaskClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@SpringBootApplication
public class SpringBootRestApiCamundaApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootRestApiCamundaApplication.class, args);

        File bpmnFile = new File("D:\\spring-boot-rest-api-camunda\\src\\main\\resources\\serviceTask.bpmn");
        File form1File = new File("D:\\spring-boot-rest-api-camunda\\src\\main\\resources\\static\\forms\\form.html");
        File form2File = new File("D:\\spring-boot-rest-api-camunda\\src\\main\\resources\\static\\forms\\displayUser.html");

        FileSystemResource bpmnFileResource = new FileSystemResource(bpmnFile);
        FileSystemResource form1FileResource = new FileSystemResource(form1File);
        FileSystemResource form2FileResource = new FileSystemResource(form2File);

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("deployment-name", "serviceTask");
        parts.add("enable-duplicate-filtering", "true");
        parts.add("deployment-source", "process application");
        parts.add("file1", bpmnFileResource); // Main BPMN file
        parts.add("file2", form1FileResource); // Additional forms
        parts.add("file3", form2FileResource); // Additional forms

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parts, headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "http://localhost:8080/engine-rest/deployment/create",
                    HttpMethod.POST,
                    request,
                    String.class
            );
            System.out.println("Response: " + response.getBody());
        } catch (HttpClientErrorException e) {
            System.err.println("Error: " + e.getResponseBodyAsString());
        }
    }
}
