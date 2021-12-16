package com.behind.the.project.repository;

import com.behind.the.project.domain.Project;
import com.behind.the.project.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Query(value = "select * from project p inner join tag_projects tp on p.id = tp.projects_id inner join tag t on tp.tag_id = t.id where t.tag  like %?1%", nativeQuery = true)
    List<Project> findByTag(String tag);

    List<Project> findByPublished(boolean b);
}
