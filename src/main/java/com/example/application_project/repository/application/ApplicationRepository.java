package com.example.application_project.repository.application;

import com.example.application_project.dto.application.ApplicationDto;
import com.example.application_project.entity.application.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, String> {

    // 입회신청서 조회
    Optional<ApplicationEntity> findBySsnAndRcvDAndRcvSeqNo(String ssn, LocalDate rcvD, String rcvSeqNo);

    // 접수 일련 번호 시퀀스
    @Query("SELECT MAX(a.rcvSeqNo) FROM ApplicationEntity a WHERE a.rcvSeqNo LIKE CONCAT(:dateStr, '%')")
    String findMaxSeqNoByDate(@Param("dateStr") String dateStr);

    // 1. 당일 중복 신청 체크
    int countBySsnAndRcvD(String ssn, LocalDate rcvD);
    
    // 2. 같은 주민 번호 최신 접수 찾기
    @Query("SELECT a FROM ApplicationEntity a WHERE a.ssn = :ssn ORDER BY a.rcvD DESC, a.rcvSeqNo DESC")
    Optional<ApplicationEntity> findLatestBySsn(@Param("ssn") String ssn);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE RCVAPPL
           SET HG_NM = :#{#dto.hgNm},
               ENG_NM = :#{#dto.engNm},
               APPL_CLAS = :#{#dto.applClas},
               RCV_D = TRUNC(SYSDATE),
               BRD = :#{#dto.brd},
               STL_DD = :#{#dto.stlDd},
               STL_MTD = :#{#dto.stlMtd},
               BNK_CD = :#{#dto.bnkCd},
               STL_ACT = :#{#dto.stlAct},
               STMT_SND_MTD = :#{#dto.stmtSndMtd},
               BILLADR_ZIP = :#{#dto.billadrZip},
               BILLADR_ADR1 = :#{#dto.billadrAdr1},
               BILLADR_ADR2 = :#{#dto.billadrAdr2},
               EMAIL_ADR = :#{#dto.emailAdr},
               HDP_NO = :#{#dto.hdpNo},
               SCRT_NO = :scrtNo,
               IMPSB_CLAS = :#{#dto.impsbClas},
               IMPSB_CD = :#{#dto.impsbCd},
               LST_OPR_D = :oprD,
               LST_OPR_TM = :oprTm,
               LST_OPRT_EMPNO = :loginId
         WHERE SSN = :#{#dto.ssn}
           AND RCV_SEQ_NO = :#{#dto.rcvSeqNo}
        """, nativeQuery = true)
    int updateImpApplication(@Param("dto") ApplicationDto dto,
                             @Param("scrtNo") String scrtNo,
                             @Param("oprD") String oprD,
                             @Param("oprTm") String oprTm,
                             @Param("loginId") String loginId);

}
