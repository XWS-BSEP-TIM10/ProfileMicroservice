package com.profile.controller;

import com.profile.dto.InterestDTO;
import com.profile.dto.NewInterestDTO;
import com.profile.model.Interest;
import com.profile.service.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/interests")
public class InterestController {

    @Autowired
    private InterestService service;

    @PostMapping
    public ResponseEntity<InterestDTO> add(@RequestBody NewInterestDTO dto) {
        Interest newInterest = new Interest(dto);
        Interest addedInterest = service.add(dto.getUserId(), newInterest);
        if(addedInterest == null)
            return  ResponseEntity.notFound().build();
        return ResponseEntity.ok(new InterestDTO(addedInterest));
    }

    @DeleteMapping("{id}/user/{userId}")
    public ResponseEntity<HttpStatus> remove(@PathVariable Long id, @PathVariable String userId) {
        boolean success = service.removeInterest(id, userId);
        if(!success) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
    }
}
