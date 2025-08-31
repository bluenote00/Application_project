package com.example.application_project.repository.bill;

import com.example.application_project.entity.bill.BillEntity;
import com.example.application_project.entity.card.CrdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, String> {

    // 해당되는 고객 번호 찾기
    Optional<BillEntity> findByCustNo(String custNo);


}

