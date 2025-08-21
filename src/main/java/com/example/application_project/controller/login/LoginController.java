package com.example.application_project.controller.login;

import com.example.application_project.dto.login.LoginDto;
import com.example.application_project.entity.login.LoginEntity;
import com.example.application_project.service.login.LoginService;
import jakarta.servlet.http.HttpSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class LoginController {

    @Autowired
    LoginService loginService;

    // Set logger
    private final Logger logger = LogManager.getLogger(this.getClass());

    // Get class name for logger
    private final String className = this.getClass().toString();

    // 로그인 화면 이동
    @RequestMapping("/")
    public String moveLogin() {
        return "login";
    }

    // 로그인
    @PostMapping("/member/login")
    public String login(@ModelAttribute LoginDto loginDto,
                        Model model, HttpSession session) {

        LoginDto result = loginService.login(loginDto);

        if ("success".equals(result.getStatus())) {
            session.setAttribute("loginId", result.getLoginId());
            session.setAttribute("name", result.getName());
            session.setAttribute("userRole", result.getUserRole());
            return "redirect:/application/index";
        } else {
            model.addAttribute("errorMessage", result.getMessage());
            return "login";
        }
    }


    // 로그아웃
    @GetMapping("/member/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "login";
    }
}
