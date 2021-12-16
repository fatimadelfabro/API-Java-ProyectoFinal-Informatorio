package com.behind.the.project.controller;

import com.behind.the.project.domain.Tag;
import com.behind.the.project.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagRepository tagRepository;

    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody Tag tag){
        return new ResponseEntity<>(tagRepository.save(tag), HttpStatus.CREATED);
    }

    @GetMapping("/findAll")
    public List<Tag> getAllTag(){
        return this.tagRepository.findAll();
    }

    @PutMapping
    public Tag upgradeTag(@RequestBody @Valid Tag tag){
        Tag tagExisting = tagRepository.findById(tag.getId()).get();
        tagExisting.setTag(tag.getTag());
        return tagRepository.save(tagExisting);
    }
    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable String id){
        this.tagRepository.deleteById(Integer.valueOf(id));
    }
}
