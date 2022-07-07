package com.profile.service.impl;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.profile.model.UserNotification;
import com.profile.repository.UserNotificationRepository;
import com.profile.service.UserNotificationService;

@Service
public class UserNotificationServiceImpl implements UserNotificationService{
	
	private final UserNotificationRepository repository;
	
	public UserNotificationServiceImpl(UserNotificationRepository repository) {
		this.repository = repository;
	}

	
	@Override
	public ArrayList<UserNotification> findByUserId(String userId) {
		
		return repository.findByUserId(userId);
	}


	@Override
	public void markNotificationsAsReadForUser(String userId) {
		repository.markUserNotificationsAsRead(userId);
	}
	
	

}
