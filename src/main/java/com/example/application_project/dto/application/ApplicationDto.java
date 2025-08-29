package com.example.application_project.dto.application;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ApplicationDto {
    private String status;
    private String message;
    private String custNo;
    private String stlYn;
    private String rcvSeqNo;
    private LocalDate rcvD;
    private String ssn;
    private LocalDate applD;
    private String birthD;
    private String hgNm;
    private String engNm;
    private String stlMtd;
    private String stlAct;
    private String stlActCheck;
    private String bnkCd;
    private String stlDd;
    private String mgtBbrn;
    private String applClas;
    private String stmtSndMtd;
    private String billadrAdr1;
    private String billadrAdr2;
    private String billadrZip;
    private String hdpNo;
    private String brd;
    private String scrtNo;
    private String emailAdr;
    private String crdNo;
    private String impsbClas;
    private String impsbCd;
    private String impsbMsg;
    private String lstOprTm;
    private String lstOprD;
    private String lstOprtEmpno;
}
