package org.camunda.bpm.getstarted.springbootrestapicamunda;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SpringBootRestApiCamundaApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootRestApiCamundaApplication.class, args);


    }

    private void method() {
        MultiValueMap<String, Object> parts = getStringObjectMultiValueMap();

        /*parts.add("file2", form1FileResource); // Additional forms
        parts.add("file3", form2FileResource); // Additional forms*/

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parts, headers);

        // deploy bpmn file on camunda before we tried to deploy a package (bpmn,forms)
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "http://localhost:8080/engine-rest/deployment/create",
                    HttpMethod.POST,
                    request,
                    String.class
            );
            log.info(response.getBody());
        } catch (HttpClientErrorException e) {
            log.error(e.getResponseBodyAsString());
        }
    }

    private static MultiValueMap<String, Object> getStringObjectMultiValueMap() {

        FileSystemResource bpmnFileResource = getFileSystemResource();

        /*FileSystemResource form1FileResource = new FileSystemResource(form1File);
        FileSystemResource form2FileResource = new FileSystemResource(form2File);*/

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("deployment-name", "serviceTask");
        parts.add("enable-duplicate-filtering", "true");
        parts.add("deployment-source", "process application");
        parts.add("file1", bpmnFileResource); // Main BPMN file
        return parts;
    }

    private static FileSystemResource getFileSystemResource() {
        // static way and internal forms ! now we are using spring-boot-form-io-example as template
        File bpmnFile = new File("D:\\spring-boot-rest-api-camunda\\src\\main\\resources\\serviceTask.bpmn");

        /*File form1File = new File("D:\\spring-boot-rest-api-camunda\\src\\main\\resources\\templates\\form.html");
        File form2File = new File("D:\\spring-boot-rest-api-camunda\\src\\main\\resources\\templates\\displayUser.html");*/

        return new FileSystemResource(bpmnFile);
    }
}
