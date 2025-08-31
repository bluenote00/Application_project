package com.example.application_project.repository.noseq;

import com.example.application_project.dto.application.ApplicationDto;
import com.example.application_project.entity.login.LoginEntity;
import com.example.application_project.entity.noseq.NoseqEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface NoseqRepository extends JpaRepository<NoseqEntity, String> {
        @Modifying
        @Transactional
        @Query(value = """
        UPDATE NOSEQTBL
           SET RCV_D = SYSDATE
         WHERE RCV_SEQ_NO = :#{#dto.rcvSeqNo}
        """, nativeQuery = true)
        int updateNoseq(@Param("dto") ApplicationDto dto);

}
