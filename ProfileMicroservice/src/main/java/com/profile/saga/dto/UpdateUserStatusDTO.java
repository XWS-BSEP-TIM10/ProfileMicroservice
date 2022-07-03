package com.profile.saga.dto;

public class UpdateUserStatusDTO {

    private final String id;

    private final boolean profilePublic;

    public UpdateUserStatusDTO(String id, boolean profilePublic) {
        this.id = id;
        this.profilePublic = profilePublic;
    }

    public String getId() {
        return id;
    }

    public boolean isProfilePublic() {
        return profilePublic;
    }
}
