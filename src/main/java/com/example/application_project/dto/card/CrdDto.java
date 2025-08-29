package com.example.application_project.dto.card;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CrdDto {
    private String crdNo;
    private String custNo;
    private String mgtBbrn;
    private LocalDate regD;
    private String ssn;
    private String vldDur;
    private String brd;
    private String scrtNo;
    private String engNm;
    private String bfCrdNo;
    private String lstCrdF;
    private LocalDate fstRegD;
    private String crdGrd;
    private String lstOprTm;
    private String lstOprD;
    private String lstOprtEmpno;
}
