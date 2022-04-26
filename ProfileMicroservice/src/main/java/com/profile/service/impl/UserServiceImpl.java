package com.profile.service.impl;

import com.profile.model.User;
import com.profile.repository.UserRepository;
import com.profile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if(existingUser != null)
            return null;
        return userRepository.save(user);
    }
}
