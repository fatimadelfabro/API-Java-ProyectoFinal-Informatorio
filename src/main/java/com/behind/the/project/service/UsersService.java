package com.behind.the.project.service;

import com.behind.the.project.domain.City;
import com.behind.the.project.dto.MessageDTO;
import com.behind.the.project.domain.Users;
import com.behind.the.project.exception.*;
import com.behind.the.project.repository.CityRepository;
import com.behind.the.project.repository.UsersRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UsersService {

    private UsersRepository usersRepository;

    private CityRepository cityRepository;

    private static final Pattern pattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");

    @Autowired
    public UsersService(UsersRepository usersRepository, CityRepository cityRepository) {
        this.usersRepository = usersRepository;
        this.cityRepository = cityRepository;
    }

    public List<Users> findByCity(String cityName){

        City city = cityRepository.findByCity(cityName);

        return this.usersRepository.findByCity(city);
    }

    public MessageDTO createUser(Users user) {
        MessageDTO msgDto = new MessageDTO();
        try{
            if(user.getPassword() == null || user.getPassword().isEmpty()){
                throw new NotPasswordPresentException();
            }

            if(user.getEmail() == null || user.getEmail().isEmpty()){
                throw new NotEmailPressentException();
            }

            if(!pattern.matcher(user.getEmail()).matches()){
                throw new EmailFormatException();
            }

            Users usr = usersRepository.findByEmail(user.getEmail());
            if(usr != null){
                throw new DuplicateEmailException();
            }

            user.setCreatedDate(LocalDate.now());
            user.setStatus("A");
            usersRepository.save(user);
            msgDto.setStatus(HttpStatus.CREATED);
            msgDto.setMsg("The user has been created.");

        }catch (NotPasswordPresentException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("The user must have a password.");
        }catch (NotEmailPressentException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("The user must have am email.");
        }catch (DuplicateEmailException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("The email used by the user must be unique.");
        }catch (EmailFormatException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("The email must have the form of test@email.com.");
        }

        return msgDto;
    }

    public MessageDTO updateUser(Users user) {
        MessageDTO msgDto = new MessageDTO();
        try{
            if(user.getEmail() == null || user.getEmail().isEmpty()){
                throw new NotEmailPressentException();
            }

            if(!pattern.matcher(user.getEmail()).matches()){
                throw new EmailFormatException();
            }

            Users usr = usersRepository.findByEmail(user.getEmail());
            if(usr == null){
                throw new UserNotFoundException();
            }

            if(user.getPassword() == null || user.getPassword().isEmpty()){
                throw new NotPasswordPresentException();
            }

            usr.setName(user.getName());
            usr.setLastname(user.getLastname());
            usr.setPassword(user.getPassword());

            usersRepository.save(usr);
            msgDto.setStatus(HttpStatus.OK);
            msgDto.setMsg("The user has been updated successfully.");

        }catch (NotPasswordPresentException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("The user must have a password.");
        }catch (NotEmailPressentException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("The user must have am email.");
        }catch (UserNotFoundException e){
            msgDto.setStatus(HttpStatus.NOT_FOUND);
            msgDto.setMsg("The user you are looking for doesn't exist.");
        }catch (EmailFormatException e){
            msgDto.setStatus(HttpStatus.BAD_REQUEST);
            msgDto.setMsg("The email must have the form of test@email.com.");
        }

        return msgDto;
    }

    public MessageDTO deleteUser(Integer id) {
        MessageDTO msgDto = new MessageDTO();
        try{

            Users usr = usersRepository.getById(id);

            usr.setStatus("I");

            usersRepository.save(usr);
            msgDto.setStatus(HttpStatus.OK);
            msgDto.setMsg("The user has been inactivated successfully.");

        }catch (EntityNotFoundException e){
            msgDto.setStatus(HttpStatus.NOT_FOUND);
            msgDto.setMsg("The user you are looking for doesn't exist.");
        }

        return msgDto;
    }

    public MessageDTO activateUser(Integer id) {
        MessageDTO msgDto = new MessageDTO();
        try{

            Users usr = usersRepository.getById(id);

            usr.setStatus("A");

            usersRepository.save(usr);
            msgDto.setStatus(HttpStatus.OK);
            msgDto.setMsg("The user has been activated successfully.");

        }catch (EntityNotFoundException e){
            msgDto.setStatus(HttpStatus.NOT_FOUND);
            msgDto.setMsg("The user you are looking for doesn't exist.");
        }

        return msgDto;
    }
}
