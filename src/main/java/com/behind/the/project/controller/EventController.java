package com.behind.the.project.controller;

import com.behind.the.project.domain.Event;
import com.behind.the.project.dto.MessageDTO;
import com.behind.the.project.repository.EventRepository;
import com.behind.the.project.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventService eventService;


    @GetMapping("/findAll")
    public List<Event> getAllEvent(){
        return this.eventRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<MessageDTO> createEvent(@RequestBody Event event){
        MessageDTO msgDto = eventService.createEvent(event);
        return new ResponseEntity<MessageDTO>(msgDto, msgDto.getStatus());
    }

    @PutMapping
    public ResponseEntity<MessageDTO> updateEvent(@RequestBody Event event){
        MessageDTO msgDto = eventService.updateEvent(event);
        return new ResponseEntity<MessageDTO>(msgDto, msgDto.getStatus());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> deleteEvente(@PathVariable Integer id){
        MessageDTO msgDTO = eventService.deleteEvent(id);
        return new ResponseEntity<MessageDTO>(msgDTO, msgDTO.getStatus());
    }

    @GetMapping("/activateEvent/{id}")
    public ResponseEntity<MessageDTO> activateEvent(@PathVariable Integer id){
        MessageDTO msgDto = eventService.activateEvent(id);
        return new ResponseEntity<MessageDTO>(msgDto, msgDto.getStatus());
    }

    @GetMapping("/ranking/{idEvent}")
    public ResponseEntity<MessageDTO> rankingEvent(@PathVariable Integer idEvent){
        MessageDTO msgDto = eventService.rankingEvents(idEvent);
        return new ResponseEntity<MessageDTO>(msgDto, msgDto.getStatus());
    }
}
