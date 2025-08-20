package com.example.application_project.entity.card;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "card")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardEntity {

    @Id
    @Column(name = "CRD_NO", nullable =false)
    private String crdNo;

    @Column(name = "CUST_NO")
    private String custNo;

    @Column(name = "MGT_BBRN")
    private String mgtBbrn;

    @Column(name = "REG_D")
    private String regD;

    @Column(name = "SSN")
    private String ssn;

    @Column(name = "VLD_DUR")
    private String vldDur;

    @Column(name = "BRD")
    private String brd;

    @Column(name = "SCRT_NO")
    private String scrtNo;

    @Column(name = "ENG_NM")
    private String engNm;

    @Column(name = "BF_CRD_NO")
    private String bfCrdNo;

    @Column(name = "LST_CRD_F")
    private String lstCrdF;

    @Column(name = "FST_REG_D")
    private String fstRegD;

    @Column(name = "CRD_GRD")
    private String crdGrd;

    @Column(name = "LST_OPR_TM")
    private String lstOprTm;

    @Column(name = "LST_OPR_D")
    private String lstOprD;

    @Column(name = "LST_OPRT_EMPNO")
    private String lstOprtEmpno;

}
