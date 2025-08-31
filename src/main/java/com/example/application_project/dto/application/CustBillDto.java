package com.example.application_project.dto.application;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustBillDto {
    private String ssn;
    private String hgNm;
    private String birthD;
    private String hdpNo;
    private String bnkCd;
    private String stlAct;
    private String billAdr1;
}