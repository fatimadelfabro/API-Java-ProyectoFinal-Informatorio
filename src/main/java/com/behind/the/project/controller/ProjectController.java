package com.behind.the.project.controller;

import com.behind.the.project.domain.*;
import com.behind.the.project.dto.MessageDTO;
import com.behind.the.project.repository.*;
import com.behind.the.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<MessageDTO> createProject(@RequestBody Project project){
        MessageDTO msgDto = projectService.createProject(project);
        return new ResponseEntity<MessageDTO>(msgDto, msgDto.getStatus());
    }

    @GetMapping("/findAll")
    public List<Project> getAllProject(){
        return this.projectRepository.findAll();
    }

    @GetMapping("/getAllOneTag/{tag}")
    public List<Project> getAllOneTag(@PathVariable String tag){
        return this.projectService.findByTag(tag);
    }

    @PutMapping
    public ResponseEntity<MessageDTO> updatePoject(@RequestBody Project project){
        MessageDTO msgDto = projectService.updateProject(project);
        return new ResponseEntity<MessageDTO>(msgDto, msgDto.getStatus());
    }

    @GetMapping("/unpublish/{id}")
    public ResponseEntity<MessageDTO> unpublishProject(@PathVariable Integer id){
        MessageDTO msgDto = projectService.unpublishProject(id);
        return new ResponseEntity<MessageDTO>(msgDto, msgDto.getStatus());
    }

    @GetMapping("/publish/{id}")
    public ResponseEntity<MessageDTO> publishProject (@PathVariable Integer id){
        MessageDTO msgDto = projectService.publishProject(id);
        return new ResponseEntity<MessageDTO>(msgDto, msgDto.getStatus());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> deleteProject (@PathVariable Integer id){
        MessageDTO msgDto = projectService.deleteProject(id);
        return new ResponseEntity<MessageDTO>(msgDto, msgDto.getStatus());
    }

    @GetMapping("/activateProject/{id}")
    public ResponseEntity<MessageDTO> activateProject (@PathVariable Integer id){
        MessageDTO msgDto = projectService.activateProject(id);
        return new ResponseEntity<MessageDTO>(msgDto, msgDto.getStatus());
    }

    @GetMapping("/unpublishProject")
    public ResponseEntity activateProject (){
        List<Project> projectsUnpublish = projectService.getAllNotPublish();
        return new ResponseEntity(projectsUnpublish, HttpStatus.OK);
    }
}
