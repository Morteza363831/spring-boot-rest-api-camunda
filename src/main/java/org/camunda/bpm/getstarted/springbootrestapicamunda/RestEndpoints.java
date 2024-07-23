package org.camunda.bpm.getstarted.springbootrestapicamunda;

import org.camunda.community.rest.client.api.ProcessDefinitionApi;
import org.camunda.community.rest.client.dto.*;
import org.camunda.community.rest.client.invoker.ApiException;
import org.camunda.community.rest.client.springboot.CamundaHistoryApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RestEndpoints {

    @Autowired private UserInfoService userInfoService;
    @Autowired private ProcessDefinitionApi processDefinitionApi;
    @Autowired private CamundaHistoryApi camundaHistoryApi;

    @PutMapping("/start")
    public ResponseEntity<String> startProcess(@RequestParam String name,
                                               @RequestParam int age,
                                               @RequestParam String job) throws ApiException {

        Map<String, VariableValueDto> variables = new HashMap<String, VariableValueDto>();
        variables.put("name",new VariableValueDto().value(name).type("String"));
        variables.put("age",new VariableValueDto().value(age).type("Integer"));
        variables.put("job",new VariableValueDto().value(job).type("String"));

        ProcessInstanceWithVariablesDto processInstance = processDefinitionApi.startProcessInstanceByKey(
                ProcessConstants.PROCESS_KEY,
                new StartProcessInstanceDto().variables(variables));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Started process instance with id: " + processInstance.getId());
    }

    @GetMapping("/info")
    public ResponseEntity<List<HistoricProcessInstanceDto>> getActiveProcessInstance() throws ApiException {
        List<HistoricProcessInstanceDto> processInstances = camundaHistoryApi.historicProcessInstanceApi().queryHistoricProcessInstances(
                null,
                null,
                new HistoricProcessInstanceQueryDto().active(true));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(processInstances);

    }

}
