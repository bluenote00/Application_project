package com.example.application_project.repository.application;

import com.example.application_project.entity.application.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, String> {

    // 입회신청서 조회
    Optional<ApplicationEntity> findBySsnAndRcvDAndRcvSeqNo(String ssn, LocalDate rcvD, int rcvSeqNo);

    // 입회신청서 신청 등록
    Optional<ApplicationEntity> findBySsnAndRcvDAndRcvSeqNo(String ssn, LocalDate rcvD, int rcvSeqNo);
}
