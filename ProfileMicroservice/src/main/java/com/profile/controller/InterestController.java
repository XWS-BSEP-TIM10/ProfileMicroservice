package com.profile.controller;

import com.profile.dto.InterestDTO;
import com.profile.dto.NewInterestDTO;
import com.profile.model.Interest;
import com.profile.service.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/interests")
public class InterestController {

    @Autowired
    private InterestService service;

    @PutMapping("{id}")
    public ResponseEntity<InterestDTO> update(@PathVariable Long id, @RequestBody NewInterestDTO dto) {
        Interest newInterest = new Interest(dto);
        Interest updatedInterest = service.update(id, newInterest);
        if (updatedInterest == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new InterestDTO(updatedInterest));
    }
}
