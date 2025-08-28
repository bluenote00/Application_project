package com.example.application_project.dto.card;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardDto {
    private String crdNo;
    private String custNo;
    private String mgtBbrn;
    private String regD;
    private String ssn;
    private String vldDur;
    private String brd;
    private String scrtNo;
    private String engNm;
    private String bfCrdNo;
    private String lstCrdF;
    private String fstRegD;
    private String crdGrd;
    private String lstOprTm;
    private String lstOprD;
    private String lstOprtEmpno;
}
