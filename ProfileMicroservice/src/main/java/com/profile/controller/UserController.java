package com.profile.controller;

import com.profile.dto.NewUserDTO;
import com.profile.dto.ProfileResponseDTO;
import com.profile.model.User;
import com.profile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping(value = "/api/v1/profiles")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProfileResponseDTO> create(@RequestBody NewUserDTO dto) {
        try {
            User newUser = new User(dto);
            newUser.setDateOfBirth(new SimpleDateFormat("dd/MM/yyyy").parse(dto.getDateOfBirth()));
            User createdUser = service.create(newUser);
            if (createdUser == null)
                return ResponseEntity.ok(new ProfileResponseDTO(false, "failed"));
            return ResponseEntity.ok(new ProfileResponseDTO(createdUser.getUuid(), true, "sucess"));
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    public ResponseEntity<ProfileResponseDTO> update(@RequestBody NewUserDTO dto) {
        try {
            User user = new User(dto);
            user.setDateOfBirth(new SimpleDateFormat("dd/MM/yyyy").parse(dto.getDateOfBirth()));
            User updatedUser = service.update(user);
            if (updatedUser == null)
                return ResponseEntity.ok(new ProfileResponseDTO(false, "failed"));
            return ResponseEntity.ok(new ProfileResponseDTO(updatedUser.getUuid(), true, "sucess"));
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable String id) {
        service.deleteByUuid(id);
        return ResponseEntity.noContent().build();
    }
}
