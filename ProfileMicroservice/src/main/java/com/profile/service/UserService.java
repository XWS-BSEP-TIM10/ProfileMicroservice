package com.profile.service;

import com.profile.model.Experience;
import com.profile.model.Interest;
import com.profile.model.User;

import java.util.Optional;

public interface UserService {

    void deleteByUuid(String id);

    User update(User newUser);

    void addInterest(User user, Interest newInterest);

    Optional<User> findByUuid(String id);

    void addExperience(Experience experience, User user);

    User create(User newUser);

    void removeExperience(Experience experience);

    boolean removeInterest(String id, Interest interest);
}
