package com.example.application_project.repository.cust;

import com.example.application_project.entity.cust.CustEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustRepository extends JpaRepository<CustEntity, String> {

    @Query(value = "SELECT MAX(c.CUST_NO) FROM CUST c", nativeQuery = true)
    String findMaxCustNo();

    // 해당 되는 주민 번호로 고객 찾기
    Optional<CustEntity> findBySsn(String ssn);

}
