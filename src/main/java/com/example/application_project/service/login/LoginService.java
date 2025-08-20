package com.example.application_project.service.login;

import com.example.application_project.dto.login.LoginDto;
import com.example.application_project.dto.login.LoginResponseDto;
import com.example.application_project.entity.login.LoginEntity;
import com.example.application_project.repository.login.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private LoginDto loginDto;


    /**
     * 로그인
     */
    public LoginDto login(LoginEntity loginEntity) {
        return loginRepository.findByLoginId(loginEntity.getLoginId())
                .map(user -> {
                    if (loginEntity.getLoginPw().equals(user.getLoginPw())) {
                        return loginDto.builder()
                                .status("success")
                                .message("로그인되었습니다.")
                                .loginId(user.getLoginId())
                                .name(user.getName())
                                .build();
                    } else {
                        return loginDto.builder()
                                .status("fail")
                                .message("잘못된 비밀번호입니다.")
                                .build();
                    }
                })
                .orElse(loginDto.builder()
                        .status("null")
                        .message("존재하지 않는 회원입니다.")
                        .build());
    }
}
