package com.profile.service;

import com.profile.model.Experience;

public interface ExperienceService {

    Experience save(Experience experience);

    Experience update(Long id, Experience newExperience);
}
