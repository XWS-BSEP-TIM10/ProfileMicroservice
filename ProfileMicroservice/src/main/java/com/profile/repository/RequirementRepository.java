package com.profile.repository;

import com.profile.model.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequirementRepository extends JpaRepository<Requirement, String> {
    Optional<Requirement> getByName(String name);
}
