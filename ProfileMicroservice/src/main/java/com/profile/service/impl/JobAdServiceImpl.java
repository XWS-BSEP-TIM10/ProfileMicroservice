package com.profile.service.impl;

import com.profile.model.JobAd;
import com.profile.model.User;
import com.profile.repository.JobAdRepository;
import com.profile.service.JobAdService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobAdServiceImpl implements JobAdService {

    private final JobAdRepository repository;

    public JobAdServiceImpl(JobAdRepository repository) {
        this.repository = repository;
    }

    @Override
    public JobAd save(JobAd jobAd) {
        return repository.save(jobAd);
    }

    @Override
    public List<JobAd> findByUser(User user) {
        return repository.findAllByUser(user);
    }
}
