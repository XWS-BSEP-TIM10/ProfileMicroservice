package com.profile.saga.dto;

import com.profile.model.User;

public class OrchestratorResponseDTO {

    private boolean success;

    private String message;

    private User oldUser;

    public OrchestratorResponseDTO() {
    }

    public OrchestratorResponseDTO(boolean success, String message, User oldUser) {
        this.success = success;
        this.message = message;
        this.oldUser = oldUser;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public User getOldUser() {
        return oldUser;
    }
}
