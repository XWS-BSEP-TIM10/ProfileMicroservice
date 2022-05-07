package com.profile.dto;

public class UpdateResponseDTO {

    private boolean success;
    private String message;

    public UpdateResponseDTO() {
    }

    public UpdateResponseDTO(boolean success, String message) {
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
