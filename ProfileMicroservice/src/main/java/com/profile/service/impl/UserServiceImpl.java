package com.profile.service.impl;

import com.profile.model.Experience;
import com.profile.model.Interest;
import com.profile.model.User;
import com.profile.repository.UserRepository;
import com.profile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User create(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) return null;
        return userRepository.save(user);
    }

    @Override
    public void removeExperience(Experience experience) {
        for(User user : userRepository.findAll()){
            if(user.getExperiences().contains(experience)){
                user.getExperiences().remove(experience);
                userRepository.save(user);
                return;
            }
        }
    }

    @Override
    public boolean removeInterest(String id, Interest interest) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) return false;
        user.get().getInterests().remove(interest);
        userRepository.save(user.get());
        return true;
    }

    @Transactional
    @Override
    public void deleteById(String uuid) {
        userRepository.deleteById(uuid);
    }

    @Override
    public User update(User user) {
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isEmpty()) return null;
        user.setId(existingUser.get().getId());
        user.setExperiences(existingUser.get().getExperiences());
        user.setInterests(existingUser.get().getInterests());
        return userRepository.save(user);
    }

    @Override
    public void addInterest(User user, Interest newInterest) {
        if(!contains(user, newInterest)) user.getInterests().add(newInterest);
        userRepository.save(user);
    }

    private boolean contains(User user, Interest newInterest) {
        for(Interest interest : user.getInterests()) {
            if(interest.getDescription().equals(newInterest.getDescription()))
                return true;
        }
        return false;
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public void addExperience(Experience experience, User user) {
        user.getExperiences().add(experience);
        userRepository.save(user);
    }
}
