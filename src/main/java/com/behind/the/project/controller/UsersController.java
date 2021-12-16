package com.behind.the.project.controller;

import com.behind.the.project.domain.Country;
import com.behind.the.project.domain.TypeUser;
import com.behind.the.project.domain.Users;
import com.behind.the.project.dto.MessageDTO;
import com.behind.the.project.repository.UsersRepository;
import com.behind.the.project.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UsersController {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UsersService usersService;

    @PostMapping
    public ResponseEntity<MessageDTO> createUser(@RequestBody Users user){
        MessageDTO msgDto = usersService.createUser(user);
        return new ResponseEntity<MessageDTO>(msgDto, msgDto.getStatus());
    }

    @PutMapping
    public ResponseEntity<MessageDTO> updateUser(@RequestBody Users user){
        MessageDTO msgDto = usersService.updateUser(user);
        return new ResponseEntity<MessageDTO>(msgDto, msgDto.getStatus());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> deleteUser(@PathVariable Integer id){
        MessageDTO msgDto = usersService.deleteUser(id);
        return new ResponseEntity<MessageDTO>(msgDto, msgDto.getStatus());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageDTO> activateUser(@PathVariable Integer id){
        MessageDTO msgDto = usersService.activateUser(id);
        return new ResponseEntity<MessageDTO>(msgDto, msgDto.getStatus());
    }

    @GetMapping("/getAllOneCity/{city}")
    public List<Users> getAllOneCity(@PathVariable String city){
        return this.usersService.findByCity(city);
    }

    @GetMapping("/findAll")
    public List<Users> getAllUsers(){
        return this.usersRepository.findAll();
    }

    @GetMapping("/{year}/{month}/{day}")
    public List<Users> getAllUsersByCreatedDate(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day){
        return this.usersRepository.findByCreatedDate(LocalDate.of(year,month,day));
    }
}
