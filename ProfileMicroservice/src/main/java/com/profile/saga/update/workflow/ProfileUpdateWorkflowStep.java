package com.profile.saga.update.workflow;

import com.profile.model.User;
import com.profile.saga.workflow.WorkflowStep;
import com.profile.saga.workflow.WorkflowStepStatus;
import com.profile.service.UserService;
import reactor.core.publisher.Mono;

public class ProfileUpdateWorkflowStep implements WorkflowStep {

    private final User newUser;
    private final User oldUser;
    private final UserService userService;
    private WorkflowStepStatus status = WorkflowStepStatus.PENDING;

    public ProfileUpdateWorkflowStep(User newUser, User oldUser, UserService userService) {
        this.newUser = newUser;
        this.oldUser = oldUser;
        this.userService = userService;
    }

    @Override
    public WorkflowStepStatus getStatus() {
        return status;
    }

    @Override
    public Mono<Boolean> process() {
        return updateUser(newUser);
    }

    @Override
    public Mono<Boolean> revert() {
        return updateUser(oldUser);
    }

    private Mono<Boolean> updateUser(User user) {
        User updatedUser = userService.update(user);
        if(updatedUser == null) return Mono.just(false);
        status = WorkflowStepStatus.COMPLETE;
        return Mono.just(true);
    }
}
