package com.behind.the.project.controller;

import com.behind.the.project.domain.Country;
import com.behind.the.project.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/country")
public class CountryController {
    @Autowired
    private CountryRepository countryRepository;

    @PostMapping
    public ResponseEntity<?> createCountry(@RequestBody @Valid Country country){
        return new ResponseEntity<>(countryRepository.save(country), HttpStatus.CREATED);
    }

    @GetMapping("/findAll")
    public List<Country> getAllCountries(){
        return this.countryRepository.findAll();
    }

    @PutMapping
    public Country upgradeCountry(@RequestBody @Valid Country country){
        Country countryExisting = countryRepository.findById(country.getId()).get();
        countryExisting.setCountry(country.getCountry());
        return countryRepository.save(countryExisting);
    }

    @DeleteMapping("{id}")
    public void deleteCountry(@PathVariable String id){
        this.countryRepository.deleteById(Integer.valueOf(id));
    }
}
