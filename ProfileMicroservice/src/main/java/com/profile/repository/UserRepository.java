package com.profile.repository;

import com.profile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    void deleteByUuid(String uuid);

    Optional<User> findByUuid(String uuid);
}
