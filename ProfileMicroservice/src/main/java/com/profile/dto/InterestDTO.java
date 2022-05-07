package com.profile.dto;

import com.profile.model.Interest;

public class InterestDTO {

    private Long id;

    private String description;

    public InterestDTO() {
    }

    public InterestDTO(Interest interest) {
        this.id = interest.getId();
        this.description = interest.getDescription();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
