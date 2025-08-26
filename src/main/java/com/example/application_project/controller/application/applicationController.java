package com.example.application_project.controller.application;

import com.example.application_project.dto.application.ApplicationDto;
import com.example.application_project.entity.application.ApplicationEntity;
import com.example.application_project.service.application.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
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
    @ResponseBody
    public Map<String, Object> searchAppl(@RequestBody Map<String, String> params) {

        String ssn = params.get("ssn");
        String rcvDStr = params.get("rcvD");
        String rcvSeqNo = params.get("rcvSeqNo");

        logger.info("+ Start " + className + " 접수 번호: " + rcvSeqNo + ", 주민번호: " + ssn + ", 접수일자: " + rcvDStr);

        Map<String, Object> result = new HashMap<>();

        LocalDate rcvD = null;

        if (rcvDStr != null && !rcvDStr.isEmpty()) {
            rcvD = LocalDate.parse(rcvDStr, DateTimeFormatter.ISO_DATE);
        }

        Optional<ApplicationEntity> appl = applicationService.searchAppl(ssn, rcvD, rcvSeqNo);

        if (appl.isEmpty()) {
            result.put("message", "조회 결과가 없습니다.");

        } else {
            ApplicationEntity entity = appl.get();

            result.put("applD", entity.getApplD());
            result.put("applClas", entity.getApplClas());
            result.put("brd", entity.getBrd());
            result.put("hgNm", entity.getHgNm());
            result.put("engNm", entity.getEngNm());
            result.put("birthD", entity.getBirthD());
            result.put("stlDd", entity.getStlDd());
            result.put("stlMtd", entity.getStlMtd());
            result.put("bnkCd", entity.getBnkCd());
            result.put("stlAct", entity.getStlAct());
            result.put("billadrZip", entity.getBilladrZip());
            result.put("billadrAdr1", entity.getBilladrAdr1());
            result.put("billadrAdr2", entity.getBilladrAdr2());
            result.put("emailAdr", entity.getEmailAdr());
            result.put("hdpNo", entity.getHdpNo());
            result.put("scrtNo", entity.getScrtNo());
            result.put("impsbClas", entity.getImpsbClas());
            result.put("impsbCd", entity.getImpsbCd());

            logger.info("+ Start " + className + " 조회 결과 " + result);
        }
        return result;
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
