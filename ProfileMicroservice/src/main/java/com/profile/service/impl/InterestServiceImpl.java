package com.profile.service.impl;

import com.profile.model.Interest;
import com.profile.repository.InterestRepository;
import com.profile.service.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InterestServiceImpl implements InterestService {

    @Autowired
    private InterestRepository interestRepository;

    @Override
    public void save(Interest newInterest) {
        if(!exists(newInterest))
            interestRepository.save(newInterest);
    }

    private boolean exists(Interest newInterest) {
        for(Interest interest : interestRepository.findAll()) {
            if (interest.getDescription().equals(newInterest.getDescription())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Interest update(Long id, Interest newInterest) {
        Optional<Interest> existingInterest = interestRepository.findById(id);
        if(existingInterest.isEmpty())
            return null;
        newInterest.setId(id);
        return interestRepository.save(newInterest);
    }
}
