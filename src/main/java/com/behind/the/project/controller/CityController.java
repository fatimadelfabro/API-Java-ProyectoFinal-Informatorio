package com.behind.the.project.controller;

import com.behind.the.project.domain.City;
import com.behind.the.project.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/city")
public class CityController {
    @Autowired
    private CityRepository cityRepository;

    @PostMapping
    public ResponseEntity<?> createCity(@RequestBody @Valid City city){
        return new ResponseEntity<>(cityRepository.save(city), HttpStatus.CREATED);
    }

    @GetMapping("/findAll")
    public List<City> getAllCities (){
        return this.cityRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        this.cityRepository.deleteById(Integer.valueOf(id));
    }

    @PutMapping
    public City upgradeCity(@RequestBody @Valid City city){
        City cityExisting = cityRepository.findById(city.getId()).get();
        cityExisting.setCity(city.getCity());
        return cityRepository.save(cityExisting);
    }
}
