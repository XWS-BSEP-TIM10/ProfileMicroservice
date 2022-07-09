package com.profile.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.profile.exception.UserNotFoundException;
import com.profile.model.Notification;
import com.profile.model.User;
import com.profile.model.UserNotification;
import com.profile.repository.NotificationRepository;
import com.profile.repository.UserNotificationRepository;
import com.profile.service.NotificationService;
import com.profile.service.UserService;

@Service
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
    	notification.setCreationTime();
        Optional<User> user = userService.findById(userId);
        if(user.isEmpty()) throw new UserNotFoundException();
        if(user.get().isMuteMassageNotifications() && notification.getText().contains("post"))
            return null;
        if(user.get().isMuteConnectionsNotifications() && notification.getText().contains("connect"))
            return null;
        Notification savedNotification = repository.save(notification);
        UserNotification userNotification = new UserNotification(user.get(), notification, false);
        userNotificationRepository.save(userNotification);
        return savedNotification;
    }
    
    @Override
    public Notification saveMultiple(Notification notification, List<String> usersIds) {
    	notification.setCreationTime();
    	Notification savedNotification = repository.save(notification);
    	for(String userId:usersIds) {
    		Optional<User> user = userService.findById(userId);
            if(user.isEmpty()) throw new UserNotFoundException();
            UserNotification userNotification = new UserNotification(user.get(), notification, false);
            if(user.get().isMutePostNotifications())
                continue;
            userNotificationRepository.save(userNotification);
    	}
        return savedNotification;
    }
}
