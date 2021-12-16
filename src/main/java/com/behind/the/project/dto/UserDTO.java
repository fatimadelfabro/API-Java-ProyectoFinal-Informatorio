package com.behind.the.project.dto;

import com.behind.the.project.domain.TypeUser;

public class UserDTO {

    private String email;
    private TypeUser typeUser;

    public UserDTO(String email, TypeUser typeUser) {
        this.email = email;
        this.typeUser = typeUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TypeUser getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(TypeUser typeUser) {
        this.typeUser = typeUser;
    }
}
