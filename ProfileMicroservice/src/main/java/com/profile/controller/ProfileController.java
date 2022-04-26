package com.profile.controller;

import com.profile.dto.NewUserDto;
import com.profile.dto.ProfileResponseDTO;
import com.profile.model.User;
import com.profile.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        if(user == null)
            return ResponseEntity.ok(new ProfileResponseDTO(false, "failed"));
        return ResponseEntity.ok(new ProfileResponseDTO(true, "sucess"));
    }

    /*@DeleteMapping
    public ResponseEntity<ProfileResponseDTO> delete(@PathVariable String username) {

    }*/

}
