package com.profile.model;


import javax.persistence.*;

@Entity
public class UserNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private User user;

    @ManyToOne
    @JoinColumn
    private Notification notification;

    private boolean read;

    public UserNotification() {
    }

    public UserNotification(User user, Notification notification, boolean read) {
        this.id = id;
        this.user = user;
        this.notification = notification;
        this.read = read;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Notification getNotification() {
        return notification;
    }

    public boolean isRead() {
        return read;
    }
}
