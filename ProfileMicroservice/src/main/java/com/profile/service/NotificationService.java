package com.profile.service;

import java.util.List;

import com.profile.model.Notification;

public interface NotificationService {

    Notification save(Notification notification, String userID);
    
    Notification saveMultiple(Notification notification, List<String> usersIds);
}
