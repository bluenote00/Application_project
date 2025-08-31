package com.example.application_project.entity.seqno;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SEQNOTBL")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeqnoEntity {

    @Id
    @Column(name = "CUST_NO", nullable =false)
    private String custNo;

    @Column(name = "CRD_NO")
    private String crdNo;

}
