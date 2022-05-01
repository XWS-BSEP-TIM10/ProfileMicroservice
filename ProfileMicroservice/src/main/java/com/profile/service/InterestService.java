package com.profile.service;

import com.profile.model.Interest;

public interface InterestService {

    void save(Interest newInterest);

    Interest update(Long id, Interest newInterest);
}
