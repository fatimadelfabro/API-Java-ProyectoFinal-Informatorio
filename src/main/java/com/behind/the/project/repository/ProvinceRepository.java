package com.behind.the.project.repository;

import com.behind.the.project.domain.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Integer> {
    Province findByProvince(String province);
}
