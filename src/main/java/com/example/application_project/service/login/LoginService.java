package com.example.application_project.service.login;

import com.example.application_project.dto.login.LoginDto;
import com.example.application_project.repository.login.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final LoginRepository loginRepository;

    public LoginDto login(LoginDto loginDto) {
        return loginRepository.findByLoginId(loginDto.getLoginId())
                .map(user -> {
                    if (loginDto.getLoginPw().equals(user.getLoginPw())) {
                        return LoginDto.builder()
                                .status("success")
                                .message("로그인되었습니다.")
                                .loginId(user.getLoginId())
                                .name(user.getName())
                                .userRole(user.getUserRole())
                                .build();
                    } else {
                        return LoginDto.builder()
                                .status("fail")
                                .message("잘못된 비밀번호입니다.")
                                .build();
                    }
                })
                .orElse(LoginDto.builder()
                        .status("null")
                        .message("존재하지 않는 회원입니다.")
                        .build());
    }
}
