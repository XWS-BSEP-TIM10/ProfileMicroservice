package com.profile.service.impl;

import com.profile.model.Event;
import com.profile.repository.EventRepository;
import com.profile.service.EventService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public List<Event> findAll() {
        return eventRepository.findAll();
    }
}
