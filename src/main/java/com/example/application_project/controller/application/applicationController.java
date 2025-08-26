package com.example.application_project.controller.application;

import com.example.application_project.dto.application.ApplicationDto;
import com.example.application_project.entity.application.ApplicationEntity;
import com.example.application_project.service.application.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // Set logger
    private final Logger logger = LogManager.getLogger(this.getClass());

    // Get class name for logger
    private final String className = this.getClass().toString();

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
            @RequestParam("rcvSeqNo") String rcvSeqNo,
            @RequestParam("ssn") String ssn,
            @RequestParam("rcvD") String rcvDStr,
            Model model
    ) {

        LocalDate rcvD = LocalDate.parse(rcvDStr, DateTimeFormatter.ISO_DATE);

        logger.info("+ Start " + className + " 접수 번호: " + rcvSeqNo + ", 주민번호: " + ssn + ", 접수일자: " + rcvD);

        Optional<ApplicationEntity> result = applicationService.searchAppl(ssn, rcvD, rcvSeqNo);

        logger.info("+ Start " + className + " 조회 결과 " + result);

        if (result.isPresent()) {
            model.addAttribute("appl", result.get());

        } else {
            model.addAttribute("message", "조회 결과가 없습니다.");
        }

        return "index";
    }

    // 입회신청서 등록
    @PostMapping("/insertAppl")
    public String insertAppl(@ModelAttribute ApplicationDto applicationDto) {

        logger.info("+ Start " + className + " 입회신청서 등록 " + applicationDto);

        applicationService.insertApplication(applicationDto);

        logger.info("+ End " + className + " 등록 결과 " + applicationDto);

        return "redirect:/application/index";
    }



}
