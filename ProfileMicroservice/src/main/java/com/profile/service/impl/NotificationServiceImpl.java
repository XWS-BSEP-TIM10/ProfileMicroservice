package com.profile.service.impl;

import com.profile.exception.UserNotFoundException;
import com.profile.model.Notification;
import com.profile.model.User;
import com.profile.model.UserNotification;
import com.profile.repository.NotificationRepository;
import com.profile.repository.UserNotificationRepository;
import com.profile.service.NotificationService;
import com.profile.service.UserService;

import java.util.Optional;

public class NotificationServiceImpl implements NotificationService {


    private final NotificationRepository repository;

    private final UserNotificationRepository userNotificationRepository;

    private final UserService userService;

    public NotificationServiceImpl(NotificationRepository repository, UserNotificationRepository userNotificationRepository, UserService userService) {
        this.repository = repository;
        this.userNotificationRepository = userNotificationRepository;
        this.userService = userService;
    }

    @Override
    public Notification save(Notification notification, String userId) {
        Optional<User> user = userService.findById(userId);
        if(user.isEmpty()) throw new UserNotFoundException();
        Notification savedNotification = repository.save(notification);
        UserNotification userNotification = new UserNotification(user.get(), notification, false);
        userNotificationRepository.save(userNotification);
        return savedNotification;
    }
}
