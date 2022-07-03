package com.profile.controller;

import com.profile.dto.AuthSagaResponseDTO;
import com.profile.dto.NewUserDTO;
import com.profile.dto.UpdateResponseDTO;
import com.profile.exception.UserNotFoundException;
import com.profile.exception.UsernameAlreadyExists;
import com.profile.model.User;
import com.profile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/profiles")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AuthSagaResponseDTO> create(@RequestBody NewUserDTO dto) {
        try {
            User newUser = new User(dto);
            newUser.setDateOfBirth(new SimpleDateFormat("dd/MM/yyyy").parse(dto.getDateOfBirth()));
            User createdUser = service.create(newUser);
            if (createdUser == null)
                return ResponseEntity.ok(new AuthSagaResponseDTO(false, "failed", dto.getUuid()));
            return ResponseEntity.ok(new AuthSagaResponseDTO(createdUser.getId(), true, "sucess", dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    public ResponseEntity<UpdateResponseDTO> update(@RequestBody NewUserDTO dto) {
        try {
            User user = new User(dto);
            user.setDateOfBirth(new SimpleDateFormat("dd/MM/yyyy").parse(dto.getDateOfBirth()));
            service.updateUser(user);
            return ResponseEntity.ok(new UpdateResponseDTO(true, "success"));
        } catch (ParseException e) {
            return ResponseEntity.badRequest().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UsernameAlreadyExists e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable String id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{first_name}/{last_name}")
    public ResponseEntity<List<User>> find(@PathVariable String first_name, @PathVariable String last_name) {
        List<User> users = service.findByFirstNameAndLastName(first_name, last_name);
        return ResponseEntity.ok(users);

    }
}
