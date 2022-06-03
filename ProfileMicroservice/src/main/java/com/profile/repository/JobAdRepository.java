package com.profile.repository;

import com.profile.model.JobAd;
import com.profile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobAdRepository extends JpaRepository<JobAd, String> {
    List<JobAd> findAllByUser(User user);
}
