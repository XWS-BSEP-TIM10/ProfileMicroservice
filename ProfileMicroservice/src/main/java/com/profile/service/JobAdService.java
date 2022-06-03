package com.profile.service;

import com.profile.model.JobAd;
import com.profile.model.User;

import java.util.List;

public interface JobAdService {
    JobAd save(JobAd jobAd);

    List<JobAd> findByUser(User user);
}
