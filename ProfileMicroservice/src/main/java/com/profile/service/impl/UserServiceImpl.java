package com.profile.service.impl;

import com.profile.model.Experience;
import com.profile.model.Interest;
import com.profile.model.User;
import com.profile.repository.UserRepository;
import com.profile.service.ExperienceService;
import com.profile.service.InterestService;
import com.profile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExperienceService experienceService;

    @Autowired
    private InterestService interestService;

    @Override
    public User save(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) return null;
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteByUuid(String uuid) {
        userRepository.deleteByUuid(uuid);
    }

    @Override
    public User update(User user) {
        User existingUser = userRepository.findByUuid(user.getUuid());
        if (existingUser == null) return null;
        user.setId(existingUser.getId());
        user.setExperiences(existingUser.getExperiences());
        user.setInterests(existingUser.getInterests());
        return userRepository.save(user);
    }

    @Override
    public User addExperience(String id, Experience newExperience) {
        User existingUser = userRepository.findByUuid(id);
        if (existingUser == null) return null;
        experienceService.save(newExperience);
        existingUser.getExperiences().add(newExperience);
        return userRepository.save(existingUser);
    }

    @Override
    public User addInterest(String id, Interest newInterest) {
        User existingUser = userRepository.findByUuid(id);
        if (existingUser == null) return null;
        interestService.save(newInterest);
        if(!contains(existingUser, newInterest)) existingUser.getInterests().add(newInterest);
        return userRepository.save(existingUser);
    }

    private boolean contains(User user, Interest newInterest) {
        for(Interest interest : user.getInterests()) {
            if(interest.getDescription().equals(newInterest.getDescription()))
                return true;
        }
        return false;
    }
}
