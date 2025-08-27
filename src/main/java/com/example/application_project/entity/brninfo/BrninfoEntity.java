package com.example.application_project.entity.brninfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BRNINFO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrninfoEntity {

    @Id
    @Column(name = "MGT_BBRN", nullable =false)
    private String mgtBbrn;

    @Column(name = "MGT_BBRN_NM")
    private String mgtBbrnNm;

    @Column(name = "BBRN_ZIP")
    private String bbrnZip;

    @Column(name = "BBRN_ADR1")
    private String bbrnAdr1;

    @Column(name = "BBRN_ADR2")
    private String bbrnAdr2;

    @Column(name = "BBRN_TEL_NO")
    private String bbrnTelNo;

    @Column(name = "LST_OPR_TM")
    private String lstOprTm;

    @Column(name = "LST_OPR_D")
    private String lstOprD;

    @Column(name = "LST_OPRT_EMPNO")
    private String lstOprtEmpno;
}
