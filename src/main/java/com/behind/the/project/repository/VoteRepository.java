package com.behind.the.project.repository;

import com.behind.the.project.domain.Users;
import com.behind.the.project.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {
    List<Vote> findByUser(Users user);
}
