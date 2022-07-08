package com.profile.repository;

import com.profile.model.UserNotification;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
	ArrayList<UserNotification> findByUserId(String userId);
	
	@Modifying
	@Transactional
	@Query(value = "update user_notification set read = true where user_id = ?1", nativeQuery = true)
	void markUserNotificationsAsRead(String userId);
}
