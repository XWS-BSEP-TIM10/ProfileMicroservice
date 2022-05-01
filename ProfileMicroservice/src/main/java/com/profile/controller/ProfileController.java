package com.profile.controller;

import com.profile.dto.NewExperienceDTO;
import com.profile.dto.NewInterestDTO;
import com.profile.dto.NewUserDTO;
import com.profile.dto.ProfileResponseDTO;
import com.profile.model.Experience;
import com.profile.model.Interest;
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
public class ProfileController {

    private final UserService service;

    @Autowired
    public ProfileController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProfileResponseDTO> create(@RequestBody NewUserDTO dto) {
        try {
            User newUser = new User(dto);
            newUser.setDateOfBirth(new SimpleDateFormat("dd/MM/yyyy").parse(dto.getDateOfBirth()));
            User savedUser = service.save(newUser);
            if (savedUser == null)
                return ResponseEntity.ok(new ProfileResponseDTO(false, "failed"));
            return ResponseEntity.ok(new ProfileResponseDTO(savedUser.getUuid(), true, "sucess"));
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

    @PutMapping("{id}/experience")
    public ResponseEntity<Boolean> addExperience(@PathVariable String id, @RequestBody NewExperienceDTO dto) {
        try {
            Experience newExperience = new Experience(dto);
            newExperience.setFromDate(new SimpleDateFormat("dd/MM/yyyy").parse(dto.getFromDate()));
            newExperience.setToDate(new SimpleDateFormat("dd/MM/yyyy").parse(dto.getToDate()));
            User user = service.addExperience(id, newExperience);
            if(user == null)
                return  ResponseEntity.notFound().build();
            return ResponseEntity.ok(true);
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("{id}/interest")
    public ResponseEntity<Boolean> addInterest(@PathVariable String id, @RequestBody NewInterestDTO dto) {
        Interest newInterest = new Interest(dto);
        User user = service.addInterest(id, newInterest);
        if(user == null)
            return  ResponseEntity.notFound().build();
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable String id) {
        service.deleteByUuid(id);
        return ResponseEntity.noContent().build();
    }

}
