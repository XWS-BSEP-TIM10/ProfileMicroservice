package com.profile.service;

import com.profile.model.Experience;
import com.profile.model.Interest;
import com.profile.model.User;

public interface UserService {
    User save(User user);

    void deleteByUuid(String id);

    User update(User newUser);

    User addExperience(String id, Experience newExperience);

    User addInterest(String id, Interest newInterest);
}
