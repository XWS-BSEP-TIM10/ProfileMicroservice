package com.profile.service;

import com.profile.model.User;

public interface UserService {
    User save(User user);

    void deleteByUuid(String id);
}
