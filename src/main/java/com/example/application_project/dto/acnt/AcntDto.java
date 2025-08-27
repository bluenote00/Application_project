package com.example.application_project.dto.acnt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AcntDto {
    private String acntCode;
    private String acntName;
    private String acntNum;
}
