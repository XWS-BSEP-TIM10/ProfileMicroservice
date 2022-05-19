package com.profile.repository;

import com.profile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String username);

    @Query(value = "SELECT * FROM USERS WHERE LOWER(FIRST_NAME) LIKE LOWER(CONCAT('%', ?1, '%')) AND LOWER(LAST_NAME) LIKE LOWER(CONCAT('%', ?2, '%'))", nativeQuery = true)
    List<User> findAllByFirstNameAndLastName(String firstName, String lastName);
}
