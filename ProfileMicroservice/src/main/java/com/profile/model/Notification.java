package com.profile.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PostPersist;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
public class Notification {

    @Id
    @GeneratedValue
    private UUID id;

    private String text;

    private Date creationTime;

    public Notification() {
    }

    public Notification(String text) {
        this.text = text;
    }

    public UUID getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    @PostPersist
    public void setCreationTime() {
        this.creationTime = new Date();
    }
}
