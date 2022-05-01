package com.profile.model;

import com.profile.dto.NewInterestDTO;

import javax.persistence.*;

@Entity
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String description;

    public Interest() {
    }

    public Interest(NewInterestDTO dto) {
        this.description = dto.getDescription();
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

    public void setDescription(String name) {
        this.description = name;
    }
}
