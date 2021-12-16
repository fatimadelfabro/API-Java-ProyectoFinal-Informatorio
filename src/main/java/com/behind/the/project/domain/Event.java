package com.behind.the.project.domain;

import com.behind.the.project.dto.ProjectDTO;
import com.behind.the.project.utils.Converter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String eventsDetail;
    private LocalDate startDate;
    private LocalDate endDate;
    private EventState eventState;
    private BigDecimal reward;
    @OneToMany(mappedBy = "event")
    @JsonIgnore
    private List<Project> projects;
    @Transient
    private List<ProjectDTO> projectDTOS;
    /**
     * A=ACTIVE
     * I=INACTIVE
     */
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEventsDetail() {
        return eventsDetail;
    }

    public void setEventsDetail(String eventsDetail) {
        this.eventsDetail = eventsDetail;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public EventState getEventState() {
        return eventState;
    }

    public void setEventState(EventState eventState) {
        this.eventState = eventState;
    }

    public BigDecimal getReward() {
        return reward;
    }

    public void setReward(BigDecimal reward) {
        this.reward = reward;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProjectDTO> getProjectDTOS() {
        return projectDTOS = Converter.convertToListOfProjectDTOS(this.projects);
    }
}
