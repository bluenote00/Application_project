package com.example.application_project.entity.cust;

import com.example.application_project.dto.application.ApplicationDto;
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
@Table(name = "CUST")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustEntity {

    @Id
    @Column(name = "CUST_NO", nullable =false)
    private String custNo;

    @Column(name = "SSN")
    private String ssn;

    @Column(name = "REG_D")
    private LocalDate regD;

    @Column(name = "HG_NM")
    private String hgNM;

    @Column(name = "BIRTH_D")
    private String birthD;

    @Column(name = "HDP_NO")
    private String hdpNO;

    @Column(name = "LST_OPR_TM")
    private String lstOprTm;

    @Column(name = "LST_OPR_D")
    private String lstOprD;

    @Column(name = "LST_OPRT_EMPNO")
    private String lstOprtEmpno;

    public void updateFromDto(ApplicationDto dto, String todayDate, String currentTime, String loginId) {
        this.hgNM = dto.getHgNm();
        this.birthD = dto.getBirthD();
        this.hdpNO = dto.getHdpNo();
        this.lstOprD = todayDate;
        this.lstOprTm = currentTime;
        this.lstOprtEmpno = loginId;
    }

}
