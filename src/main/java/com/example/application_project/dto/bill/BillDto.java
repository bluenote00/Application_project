package com.example.application_project.dto.bill;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BillDto {
    private String custNo;
    private String stlAct;
    private String bnkCd;
    private String dpsNm;
    private String stlMTD;
    private String StlDd;
    private String prcsClas;
    private String stmtSndMtd;
    private String stmtDeniClas;
    private String billZip;
    private String billAdr1;
    private String billAdr2;
    private String emailAdr;
    private String lstOprTm;
    private String lstOprD;
    private String lstOprtEmpno;
}
