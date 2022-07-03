package com.profile.saga.update;

import com.profile.exception.UserNotFoundException;
import com.profile.model.User;
import com.profile.saga.dto.UpdateUserDTO;
import com.profile.service.MessageQueueService;
import com.profile.service.UserService;

public class UpdateUserOrchestrator {

    private final MessageQueueService messageQueue;

    private final UserService userService;

    public UpdateUserOrchestrator(UserService userService) {
        this.messageQueue = new MessageQueueService(userService);
        this.userService = userService;
    }

    public void updateUser(User newUser) {
        User oldUser = userService.findById(newUser.getId()).orElseThrow(UserNotFoundException::new);
        userService.update(newUser);

        UpdateUserDTO newUserDTO = new UpdateUserDTO(newUser.getId(), newUser.getUsername());
        if (!newUser.getUsername().equals(oldUser.getUsername())) {
            messageQueue.publishUpdateUser(newUserDTO, oldUser, "nats.update.auth");
        }
    }
}
