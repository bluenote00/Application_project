package com.example.application_project.dto.cardDetail;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CardDetailDto {
    // CUST
    private String hgNm;
    private String hdpNo;
    private String custNo;
    private LocalDate regD;

    // CRD
    private String crdNo;
    private String engNm;
    private String vldDur;
    private String brd;
    private String crdGrd;
    private String bfCrdNo;
    private String lstCrdF;
    private LocalDate fstRegD;
    private String mgtBbrn;
    private String lstOprD;

    // BILL
    private String stlAct;
    private String stlMtd;
    private String stlDd;
    private String bnkCd;
    private String stmtSndMtd;
    private String emailAdr;
    private String billZip;
    private String billAdr1;
    private String billAdr2;
}
