package com.behind.the.project.controller;

import com.behind.the.project.domain.Province;
import com.behind.the.project.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/province")
public class ProvinceController {
    @Autowired
    private ProvinceRepository provinceRepository;

    @PostMapping
    public ResponseEntity<?> createProvince(@RequestBody @Valid Province province){
        return new ResponseEntity<>(provinceRepository.save(province), HttpStatus.CREATED);
    }

    @GetMapping("/findAll")
    public List<Province> getAllProvince(){
        return this.provinceRepository.findAll();
    }

    @PutMapping
    public Province upgradeProvince(@RequestBody @Valid Province province) {
        Province provinceExisting = provinceRepository.findById(province.getId()).get();
        provinceExisting.setProvince(province.getProvince());
        return provinceRepository.save(provinceExisting);
    }

    @DeleteMapping("/{id}")
    public void deleteProvince(@PathVariable String id){
        this.provinceRepository.deleteById(Integer.valueOf(id));
    }
}