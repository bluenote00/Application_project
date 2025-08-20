package com.example.application_project.entity.bill;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bill")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillEntity {

    @Id
    @Column(name = "STL_ACT", nullable = false)
    private String stlAct;

    @Column(name = "BNK_CD")
    private String bnkCd;

    @Column(name = "DPS_NM")
    private String dpsNm;

    @Column(name = "STL_MTD")
    private String stlMTD;

    @Column(name = "STL_DD")
    private String StlDd;

    @Column(name = "CUST_NO")
    private String custNo;

    @Column(name = "PRCS_CLAS")
    private String prcsClas;

    @Column(name = "STMT_SND_MTD")
    private String stmtSndMtd;

    @Column(name = "STMT_DENI_CLAS")
    private String stmtDeniClas;

    @Column(name = "BILL_ZIP")
    private String billZip;

    @Column(name = "BILL_ADR1")
    private String billAdr1;

    @Column(name = "BILL_ADR2")
    private String billAdr2;

    @Column(name = "EMAIL_ADR")
    private String emailAdr;

    @Column(name = "LST_OPR_TM")
    private String lstOprTm;

    @Column(name = "LST_OPR_D")
    private String lstOprD;

    @Column(name = "LST_OPRT_EMPNO")
    private String lstOprtEmpno;
}
