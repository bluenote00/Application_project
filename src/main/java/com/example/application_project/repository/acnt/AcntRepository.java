package com.example.application_project.repository.acnt;

import com.example.application_project.entity.acnt.AcntEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcntRepository extends JpaRepository<AcntEntity, String> {
    int countByAcntCodeAndAcntNum(String acntCode, String acntNum);
}
