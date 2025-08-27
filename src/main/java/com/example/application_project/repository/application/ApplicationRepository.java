package com.example.application_project.repository.application;

import com.example.application_project.entity.application.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, String> {

    // 입회신청서 조회
    Optional<ApplicationEntity> findBySsnAndRcvDAndRcvSeqNo(String ssn, LocalDate rcvD, String rcvSeqNo);

    // 입회 신청서 등록
    @Query("SELECT MAX(a.rcvSeqNo) FROM ApplicationEntity a WHERE a.rcvSeqNo LIKE CONCAT(:dateStr, '%')")
    String findMaxSeqNoByDate(@Param("dateStr") String dateStr);

    // 당일 중복 신청 체크
    int countBySsnAndRcvD(String ssn, LocalDate rcvD);
    
}
