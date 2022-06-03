package com.profile.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Requirement {

    @Id
    private String id;

    @Column(name = "name")
    private String name;

    public Requirement() {
    }

    public Requirement(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
}
