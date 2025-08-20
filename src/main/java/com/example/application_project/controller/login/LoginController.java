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

import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    LoginService loginService;

    // Set logger
    private final Logger logger = LogManager.getLogger(this.getClass());

    // Get class name for logger
    private final String className = this.getClass().toString();

    @RequestMapping("/")
    public String movelogin(@RequestParam Map<String, Object> paramMap, HttpSession session) throws Exception {

        logger.info("+ Start LoginUserController.login");

        return "login";
    }

    /**
     * 로그인 처리
     */
    @PostMapping("/member/login")
    public String login(@ModelAttribute LoginEntity loginEntity,
                        Model model, HttpSession session) {

        LoginDto result = loginService.login(loginEntity);

        if ("success".equals(result.getStatus())) {
            session.setAttribute("loginId", result.getLoginId());
            session.setAttribute("name", result.getName());
            return "redirect:/";
        } else {
            model.addAttribute("errorMessage", result.getMessage());
            return "login";
        }
    }



    /**
     * 로그아웃
     */
    @RequestMapping(value = "/member/logout")
    public String loginOut(HttpSession session) {

        session.invalidate();

        return "login";
    }

}
