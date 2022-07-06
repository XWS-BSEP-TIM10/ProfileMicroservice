package com.profile.service;

import com.profile.model.Notification;

public interface NotificationService {

    Notification save(Notification notification, String userID);
}
