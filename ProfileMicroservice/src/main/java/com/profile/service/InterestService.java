package com.profile.service;

import com.profile.model.Interest;

public interface InterestService {

    Interest save(Interest newInterest);

    Interest add(String userId, Interest newInterest);

    boolean removeInterest(Long id, String userId);
}
