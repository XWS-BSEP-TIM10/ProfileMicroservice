package com.profile.saga.update;

import com.profile.dto.NewUserDTO;
import com.profile.exception.UserNotFoundException;
import com.profile.exception.WorkflowException;
import com.profile.model.User;
import com.profile.saga.dto.OrchestratorResponseDTO;
import com.profile.saga.dto.UpdateUserDTO;
import com.profile.saga.update.workflow.AuthUpdateWorkflowStep;
import com.profile.saga.update.workflow.ProfileUpdateWorkflowStep;
import com.profile.saga.workflow.Workflow;
import com.profile.saga.workflow.WorkflowStep;
import com.profile.saga.workflow.WorkflowStepStatus;
import com.profile.service.UserService;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class UpdateUserOrchestrator {

    private final UserService userService;

    private final WebClient authClient;

    public UpdateUserOrchestrator(UserService userService, WebClient authClient) {
        this.userService = userService;
        this.authClient = authClient;
    }

    public Mono<OrchestratorResponseDTO> updateUser(NewUserDTO requestDTO) {
        Workflow workflow = this.getRegisterUserWorkflow(requestDTO);
        return Flux.fromStream(() -> workflow.getSteps().stream())
                .flatMap(WorkflowStep::process)
                .handle(((aBoolean, synchronousSink) -> {
                    if (Boolean.TRUE.equals(aBoolean))
                        synchronousSink.next(true);
                    else
                        synchronousSink.error(new WorkflowException("update user failed!"));
                }))
                .then(Mono.fromCallable(() -> getResponseDTO(true, "")))
                .onErrorResume(ex -> this.revertUpdate(workflow));
    }

    private Mono<? extends OrchestratorResponseDTO> revertUpdate(Workflow workflow) {
        return Flux.fromStream(() -> workflow.getSteps().stream())
                .filter(wf -> wf.getStatus() == WorkflowStepStatus.COMPLETE || wf.getStatus() == WorkflowStepStatus.START)
                .flatMap(WorkflowStep::revert)
                .retry(3)
                .then(Mono.just(getResponseDTO(false, "update user failed!")));
    }

    private Workflow getRegisterUserWorkflow(NewUserDTO userDTO) {
        List<WorkflowStep> workflowSteps = new ArrayList<>();

        User newUser = new User(userDTO);
        UpdateUserDTO newUserDTO = new UpdateUserDTO(userDTO.getUuid(), userDTO.getUsername());

        User oldUser = userService.findById(userDTO.getUuid()).orElseThrow(UserNotFoundException::new);
        UpdateUserDTO oldUserDTO = new UpdateUserDTO(oldUser.getId(), oldUser.getUsername());

        if (!newUser.getUsername().equals(oldUser.getUsername())) {
            AuthUpdateWorkflowStep authWorkflowStep = new AuthUpdateWorkflowStep(authClient, newUserDTO, oldUserDTO);
            workflowSteps.add(authWorkflowStep);
        }
        try {
            newUser.setDateOfBirth(new SimpleDateFormat("dd/MM/yyyy").parse(userDTO.getDateOfBirth()));
        } catch (ParseException e) {
            return null;
        }
        ProfileUpdateWorkflowStep profileWorkflowStep = new ProfileUpdateWorkflowStep(newUser, oldUser, userService);
        workflowSteps.add(profileWorkflowStep);

        return new Workflow(workflowSteps);
    }

    private OrchestratorResponseDTO getResponseDTO(boolean success, String message) {
        return new OrchestratorResponseDTO(success, message);
    }
}
