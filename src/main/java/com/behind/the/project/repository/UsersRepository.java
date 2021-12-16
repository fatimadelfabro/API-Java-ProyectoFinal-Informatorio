package com.behind.the.project.repository;

import com.behind.the.project.domain.City;
import com.behind.the.project.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    List<Users> findByCity(City city);

    Users findByEmail(String email);

    @Query(value = "select * from users u where u.created_date > ?1", nativeQuery = true)
    List<Users> findByCreatedDate(LocalDate date);
}
