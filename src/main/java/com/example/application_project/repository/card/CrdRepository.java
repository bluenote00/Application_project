package com.example.application_project.repository.card;

import com.example.application_project.entity.acnt.AcntEntity;
import com.example.application_project.entity.card.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrdRepository extends JpaRepository<CardEntity, String> {
    int countBySsnOrBrd(String ssn, String brd);
}
