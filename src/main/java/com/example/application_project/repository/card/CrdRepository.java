package com.example.application_project.repository.card;

import com.example.application_project.entity.card.CrdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrdRepository extends JpaRepository<CrdEntity, String> {
    int countBySsn(String ssn);

    int countBySsnAndBrd(String ssn, String brd);

    @Query("SELECT MAX(c.crdNo) FROM CrdEntity c")
    String findMaxCrdNo();

    // 이전 카드 번호 찾기
    Optional<CrdEntity> findBySsnAndBrdAndLstCrdF(String ssn, String brd, String lstCrdF);

    // 카드 번호 찾기
    Optional<CrdEntity> findBySsnAndCrdNo(String ssn, String crdNo);

    // 카드 리스트 상세 조회
    Optional<CrdEntity> findByCrdNoAndSsn(String crdNo, String ssn);
}



