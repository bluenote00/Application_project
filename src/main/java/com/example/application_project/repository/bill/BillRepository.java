package com.example.application_project.repository.bill;

import com.example.application_project.entity.bill.BillEntity;
import com.example.application_project.entity.card.CrdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, String> {
}

