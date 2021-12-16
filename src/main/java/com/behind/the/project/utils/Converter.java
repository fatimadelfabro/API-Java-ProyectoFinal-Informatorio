package com.behind.the.project.utils;

import com.behind.the.project.domain.Project;
import com.behind.the.project.domain.Users;
import com.behind.the.project.domain.Vote;
import com.behind.the.project.dto.ProjectDTO;
import com.behind.the.project.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class Converter {

    public static List<ProjectDTO> convertToListOfProjectDTOS(List<Project> projects){
        List<ProjectDTO> dtos = new ArrayList<>();
        ProjectDTO projectDTO;
        for (Project p: projects) {
            projectDTO = new ProjectDTO();
            projectDTO.setProjectName(p.getName());
            for (Users usr: p.getUsers()) {
                projectDTO.getUsers().add(new UserDTO(usr.getEmail(),usr.getType()));
            }
            for (Vote like: p.getLike()) {
                projectDTO.setLikes(projectDTO.getLikes()+1);
            }
            dtos.add(projectDTO);
        }
        return dtos;
    }
}
