package com.example.application_project.dto.login;

import lombok.*;

@Data
@Builder
public class LoginDto {
    private String status;
    private String message;
    private String loginId;
    private String loginPw;
    private String name;
    private String userRole;
}
