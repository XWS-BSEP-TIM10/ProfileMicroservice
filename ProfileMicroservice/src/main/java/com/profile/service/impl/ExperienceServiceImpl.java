package com.profile.service.impl;

import com.profile.model.Experience;
import com.profile.model.User;
import com.profile.repository.ExperienceRepository;
import com.profile.service.ExperienceService;
import com.profile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExperienceServiceImpl implements ExperienceService {

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private UserService userService;

    @Override
    public Experience save(Experience experience) {
        return experienceRepository.save(experience);
    }

    @Override
    public Experience update(Long id, Experience newExperience) {
        Optional<Experience> existingExperience = experienceRepository.findById(id);
        if(existingExperience.isEmpty())
            return null;
        newExperience.setId(id);
        return experienceRepository.save(newExperience);
    }

    @Override
    public Experience add(String userId, Experience newExperience) {
        Optional<User> existingUser = userService.findByUuid(userId);
        if (existingUser.isEmpty()) return null;
        Experience savedExperience = save(newExperience);
        userService.addExperience(savedExperience, existingUser.get());
        return savedExperience;
    }

    @Override
    public boolean remove(Long id) {
        Optional<Experience> experience = experienceRepository.findById(id);
        if(experience.isEmpty()) return false;
        userService.removeExperience(experience.get());
        experienceRepository.delete(experience.get());
        return true;
    }

}
