package com.profile.saga.dto;

public class UpdateUserDTO {

    private String id;

    private String username;

    public UpdateUserDTO() {
    }

    public UpdateUserDTO(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
