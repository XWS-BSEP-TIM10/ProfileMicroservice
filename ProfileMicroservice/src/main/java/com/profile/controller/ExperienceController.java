package com.profile.controller;

import com.profile.dto.ExperienceDTO;
import com.profile.dto.NewExperienceDTO;
import com.profile.model.Experience;
import com.profile.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping(value = "/api/v1/experiences")
public class ExperienceController {

    @Autowired
    private ExperienceService service;

    @PostMapping
    public ResponseEntity<ExperienceDTO> add(@RequestBody NewExperienceDTO dto) {
        try {
            Experience newExperience = new Experience(dto);
            newExperience.setFromDate(new SimpleDateFormat("dd/MM/yyyy").parse(dto.getFromDate()));
            newExperience.setToDate(new SimpleDateFormat("dd/MM/yyyy").parse(dto.getToDate()));
            Experience addedExperience = service.add(dto.getUserId(), newExperience);
            if(addedExperience == null)
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(new ExperienceDTO(addedExperience));
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<ExperienceDTO> update(@PathVariable Long id, @RequestBody NewExperienceDTO dto) {
        try {
            Experience newExperience = new Experience(dto);
            newExperience.setFromDate(new SimpleDateFormat("dd/MM/yyyy").parse(dto.getFromDate()));
            newExperience.setToDate(new SimpleDateFormat("dd/MM/yyyy").parse(dto.getToDate()));
            Experience updatedExperience = service.update(id, newExperience);
            if (updatedExperience == null)
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(new ExperienceDTO(updatedExperience));
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> remove(@PathVariable Long id) {
        boolean success = service.remove(id);
        if(!success) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
    }
}