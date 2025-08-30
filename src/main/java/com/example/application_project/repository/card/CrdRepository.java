package com.example.application_project.repository.card;

import com.example.application_project.entity.card.CrdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CrdRepository extends JpaRepository<CrdEntity, String> {
    int countBySsn(String ssn);

    int countBySsnAndBrd(String ssn, String brd);

    @Query("SELECT MAX(c.crdNo) FROM CrdEntity c")
    String findMaxCrdNo();

}



