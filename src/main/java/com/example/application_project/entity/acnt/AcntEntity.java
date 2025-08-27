package com.example.application_project.entity.acnt;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ACNT")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcntEntity {
    @Id
    @Column(name = "ACNT_CODE", nullable = false)
    private String acntCode;

    @Column(name = "ACNT_NAME")
    private String acntName;

    @Column(name = "ACNT_NUM")
    private String acntNum;
}
