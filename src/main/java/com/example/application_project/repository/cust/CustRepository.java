package com.example.application_project.repository.cust;

import com.example.application_project.entity.card.CrdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustRepository extends JpaRepository<CrdEntity, String> {
    int countBySsnOrBrd(String ssn, String brd);
}
