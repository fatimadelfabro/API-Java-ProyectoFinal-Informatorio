package com.behind.the.project.dto;

import org.springframework.http.HttpStatus;

public class MessageDTO {
    private HttpStatus status;
    private String msg;
    private Object additionalItems;

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getAdditionalItems() {
        return additionalItems;
    }

    public void setAdditionalItems(Object additionalItems) {
        this.additionalItems = additionalItems;
    }
}
