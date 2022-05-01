package com.profile.service.impl;

import com.profile.model.Experience;
import com.profile.repository.ExperienceRepository;
import com.profile.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExperienceServiceImpl implements ExperienceService {

    @Autowired
    private ExperienceRepository experienceRepository;

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

}
