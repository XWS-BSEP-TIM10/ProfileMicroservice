package com.profile.service.impl;

import com.profile.model.Requirement;
import com.profile.repository.RequirementRepository;
import com.profile.service.RequirementService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RequirementServiceImpl implements RequirementService {

    private final RequirementRepository repository;

    public RequirementServiceImpl(RequirementRepository repository) {
        this.repository = repository;
    }

    @Override
    public Requirement addNewRequirement(Requirement requirement) {
        Optional<Requirement> existingRequirement = repository.getByName(requirement.getName());
        if(existingRequirement.isPresent()) return existingRequirement.get();
        requirement.setId(UUID.randomUUID().toString());
        return repository.save(requirement);
    }
}
