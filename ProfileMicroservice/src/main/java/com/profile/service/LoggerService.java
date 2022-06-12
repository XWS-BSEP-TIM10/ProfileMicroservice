package com.profile.service;

public interface LoggerService {
	
	void findUsers(String firstName, String lastName);
	void getFirstAndLastName (String email);
	void getFirstAndLastNameFailed (String id);
	void getEmail (String email);
	void getEmailFailed (String id);
	void getId(String email);
	void getIdFailed (String email);
	void getUserById(String email);
	void getUserByIdFailed (String email);
	void addExperience(String email);
	void addExperienceUserNotFound (String id);
	void addExperienceBadDate (String id);
	void updateExperience(String id, String userId);
	void updateExperienceNotFound (String id);
	void updateExperienceBadDate (String id);
	void deleteExperience (String id);
	void deleteExperienceNotFound (String id);
	void addInterest(String id, String email);
	void addInterestUserNotFound (String id);
	void removeInterest(String id, String email);
	void removeInterestFailed(String id);
	void addJobAd(String id, String email);
	void addJobAdFailed(String email);
	void addJobAdUserNotFound(String id);
	void getUserJobAds(String email);
	void getUserJobAdsFailed(String id);
	void getJobAdsByPosition(String position);
	void updateUser(String id);
	void updateUserNotFound(String id);
	void updateUserUsernameAlreadyExists(String id, String username);
	void updateUserBadDate(String id);
}
