package com.example.application_project.dto.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LoginDto {
    private String status;   // success, fail, null
    private String message;  // 응답 메시지
    private String loginId;  // 로그인한 ID
    private String name;     // 사용자 이름
}
