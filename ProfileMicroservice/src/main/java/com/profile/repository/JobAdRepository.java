package com.profile.repository;

import com.profile.model.JobAd;
import com.profile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobAdRepository extends JpaRepository<JobAd, String> {
    List<JobAd> findAllByUser(User user);

    @Query("select u from JobAd u where u.position like concat('%',:position,'%') ")
   List<JobAd> searchByPosition(@Param("position") String position);
}
