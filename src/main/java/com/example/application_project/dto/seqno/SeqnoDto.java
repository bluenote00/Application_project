package com.example.application_project.dto.seqno;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeqnoDto {
    private String custNo;
    private String crdNo;
}
