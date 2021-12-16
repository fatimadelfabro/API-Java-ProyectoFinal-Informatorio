package com.behind.the.project.service;

import com.behind.the.project.domain.Image;
import com.behind.the.project.domain.Project;
import com.behind.the.project.domain.Tag;
import com.behind.the.project.domain.Users;
import com.behind.the.project.dto.MessageDTO;
import com.behind.the.project.exception.*;
import com.behind.the.project.repository.ProjectRepository;
import com.behind.the.project.repository.TagRepository;
import com.behind.the.project.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;

    private TagRepository tagRepository;

    private UsersRepository usersRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, TagRepository tagRepository, UsersRepository usersRepository) {
        this.projectRepository = projectRepository;
        this.tagRepository = tagRepository;
        this.usersRepository = usersRepository;
    }

    private Boolean isUserExist(Users usr){
        Optional<Users> user = usersRepository.findById(usr.getId());
        if(user.isEmpty())
            return false;
        else
            return true;
    }

    private Boolean isAnExistingTagId(Tag tg){
        Optional<Tag> oTag = tagRepository.findById(tg.getId());
        Tag tag = tagRepository.findByTag(tg.getTag());
        if( oTag.isEmpty() && tag == null )
            return false;
        else
            return true;
    }

    private Boolean isAnExistingTag(Tag tg){
        Tag tag = tagRepository.findByTag(tg.getTag());
        if( tag == null )
            return false;
        else
            return true;
    }

    public MessageDTO createProject (Project project){
        MessageDTO msgDto = new MessageDTO();
        try {
            if (project.getUsers() == null || project.getUsers().isEmpty()){
                throw new UserNotExistException();
            }

            for (Users usr: project.getUsers()) {
                if(!isUserExist(usr))
                    throw new UserNotExistException();
            }

            if (project.getName() == null || project.getName().isEmpty() || project.getName().isBlank() ||
                project.getDescription() == null || project.getDescription().isEmpty() ||
                project.getDescription().isBlank() || project.getContent() == null ||
                project.getContent().isEmpty() || project.getContent().isBlank() ||
                project.getEvent() == null){
                throw new BasicDataProjectNotFoundException();
            }

            Tag nwTag = null;
            for (Tag tg : project.getTags()) {
                if (tg.getTag() == null ||
                        tg.getTag().isBlank() || tg.getTag().isEmpty()){
                    throw new Exception();
                }else if(!isAnExistingTagId(tg) || !isAnExistingTag(tg)){
                    nwTag = new Tag();
                    nwTag.setTag(tg.getTag());
                    tagRepository.save(nwTag);
                }
            }

            List<Tag> tags = project.getTags();
            project.setTags(new ArrayList<>());
            for (Tag tg : tags) {
                project.getTags().add(tagRepository.findByTag(tg.getTag()));
            }

            project.setCreatedDate(LocalDate.now());
            if(project.getObjective() == null || project.getObjective().compareTo(BigDecimal.ZERO) < 0){
                project.setObjective(BigDecimal.ZERO);
            }

            project.setStatus("A");

            projectRepository.save(project);

            msgDto.setStatus(HttpStatus.CREATED);
            msgDto.setMsg("The project has been created.");

        }catch (UserNotExistException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("The project must have an existing user.");
        }catch (BasicDataProjectNotFoundException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("All project must have name, description and content.");
        }catch (Exception e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("Verify you are inserting the right values in each field.");
        }
        return msgDto;
    }

    public MessageDTO updateProject (Project project){
        MessageDTO msgDto = new MessageDTO();
        try {
            if(project.getStatus().equals("I")){
                throw new ProjectNotEditableException();
            }
            if (project.getUsers() == null || project.getUsers().isEmpty()){
                throw new UserNotExistException();
            }

            if (project.getName() == null || project.getName().isEmpty() || project.getName().isBlank() ||
                    project.getDescription() == null || project.getDescription().isEmpty() ||
                    project.getDescription().isBlank() || project.getContent() == null ||
                    project.getContent().isEmpty() || project.getContent().isBlank() ||
                    project.getEvent() == null){
                throw new BasicDataProjectNotFoundException();
            }

            Project proj = projectRepository.getById(project.getId());

            if(proj.getObjective() != project.getObjective() && proj.isPublished()){
                throw new ObjetiveChangeException();
            }
            if(project.getObjective() == null || project.getObjective().compareTo(BigDecimal.ZERO) < 0){
                project.setObjective(BigDecimal.ZERO);
            }

            proj.setDescription(project.getDescription());
            proj.setContent(project.getContent());

            List<Users> aux = new ArrayList<>();
            Optional<Users> user;
            for (Users usr: project.getUsers()) {
                user = proj.getUsers().stream().filter(u -> u.getEmail().equals(usr.getEmail())).findFirst();
                if(user.isEmpty()){
                    aux.add(usr);
                }
            }

            if(aux.isEmpty()){
                proj.getUsers().addAll(aux);
            }


            if(!proj.getObjective().equals(project.getObjective()) && proj.isPublished()){
                throw new ProfitChangeException();
            }else if (!proj.isPublished() && !proj.getObjective().equals(project.getObjective())){
                proj.setObjective(project.getObjective());
            }

            /*Tag tagIntr;
            for (Tag tags: project.getTags()) {
                if(tags.getId() == null && tags.getTag() != null && !tags.getTag().isBlank()){
                    tags = tagRepository.save(tags);
                }else if(tags.getId() == null && (tags.getTag() == null || tags.getTag().isBlank())){
                    throw new TagNameNotFoundException();
                }
                tagIntr = tagRepository.getById(tags.getId());
                if(!tagIntr.getTag().equals(tags.getTag())){
                    throw new TagFormatException();
                }

            }*/
            Tag nwTag = null;
            for (Tag tg : project.getTags()) {
                if (tg.getTag() == null ||
                        tg.getTag().isBlank() || tg.getTag().isEmpty()){
                    throw new Exception();
                }else if(!isAnExistingTagId(tg) || !isAnExistingTag(tg)){
                    nwTag = new Tag();
                    nwTag.setTag(tg.getTag());
                    tagRepository.save(nwTag);
                }
            }

            List<Tag> tags = project.getTags();
            project.setTags(new ArrayList<>());
            for (Tag tg : tags) {
                project.getTags().add(tagRepository.findByTag(tg.getTag()));
            }

            List<Image> auxImg = new ArrayList<>();
            Optional<Image> img;
            for (Image imgs: project.getUrl()) {
                img = proj.getUrl().stream().filter(u -> u.getUrl().equals(imgs.getUrl())).findFirst();
                if(img.isEmpty()){
                    auxImg.add(imgs);
                }
            }

            if(!auxImg.isEmpty()){
                proj.getUrl().addAll(auxImg);
            }

            projectRepository.save(project);
            msgDto.setStatus(HttpStatus.OK);
            msgDto.setMsg("The project has been updated successfully.");

        }catch (UserNotExistException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("The project must have a user.");
        }catch (BasicDataProjectNotFoundException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("All project must have name, description and content.");
        }catch (EntityNotFoundException e){
            msgDto.setStatus(HttpStatus.NOT_FOUND);
            msgDto.setMsg("The project you are wanting to edit doesn't exist.");
        }catch (ProfitChangeException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("The project's profit can't change when the projects have published.");
        }/*catch (TagFormatException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("You can't edit the tags inside a project instead, you can create a new one or remove.");
        }catch (TagNameNotFoundException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("You need to specify a text for the tag.");
        }*/catch (ProjectNotEditableException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("You can't edit an inactive project.");
        }catch (ObjetiveChangeException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("You can't edit the profit once the project has been published.");
        }catch (Exception e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("Verify you are inserting the right values in each field.");
        }

        return msgDto;
    }

    public MessageDTO unpublishProject (Integer id){
        MessageDTO msgDto = new MessageDTO();
        try {

            Project project = projectRepository.getById(id);

            project.setPublished(false);

            projectRepository.save(project);
            msgDto.setStatus(HttpStatus.OK);
            msgDto.setMsg("The project has been unpublished successfully.");

        }catch (EntityNotFoundException e){
            msgDto.setStatus(HttpStatus.NOT_FOUND);
            msgDto.setMsg("The project you are looking for doesn't exist.");
        }
        return msgDto;
    }

    public MessageDTO publishProject (Integer id){
        MessageDTO msgDto = new MessageDTO();
        try {

            Project project = projectRepository.getById(id);

            project.setPublished(true);

            projectRepository.save(project);
            msgDto.setStatus(HttpStatus.OK);
            msgDto.setMsg("The project has been published successfully.");

        }catch (EntityNotFoundException e){
            msgDto.setStatus(HttpStatus.NOT_FOUND);
            msgDto.setMsg("The project you are looking for doesn't exist.");
        }
        return msgDto;
    }

    public MessageDTO deleteProject (Integer id){
        MessageDTO msgDto = new MessageDTO();
        try {

            Project project = projectRepository.getById(id);

            project.setStatus("I");

            projectRepository.save(project);
            msgDto.setStatus(HttpStatus.OK);
            msgDto.setMsg("The project has been inactivated successfully.");

        }catch (EntityNotFoundException e){
            msgDto.setStatus(HttpStatus.NOT_FOUND);
            msgDto.setMsg("The project you are looking for doesn't exist.");
        }
        return msgDto;
    }

    public MessageDTO activateProject (Integer id){
        MessageDTO msgDto = new MessageDTO();
        try {

            Project project = projectRepository.getById(id);

            project.setStatus("A");

            projectRepository.save(project);
            msgDto.setStatus(HttpStatus.OK);
            msgDto.setMsg("The project has been activated successfully.");

        }catch (EntityNotFoundException e){
            msgDto.setStatus(HttpStatus.NOT_FOUND);
            msgDto.setMsg("The project you are looking for doesn't exist.");
        }
        return msgDto;
    }

    public List<Project> findByTag (String tagName){
        return this.projectRepository.findByTag(tagName);
    }

    public List<Project> getAllNotPublish(){
        return this.projectRepository.findByPublished(false);
    }
}