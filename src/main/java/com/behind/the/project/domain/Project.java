package com.behind.the.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToMany
    private List<Users> users;
    @NonNull
    @Column(length = 80)
    private String name;
    @Column(length = 150)
    private String description;
    private String content;
    private LocalDate createdDate;
    private BigDecimal objective;
    private boolean published;
    @OneToMany
    private List<Image> url;
    @ManyToMany
    @JoinTable(name = "tag_projects",
            joinColumns = @JoinColumn(name = "projects_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private List<Tag> tags;
    //@ElementCollection
    //private List<String> tags = new ArrayList<>();
    @OneToMany(mappedBy = "project")
    private List<Vote> like;
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Event event;
    /**
     * A = ACTIVE
     * I = INACTIVE
     */
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Users> getUsers() {
        if(users==null)
            this.users = new ArrayList<>();
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public BigDecimal getObjective() {
        return objective;
    }

    public void setObjective(BigDecimal objective) {
        this.objective = objective;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public List<Image> getUrl() {
        if(url == null)
            this.url = new ArrayList<>();
        return url;
    }

    public void setUrl(List<Image> url) {
        this.url = url;
    }

    public List<Tag> getTags() {
        if(tags==null)
            this.tags = new ArrayList<>();
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Vote> getLike() {
        if(like==null)
            this.like = new ArrayList<>();
        return like;
    }

    public Integer countLikes(){
        return this.like.size();
    }

    public void setLike(List<Vote> like) {
        this.like = like;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
