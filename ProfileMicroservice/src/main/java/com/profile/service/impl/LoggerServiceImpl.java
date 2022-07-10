package com.profile.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.profile.service.LoggerService;

public class LoggerServiceImpl implements LoggerService {
	
	 private final Logger logger;

	 public LoggerServiceImpl(Class<?> parentClass) {this.logger = LogManager.getLogger(parentClass); }
	 
	 @Override
	    public void findUsers(String firstName, String lastName) {
	        logger.info("Users searched by parameters: first name: {}, last name: {}",firstName,lastName);
	    }
	 
	 @Override
	 public void getFirstAndLastName (String email) {
		 logger.info("Gotten first and last name for user: {}",email);
	 }
	 
	 @Override
	 public void getFirstAndLastNameFailed (String id) {
		 logger.warn("Failed to find first name and last name because a user with a searched id:  {} does not exist",id);
	 }
	 
	 @Override
	 public void getEmail (String email) {
		 logger.info("Gotten email address for a user: {}",email);
	 }

	 
	 @Override
	 public void getEmailFailed (String id) {
		 logger.warn("Failed to get email address because a user with a searched id:  {} does not exist",id);
	 }
	 
	 @Override
	 public void getId (String email) {
		 logger.info("Gotten id for a user: {}",email);
	 }

	 
	 @Override
	 public void getIdFailed (String email) {
		 logger.warn("Failed to get id because a user with a searched email:  {} does not exist",email);
	 }
	 
	 @Override
	 public void getUserById (String email) {
		 logger.info("Gotten informations for a user: {}",email);
	 }

	 
	 @Override
	 public void getUserByIdFailed (String id) {
		 logger.warn("Failed to get informations because a user with a searched id:  {} does not exist",id);
	 }
	 
	 @Override
	 public void addExperience (String email) {
		 logger.info("Added new experience for a user:  {}",email);
	 }
	 
	 @Override
	 public void addExperienceUserNotFound (String id) {
		 logger.warn("Failed to add experience because a user with the searched id: {} does not exist",id);
	 }

	 
	 @Override
	 public void addExperienceBadDate (String id) {
		 logger.warn("Failed to add experience for a user with id: {} because date was malformatted", id);
	 }
	 
	 @Override
	 public void updateExperience (String id, String userId) {
		 logger.info("Updated experience with id: {} for a user with id:  {}", id, userId);
	 }
	 
	 @Override
	 public void updateExperienceNotFound (String id) {
		 logger.warn("Failed to update experience because an experience with the searched id: {} does not exist",id);
	 }

	 
	 @Override
	 public void updateExperienceBadDate (String id) {
		 logger.warn("Failed to update experience for a user with id: {} because date was malformatted", id);
	 }
	 
	 @Override
	 public void deleteExperience (String id) {
		 logger.info("Deleted experience with id: {}", id);
	 }

	 
	 @Override
	 public void deleteExperienceNotFound (String id) {
		 logger.warn("Failed to delete experience because an experience with the searched id: {} does not exist", id);
	 }
	 
	 @Override
	 public void addInterest (String id, String email) {
		 logger.info("Added interest with id: {} for a user: {}", id, email);
	 }

	 
	 @Override
	 public void addInterestUserNotFound (String id) {
		 logger.warn("Failed to add interest because a user with the searched id: {} does not exist", id);
	 }
	 
	 @Override
	 public void removeInterest (String id, String email) {
		 logger.info("Removed interest with id: {} for a user: {}", id, email);
	 }

	 
	 @Override
	 public void removeInterestFailed (String id) {
		 logger.warn("Failed to remove interest because a interest with the searched id: {} does not exist", id);
	 }
	 
	 
	 @Override
	 public void addJobAd(String id, String email) {
		 logger.info("Added job ad with id: {} for a user: {}", id, email);
	 }
	 
	 @Override
	 public void addJobAdFailed(String email) {
		 logger.warn("Failed to add a job ad for a user: {}", email);
	 }

	 
	 @Override
	 public void addJobAdUserNotFound(String id) {
		 logger.warn("Failed to add a job ad because a user with the searched id: {} does not exist", id);
	 }
	 
	 @Override
	 public void getUserJobAds(String email) {
		 logger.info("Gotten job ads from a user: {}", email);
	 }

	 
	 @Override
	 public void getUserJobAdsFailed(String id) {
		 logger.warn("Failed to get job ads because a user with the searched id: {} does not exist", id);
	 }

	 @Override
	 public void getJobAdsByPosition(String position) {
		 logger.info("Gotten job ads for a position: {}", position);
	 }
	 
	 @Override
	 public void updateUser(String id) {
		 logger.info("Updated user with id: {}", id);
	 }
	 
	 @Override
	 public void updateUserNotFound(String id) {
		 logger.warn("Failed to update user because a user with the searched id: {} does not exist", id);
	 }

	 
	 @Override
	 public void updateUserUsernameAlreadyExists(String id, String username) {
		 logger.warn("Failed to update user with id: {} because a user with the input username: {} already exists", id, username);
	 }

	 @Override
	 public void updateUserBadDate(String id) {
		 logger.warn("Failed to update information for a user with id: {} because date was malformatted", id);
	 }

	@Override
	public void unsuccessfulRegistration(String userId) {
		logger.error("User registration failed. User id: {}", userId);
	}

}
