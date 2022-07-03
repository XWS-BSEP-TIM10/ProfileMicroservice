package com.profile.saga.dto;

import com.profile.model.User;

public class UpdateUserDTO {

    private String id;

    private String username;

    private User oldUser;

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

    public User getOldUser() {
        return oldUser;
    }

    public void setOldUser(User oldUser) {
        this.oldUser = oldUser;
    }
}
