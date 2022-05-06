package com.profile.service;

import com.profile.model.Experience;
import com.profile.model.Interest;
import com.profile.model.User;
import com.profile.saga.dto.OrchestratorResponseDTO;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void deleteById(String id);

    User update(User newUser);

    void addInterest(User user, Interest newInterest);

    Optional<User> findById(String id);

    void addExperience(Experience experience, User user);

    User create(User newUser);

    void removeExperience(Experience experience);

    boolean removeInterest(String id, Interest interest);

    Mono<OrchestratorResponseDTO> updateUser(User user);
    
    List<User> findByFirstNameAndLastName(String firstName, String lastName);
}
