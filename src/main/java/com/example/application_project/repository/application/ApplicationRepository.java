package com.example.application_project.repository.application;

import com.example.application_project.entity.application.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, String> {

    // 입회신청서 조회
    Optional<ApplicationEntity> findBySsnAndRcvDAndRcvSeqNo(int ssn, String rcvD, int rcvSeqNo);
}
