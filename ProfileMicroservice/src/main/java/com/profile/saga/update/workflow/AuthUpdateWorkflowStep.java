package com.profile.saga.update.workflow;

import com.profile.saga.dto.AuthResponseDTO;
import com.profile.saga.dto.UpdateUserDTO;
import com.profile.saga.workflow.WorkflowStep;
import com.profile.saga.workflow.WorkflowStepStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class AuthUpdateWorkflowStep implements WorkflowStep {

    private final WebClient webClient;
    private final UpdateUserDTO newUserDTO;
    private final UpdateUserDTO oldUserDTO;
    private WorkflowStepStatus stepStatus = WorkflowStepStatus.PENDING;

    private static final String URI = "/api/v1/users";

    public AuthUpdateWorkflowStep(WebClient webClient, UpdateUserDTO newUserDTO, UpdateUserDTO oldUserDTO) {
        this.webClient = webClient;
        this.newUserDTO = newUserDTO;
        this.oldUserDTO = oldUserDTO;
    }

    @Override
    public WorkflowStepStatus getStatus() {
        return stepStatus;
    }

    @Override
    public Mono<Boolean> process() {
        stepStatus = WorkflowStepStatus.START;
        return sendRequest(newUserDTO);
    }

    @Override
    public Mono<Boolean> revert() {
        if (this.stepStatus == WorkflowStepStatus.FAILED)
            return Mono.just(true);
        return sendRequest(oldUserDTO);
    }

    private Mono<Boolean> sendRequest(UpdateUserDTO userDTO) {
        return webClient
                .put()
                .uri(URI)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(userDTO), UpdateUserDTO.class)
                .retrieve()
                .bodyToMono(AuthResponseDTO.class)
                .map(AuthResponseDTO::isSuccess)
                .doOnNext(b -> this.stepStatus = Boolean.TRUE.equals(b) ? WorkflowStepStatus.COMPLETE : WorkflowStepStatus.FAILED);
    }
}
