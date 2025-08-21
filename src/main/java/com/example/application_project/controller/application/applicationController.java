package com.example.application_project.controller.application;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/application")
public class ApplicationController {

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

}
