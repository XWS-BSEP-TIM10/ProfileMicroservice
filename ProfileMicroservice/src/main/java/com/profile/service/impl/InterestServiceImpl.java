package com.profile.service.impl;

import com.profile.model.Interest;
import com.profile.model.User;
import com.profile.repository.InterestRepository;
import com.profile.service.InterestService;
import com.profile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InterestServiceImpl implements InterestService {

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private UserService userService;

    @Override
    public Interest save(Interest newInterest) {
        Optional<Interest> existingInterest = interestRepository.findByDescription(newInterest.getDescription());
        return existingInterest.orElseGet(() -> interestRepository.save(newInterest));
    }

    @Override
    public Interest add(String userId, Interest newInterest) {
        Optional<User> existingUser = userService.findByUuid(userId);
        if (existingUser.isEmpty()) return null;
        Interest savedInterest = save(newInterest);
        userService.addInterest(existingUser.get(), savedInterest);
        return savedInterest;
    }

    @Override
    public boolean removeInterest(Long id, String userId) {
        Optional<Interest> interest = interestRepository.findById(id);
        if(interest.isEmpty()) return false;
        return userService.removeInterest(userId, interest.get());
    }
}
