package com.example.application_project.entity.noseq;

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
@Table(name = "NOSEQTBL")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoseqEntity {

    @Id
    @Column(name = "RCV_D", nullable =false)
    private LocalDate rcvD;

    @Id
    @Column(name = "RCV_SEQ_NO", nullable =false)
    private String rcvSeqNo;
}
