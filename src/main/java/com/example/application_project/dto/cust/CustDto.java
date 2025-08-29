package com.example.application_project.dto.cust;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CustDto {
    private String custNo;
    private String ssn;
    private LocalDate regD;
    private String hgNm;
    private String birthD;
    private String hdpNo;
    private String lstOprTm;
    private String lstOprD;
    private String lstOprtEmpno;
}
