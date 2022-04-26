package com.profile.controller;

import com.profile.dto.NewUserDto;
import com.profile.dto.ProfileResponseDTO;
import com.profile.model.User;
import com.profile.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/profiles")
public class ProfileController {

    private final UserServiceImpl service;

    @Autowired
    public ProfileController(UserServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProfileResponseDTO> register(@RequestBody NewUserDto dto) {
        User user = service.save(new User(dto));
        if (user == null)
            return ResponseEntity.ok(new ProfileResponseDTO(false, "failed"));
        return ResponseEntity.ok(new ProfileResponseDTO(user.getId(), true, "sucess"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
