package com.example.application_project.entity.code;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CODE")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeEntity {

    @Id
    @Column(name = "GROUP_CODE_ID", nullable =false)
    private String groupCodeId;

    @Column(name = "GROUP_CODE_NAME")
    private String groupCodeName;

    @Column(name = "DETAIL_CODE_ID")
    private String detailCodeId;

    @Column(name = "DETAIL_CODE_NAME")
    private String detailCodeName;
}
