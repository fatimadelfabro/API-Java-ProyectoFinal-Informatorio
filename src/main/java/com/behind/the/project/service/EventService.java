package com.behind.the.project.service;

import com.behind.the.project.domain.Event;
import com.behind.the.project.domain.EventState;
import com.behind.the.project.domain.Project;
import com.behind.the.project.dto.MessageDTO;
import com.behind.the.project.dto.ProjectDTO;
import com.behind.the.project.exception.EventDetailNotFoundException;
import com.behind.the.project.exception.EventNotEditableException;
import com.behind.the.project.exception.StartAndEndEventDateException;
import com.behind.the.project.repository.EventRepository;
import com.behind.the.project.exception.StartAndEndEventDateNotFoundException;
import com.behind.the.project.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    private EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public MessageDTO createEvent(Event event){
        MessageDTO msgDto = new MessageDTO();
        try{
            if(event.getEventsDetail() == null || event.getEventsDetail().isEmpty() || event.getEventsDetail().isBlank()){
                throw new EventDetailNotFoundException();
            }

            if(event.getStartDate()==null || event.getEndDate() == null){
                throw new StartAndEndEventDateNotFoundException();
            }

            if(event.getStartDate().isBefore(LocalDate.now()) || event.getEndDate().isBefore(LocalDate.now())){
                throw new StartAndEndEventDateException();
            }

            if(event.getReward() == null || event.getReward().compareTo(BigDecimal.ZERO) < 0){
                event.setReward(BigDecimal.ZERO);
            }

            event.setEventState(EventState.OPEN);
            event.setStatus("A");

            eventRepository.save(event);
            msgDto.setStatus(HttpStatus.CREATED);
            msgDto.setMsg("The event has been created.");
        }catch (StartAndEndEventDateException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("The events can't have start or end date before current day.");
        }catch (StartAndEndEventDateNotFoundException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("The events must have a start and end date.");
        }catch (EventDetailNotFoundException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("The events must have a detail.");
        }
        return msgDto;
    }

    public MessageDTO updateEvent(Event event){
        MessageDTO msgDto = new MessageDTO();
        try{
            if (event.getStatus().equals("I")){
                throw new EventNotEditableException();
            }
            if(event.getEventsDetail() == null || event.getEventsDetail().isEmpty() || event.getEventsDetail().isBlank()){
                throw new EventDetailNotFoundException();
            }

            if(event.getStartDate()==null || event.getEndDate() == null){
                throw new StartAndEndEventDateNotFoundException();
            }

            if(event.getStartDate().isBefore(LocalDate.now()) || event.getEndDate().isBefore(LocalDate.now())){
                throw new StartAndEndEventDateException();
            }

            Event evt = eventRepository.getById(event.getId());

            evt.setEventsDetail(event.getEventsDetail());
            evt.setEventState(event.getEventState());
            evt.setEndDate(event.getEndDate());
            if(event.getReward().compareTo(BigDecimal.ZERO) > 0){
                evt.setReward(event.getReward());
            }

            eventRepository.save(evt);
            msgDto.setStatus(HttpStatus.OK);
            msgDto.setMsg("The event has been updated successfully.");
        }catch (StartAndEndEventDateException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("The events can't have start or end date before current day.");
        }catch (StartAndEndEventDateNotFoundException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("The events must have a start and end date.");
        }catch (EventDetailNotFoundException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("The events must have a detail.");
        }catch (EntityNotFoundException e){
            msgDto.setStatus(HttpStatus.NOT_FOUND);
            msgDto.setMsg("The event you are looking for doesn't exist.");
        }catch (EventNotEditableException e){
            msgDto.setStatus(HttpStatus.NOT_FOUND);
            msgDto.setMsg("You can't edit an inactive event.");
        }
        return msgDto;
    }

    public MessageDTO deleteEvent(Integer id){
        MessageDTO msgDto = new MessageDTO();
        try{

            Event evt = eventRepository.getById(id);

            evt.setStatus("I");

            eventRepository.save(evt);
            msgDto.setStatus(HttpStatus.OK);
            msgDto.setMsg("The event has been inactivated successfully.");
        }catch (EntityNotFoundException e){
            msgDto.setStatus(HttpStatus.NOT_FOUND);
            msgDto.setMsg("The event you are looking for doesn't exist.");
        }
        return msgDto;
    }



    public MessageDTO activateEvent(Integer id){
        MessageDTO msgDto = new MessageDTO();
        try{

            Event evt = eventRepository.getById(id);

            evt.setStatus("A");

            eventRepository.save(evt);
            msgDto.setStatus(HttpStatus.OK);
            msgDto.setMsg("The event has been activated successfully.");
        }catch (EntityNotFoundException e){
            msgDto.setStatus(HttpStatus.NOT_FOUND);
            msgDto.setMsg("The event you are looking for doesn't exist.");
        }
        return msgDto;
    }

    public MessageDTO changeStatusEvent(Integer id, EventState eventState){
        MessageDTO msgDto = new MessageDTO();
        try{

            Event evt = eventRepository.getById(id);

            evt.setEventState(eventState);

            eventRepository.save(evt);
            msgDto.setStatus(HttpStatus.OK);
            msgDto.setMsg("The event has been updated successfully.");
        }catch (EntityNotFoundException e){
            msgDto.setStatus(HttpStatus.NOT_FOUND);
            msgDto.setMsg("The event you are looking for doesn't exist.");
        }
        return msgDto;
    }

    public MessageDTO rankingEvents(Integer id){
        MessageDTO msgDto = new MessageDTO();
        try{
            Optional<Event> evt = eventRepository.findById(id);
            if(!evt.isEmpty()){
                List<ProjectDTO> dtos = Converter.convertToListOfProjectDTOS(evt.get().getProjects());
                Collections.sort(dtos, new Comparator<ProjectDTO>() {
                    @Override
                    public int compare(ProjectDTO o1, ProjectDTO o2) {
                        return o2.getLikes() - o1.getLikes();
                    }
                });
                msgDto.setAdditionalItems(dtos);
            }else{
                msgDto.setMsg("Nothing to show.");
            }
            msgDto.setStatus(HttpStatus.OK);
        }catch (EntityNotFoundException e){
            msgDto.setStatus(HttpStatus.NOT_FOUND);
            msgDto.setMsg("The event you are looking for doesn't exist.");
        }
        return msgDto;
    }
}
