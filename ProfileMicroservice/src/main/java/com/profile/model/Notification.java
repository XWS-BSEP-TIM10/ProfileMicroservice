package com.profile.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PostPersist;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Notification {

    @Id
    @GeneratedValue
    private UUID id;

    private String text;

    private LocalDate creationTime;

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

    public LocalDate getCreationTime() {
        return creationTime;
    }

    @PostPersist
    public void setCreationTime() {
        this.creationTime = LocalDate.now();
    }
}
