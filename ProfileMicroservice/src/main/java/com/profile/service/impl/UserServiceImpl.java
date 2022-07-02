package com.profile.service.impl;

import com.profile.dto.NewUserDTO;
import com.profile.exception.UserNotFoundException;
import com.profile.exception.UsernameAlreadyExists;
import com.profile.model.Experience;
import com.profile.model.Interest;
import com.profile.model.User;
import com.profile.repository.UserRepository;
import com.profile.saga.dto.OrchestratorResponseDTO;
import com.profile.saga.update.UpdateUserOrchestrator;
import com.profile.service.UserService;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User create(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return null;
        }
        if (userRepository.findById(user.getId()).isPresent()) {
            return updateProfileUser(user);
        }
        return userRepository.save(user);
    }

    private User updateProfileUser(User user) {

        User existingUser = userRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
        existingUser.setFirstName(user.getFirstName());
        existingUser.setUsername(user.getUsername());
        existingUser.setLastName(user.getLastName());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setGender(user.getGender());
        existingUser.setDateOfBirth(user.getDateOfBirth());
        existingUser.setUsername(user.getUsername());
        existingUser.setBiography(user.getBiography());
        return userRepository.save(existingUser);
    }

    @Override
    public void removeExperience(Experience experience) {
        for (User user : userRepository.findAll()) {
            if (user.getExperiences().contains(experience)) {
                user.getExperiences().remove(experience);
                userRepository.save(user);
                return;
            }
        }
    }

    @Override
    public boolean removeInterest(String id, Interest interest) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) return false;
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
    public Mono<OrchestratorResponseDTO> updateUser(User user) throws UserNotFoundException, UsernameAlreadyExists {
        if (!userExists(user.getId()))
            throw new UserNotFoundException();
        if (usernameExists(user.getUsername(), user.getId()))
            throw new UsernameAlreadyExists();
        NewUserDTO userDTO = new NewUserDTO(user);
        SimpleDateFormat iso8601Formatter = new SimpleDateFormat("dd/MM/yyyy");
        userDTO.setDateOfBirth(iso8601Formatter.format(user.getDateOfBirth()));
        UpdateUserOrchestrator orchestrator = new UpdateUserOrchestrator(this, getAuthWebClient());
        return orchestrator.updateUser(userDTO);
    }

    private boolean usernameExists(String username, String id) {
        User user = userRepository.findByUsername(username);
        return user != null && !user.getId().equals(id);
    }

    private boolean userExists(String id) {
        return findById(id).isPresent();
    }

    private WebClient getAuthWebClient() {
        String host = System.getenv("AUTH_HOST");
        String authHost = host == null ? "localhost" : System.getenv("AUTH_HOST");
        String authPort = host == null ? "8083" : System.getenv("AUTH_PORT");
        String baseUrl = String.format("http://%s:%s/", authHost, authPort);
        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()))
                .build();
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
        if (!contains(user, newInterest)) user.getInterests().add(newInterest);
        userRepository.save(user);
    }

    private boolean contains(User user, Interest newInterest) {
        for (Interest interest : user.getInterests()) {
            if (interest.getDescription().equals(newInterest.getDescription()))
                return true;
        }
        return false;
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public String findIdByEmail(String email) {
        if (userRepository.findByEmail(email) == null)
            return null;
        return userRepository.findByEmail(email).getId();
    }

    @Override
    public void addExperience(Experience experience, User user) {
        user.getExperiences().add(experience);
        userRepository.save(user);
    }

    public List<User> findByFirstNameAndLastName(String firstName, String lastName) {
        return userRepository.findAllByFirstNameAndLastName(firstName, lastName);
    }
}
