package com.behind.the.project.service;

import com.behind.the.project.domain.Project;
import com.behind.the.project.domain.Users;
import com.behind.the.project.domain.Vote;
import com.behind.the.project.dto.MessageDTO;
import com.behind.the.project.exception.*;
import com.behind.the.project.repository.ProjectRepository;
import com.behind.the.project.repository.UsersRepository;
import com.behind.the.project.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VoteService {


    private VoteRepository voteRepository;

    private UsersRepository usersRepository;

    private ProjectRepository projectRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository, UsersRepository usersRepository, ProjectRepository projectRepository) {
        this.voteRepository = voteRepository;
        this.usersRepository = usersRepository;
        this.projectRepository = projectRepository;
    }

    public MessageDTO createVote(Vote vote){
        MessageDTO msgDto = new MessageDTO();
        try{
            Optional<Project> project = projectRepository.findById(vote.getProject().getId());
            Optional<Users> usr = usersRepository.findById(vote.getUser().getId());
            if(usr.isEmpty())
                throw new UserNotFoundException();
            if(project.isEmpty())
                throw new ProjectNotExistException();

            for (Vote v:project.get().getLike()) {
                if(v.getUser().getId().equals(vote.getUser().getId()))
                    throw new VoteAmountException();
            }

            if(vote.getUser() == null){
                throw new UserNotExistException();
            }

            if (vote.getUser().getStatus().equals("I"))
                throw new InactiveUserException();

            if(vote.getProject() == null){
                throw new ProjectNotExistException();
            }

            if(vote.getProject().getStatus().equals("I"))
                throw new InactiveProjectException();

            vote.setCreatedDate(LocalDate.now());
            vote = voteRepository.save(vote);
            Project pjt = project.get();
            pjt.getLike().add(vote);
            projectRepository.save(pjt);
            msgDto.setStatus(HttpStatus.CREATED);
            msgDto.setMsg("The vote has been created successfully.");
        }catch (UserNotExistException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("A vote must have a user.");
        }catch (ProjectNotExistException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("A vote must have a existing project.");
        }catch (InactiveUserException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("Inactive users can't vote.");
        }catch (InactiveProjectException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("Inactive projects can't receive vote.");
        }catch (VoteAmountException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("Users can vote once per project.");
        }catch (UserNotFoundException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("The user you are trying to use doesn't exist.");
        }
        return msgDto;
    }

    public Object findVotesByUser(Integer idUser){
        MessageDTO msgDto = new MessageDTO();
        try{
            Users usr = usersRepository.getById(idUser);
            if(usr.getEmail() == null)
                throw new EntityNotFoundException();
            return voteRepository.findByUser(usr);
        }catch (EntityNotFoundException e){
            msgDto.setStatus(HttpStatus.NOT_FOUND);
            msgDto.setMsg("The user you are looking for doesn't exist.");
        }
        return msgDto;
    }
}
