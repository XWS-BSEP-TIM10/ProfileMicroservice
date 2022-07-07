package com.profile.service;

import java.util.ArrayList;

import com.profile.model.UserNotification;

public interface UserNotificationService {
	ArrayList<UserNotification> findByUserId(String userId);
	void markNotificationsAsReadForUser(String userId);
}
