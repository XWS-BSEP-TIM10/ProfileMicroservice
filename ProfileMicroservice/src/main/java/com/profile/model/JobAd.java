package com.profile.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class JobAd {

    @Id
    private String id;

    @ManyToOne
    private User user;

    private String title;

    private String position;

    private String description;

    private String company;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Requirement> requirements = new HashSet<>();

    public JobAd() {
    }

    public JobAd(String id, User recruiter, String title, String position, String description, String company) {
        this.id = id;
        this.user = recruiter;
        this.title = title;
        this.position = position;
        this.description = description;
        this.company = company;
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getPosition() {
        return position;
    }

    public String getDescription() {
        return description;
    }

    public String getCompany() {
        return company;
    }

    public Set<Requirement> getRequirements() {
        return requirements;
    }
}
