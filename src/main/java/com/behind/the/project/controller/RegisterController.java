package com.behind.the.project.controller;

import com.behind.the.project.domain.Register;
import com.behind.the.project.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/proccesor")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("/process")
    public void process(@RequestBody List<Register> registers){
        registerService.process(registers);
    }
}
