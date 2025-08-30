package com.example.application_project.entity.application;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Table(name = "RCVAPPL")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationEntity {


    @Id
    @Column(name = "RCV_SEQ_NO", nullable =false, length = 11)
    private String rcvSeqNo;

    @Column(name = "RCV_D")
    private LocalDate rcvD;

    @Column(name = "SSN")
    private String ssn;

    @Column(name = "APPL_D")
    private LocalDate applD;

    @Column(name = "BIRTH_D")
    private String birthD;

    @Column(name = "HG_NM")
    private String hgNm;

    @Column(name = "ENG_NM")
    private String engNm;

    @Column(name = "STL_MTD")
    private String stlMtd;

    @Column(name = "STL_ACT")
    private String stlAct;

    @Column(name = "BNK_CD")
    private String bnkCd;

    @Column(name = "STL_DD")
    private String stlDd;

    @Column(name = "MGT_BBRN")
    private String mgtBbrn;

    @Column(name = "APPL_CLAS")
    private String applClas;

    @Column(name = "STMT_SND_MTD")
    private String stmtSndMtd;

    @Column(name = "BILLADR_ADR1")
    private String billadrAdr1;

    @Column(name = "BILLADR_ADR2")
    private String billadrAdr2;

    @Column(name = "BILLADR_ZIP")
    private String billadrZip;

    @Column(name = "HDP_NO")
    private String hdpNo;

    @Column(name = "BRD")
    private String brd;

    @Column(name = "SCRT_NO")
    private String scrtNo;

    @Column(name = "EMAIL_ADR")
    private String emailAdr;

    @Column(name = "CRD_NO")
    private String crdNo;

    @Column(name = "IMPSB_CLAS")
    private String impsbClas;

    @Column(name = "IMPSB_CD")
    private String impsbCd;

    @Column(name = "LST_OPR_TM")
    private String lstOprTm;

    @Column(name = "LST_OPR_D")
    private String lstOprD;

    @Column(name = "LST_OPRT_EMPNO")
    private String lstOprtEmpno;

    public void updateCrdNo(String crdNo, String lstOprD, String lstOprTm, String lstOprtEmpno) {
        this.crdNo = crdNo;
        this.lstOprD = lstOprD;
        this.lstOprTm = lstOprTm;
        this.lstOprtEmpno = lstOprtEmpno;
    }
}
