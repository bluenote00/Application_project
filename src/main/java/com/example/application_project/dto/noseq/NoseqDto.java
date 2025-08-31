package com.example.application_project.dto.noseq;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class NoseqDto {
    private String ncvSeqNo;
    private LocalDate rcvD;
}
