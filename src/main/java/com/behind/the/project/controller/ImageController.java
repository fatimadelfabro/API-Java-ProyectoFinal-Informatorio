package com.behind.the.project.controller;

import com.behind.the.project.domain.Image;
import com.behind.the.project.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private ImageRepository imageRepository;

    @PostMapping
    public ResponseEntity<?> createImage(@RequestBody Image image){
        return new  ResponseEntity<>(imageRepository.save(image), HttpStatus.CREATED);
    }

    @GetMapping("/findAll")
    public List<Image> getAllImage(){
        return this.imageRepository.findAll();
    }

    @PutMapping
    public Image upgradeImage(@RequestBody @Valid Image image){
        Image imageExisting = imageRepository.findById(image.getId()).get();
        imageExisting.setUrl(image.getUrl());
        return imageRepository.save(imageExisting);
    }

    @DeleteMapping("/{id}")
    public void deleteImage(@PathVariable String id){
        this.imageRepository.deleteById(Integer.valueOf(id));
    }
}
