package com.profile.dto;

public class AuthSagaResponseDTO {

    private String id;
    private boolean success;
    private String message;

    public AuthSagaResponseDTO() {
    }

    public AuthSagaResponseDTO(String id, boolean success, String message) {
        this.id = id;
        this.success = success;
        this.message = message;
    }

    public AuthSagaResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
