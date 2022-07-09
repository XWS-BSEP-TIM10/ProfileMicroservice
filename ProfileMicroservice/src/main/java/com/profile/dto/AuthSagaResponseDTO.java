package com.profile.dto;

public class AuthSagaResponseDTO {

    private String id;
    private boolean success;
    private String message;
    private NewUserDto user;
    private final String service = "Profile";

    public AuthSagaResponseDTO() {
    }

    public AuthSagaResponseDTO(String id, boolean success, String message, NewUserDto user) {
        this.id = id;
        this.success = success;
        this.message = message;
        this.user = user;
    }

    public AuthSagaResponseDTO(boolean success, String message, String id) {
        this.success = success;
        this.message = message;
        this.id = id;
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

    public void setId(String id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public NewUserDto getUser() {
        return user;
    }
}
