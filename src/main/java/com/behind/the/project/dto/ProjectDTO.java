package com.behind.the.project.dto;

import java.util.ArrayList;
import java.util.List;

public class ProjectDTO {

    private List<UserDTO> users;
    private String projectName;
    private Integer likes = 0;

    public ProjectDTO(List<UserDTO> users, String projectName, Integer likes) {
        this.users = users;
        this.projectName = projectName;
        this.likes = likes;
    }

    public ProjectDTO() {
    }

    public List<UserDTO> getUsers() {
        if(users == null)
            users = new ArrayList<>();
        return users;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }
}
