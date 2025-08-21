package com.example.application_project.controller.application;

import com.example.application_project.entity.application.ApplicationEntity;
import com.example.application_project.service.application.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // 입회신청서 첫 페이지
    @GetMapping("/index")
    public String moveIndex() {
        return "index";
    }

    // 기간별 입회신청 내역 조회
    @GetMapping("/periodicalList")
    public String movePeriodicalList() {
        return "periodicalList";
    }

    // 소지 카드 내역 조회
    @GetMapping("/cardList")
    public String moveCardList() {
        return "cardList";
    }

    // 카드 상세 내역 조회
    @GetMapping("/cardDetailList")
    public String moveCardDetailList() {
        return "cardDetailList";
    }

    // 회원 색인 조회
    @GetMapping("/userIndex")
    public String moveUserIndex() {
        return "userIndex";
    }

    // 입회신청서 조회
    @PostMapping("/searchAppl")
    public String searchAppl(
            @RequestParam("rcvSeqNo") int rcvSeqNo,
            @RequestParam("ssn") int ssn,
            @RequestParam("rcvD") String rcvD,
            Model model
    ) {
        Optional<ApplicationEntity> result = applicationService.searchAppl(ssn, rcvD, rcvSeqNo);

        if (result.isPresent()) {
            model.addAttribute("appl", result.get());
        } else {
            model.addAttribute("error", "조회 결과가 없습니다.");
        }
        return "index";
    }

    // 입회신청서 신청 접수
    @PostMapping("/insertAppl")
    public String insertAppl() {

        return "redirect:/application/index";
    }


}
