package com.behind.the.project.controller;


import com.behind.the.project.domain.Vote;
import com.behind.the.project.dto.MessageDTO;
import com.behind.the.project.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vote")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PostMapping
    public ResponseEntity<MessageDTO> createVote(@RequestBody Vote vote){
        MessageDTO msgDto = voteService.createVote(vote);
        return new ResponseEntity<MessageDTO>(msgDto, msgDto.getStatus());
    }

    @GetMapping("/all-votes/{idUser}")
    public ResponseEntity findAllVotesByIdUser(@PathVariable Integer idUser){
        Object obj = voteService.findVotesByUser(idUser);
        if(obj instanceof MessageDTO){
            return new ResponseEntity((MessageDTO)obj,((MessageDTO) obj).getStatus());
        }else{
            return new ResponseEntity((List<Vote>)obj, HttpStatus.OK);
        }
    }
}
