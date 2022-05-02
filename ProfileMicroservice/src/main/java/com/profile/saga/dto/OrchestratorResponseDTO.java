package com.profile.saga.dto;

public class OrchestratorResponseDTO {

    private boolean success;
    private String message;

    public OrchestratorResponseDTO() {
    }

    public OrchestratorResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
