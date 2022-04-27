package com.profile.repository;

import com.profile.model.User;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    void deleteByUuid(String uuid);
}
