package com.behind.the.project.repository;

import com.behind.the.project.domain.Event;
import com.behind.the.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    /*@Query(value = "select * from project p inner join event e on p.event_id = e.id inner join vote v on p.id  = v.project_id where e.id = ?1 group by v.project_id",
            countQuery = "select count(*) from project p inner join event e on p.event_id = e.id inner join vote v on p.id  = v.project_id",
            nativeQuery = true)
    List<Project> getRanking(Integer eventId);*/
}
