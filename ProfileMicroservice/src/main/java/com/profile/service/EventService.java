package com.profile.service;

import com.profile.model.Event;

import java.util.List;

public interface EventService {
    Event save(Event event);
    List<Event> findAll();
}
