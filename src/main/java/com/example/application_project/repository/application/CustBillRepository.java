package com.example.application_project.repository.application;

import com.example.application_project.dto.application.CustBillDto;
import com.example.application_project.entity.cust.CustEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
@Repository
public interface CustBillRepository extends CrudRepository<CustEntity, String> {

    @Query("""
        SELECT new com.example.application_project.dto.application.CustBillDto(
            c.ssn, c.hgNM, c.birthD, c.hdpNO,
            b.bnkCd, b.stlAct, b.billAdr1
        )
        FROM CustEntity c
        JOIN BillEntity b ON c.custNo = b.custNo
        WHERE c.ssn = :ssn
          AND c.birthD = :birthD
          AND c.hdpNO = :hdpNo
    """)
    List<CustBillDto> searchUser(@Param("ssn") String ssn,
                                 @Param("birthD") String birthD,
                                 @Param("hdpNo") String hdpNo);
}
