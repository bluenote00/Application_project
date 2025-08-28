package com.example.application_project.controller.application;

import com.example.application_project.dto.application.ApplicationDto;
import com.example.application_project.entity.application.ApplicationEntity;
import com.example.application_project.service.application.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

            // 불능 코드 시 메세지 추가
            String impsbMsg = applicationService.getImpsbMsg(entity.getImpsbCd());
            result.put("impsbMsg", impsbMsg);

            // 불능 코드가 02여서 계좌 오류일 경우, 띄울 메세지
            if ("02".equals(entity.getImpsbCd())) {
                result.put("stlActCheck", "존재하지 않는 계좌");
            } else {
                result.put("stlActCheck", "");
            }

            logger.info("+ Start " + className + " 조회 결과 " + result);
        }
        return result;
    }


    // 입회신청서 등록
    @PostMapping("/insertAppl")
    public String insertAppl(@ModelAttribute ApplicationDto applicationDto,
                             RedirectAttributes redirectAttributes) {

        logger.info("+ Start " + className + " 입회신청서 등록 " + applicationDto);

        // 1. 당일 중복 체크 : 오늘 날짜 + 주민 번호
        int appDuplicate = applicationService.checkDupApplication(applicationDto);

        // 당일 중복 시 - 불능 처리
        if (appDuplicate > 0) {
            applicationDto.setImpsbClas("불능");
            applicationDto.setImpsbCd("01");

            redirectAttributes.addFlashAttribute("message", "당일 중복 신청입니다.");
            applicationService.insertApplication(applicationDto);

            logger.info("중복 신청 → 불능 처리 : " + applicationDto);
          
        // 당일 중복이 아닐 경우
        } else if(appDuplicate < 1) {

            // 2. 계좌 확인 : 은행 코드 + 계좌 번호
            int acntCheck = applicationService.checkAcnt(applicationDto);

            // 계좌 오류 - 매칭되는 계좌가 없는 경우
            if (acntCheck < 1) {
                applicationDto.setImpsbClas("불능");
                applicationDto.setImpsbCd("02");

                redirectAttributes.addFlashAttribute("message", "불능 - 계좌 오류");
                applicationService.insertApplication(applicationDto);

                logger.info("계좌 오류 → 불능 처리 : " + applicationDto);

                return "redirect:/application/index";
            }

            //  3. 비밀번호 확인
            String validationMsg = applicationService.validatePassword(
                    applicationDto.getScrtNo(),
                    applicationDto.getHdpNo(),
                    applicationDto.getBirthD()
            );

            //  비밀번호 오류 - 비밀번호 유효성 검사에 통과하지 못할 경우
            if (validationMsg != null) {
                applicationDto.setImpsbClas("불능");
                applicationDto.setImpsbCd("03");

                redirectAttributes.addFlashAttribute("message", validationMsg);
                applicationService.insertApplication(applicationDto);

                logger.info("비밀번호 오류 → 불능 처리 : " + applicationDto);

                return "redirect:/application/index";
            }

            //  최초 신규 고객인 경우
            String applClas = applicationDto.getApplClas();

            if (applClas == "11") {
                // 신규 고객이 맞는지 확인
                int newCustYn = applicationService.checkNewCust(applicationDto);

                //  4. 최초 신규 고객이 아닌 경우 - 불능 코드 04 (기존 카드 존재)
               if (newCustYn > 0) {
                   applicationDto.setImpsbClas("불능");
                   applicationDto.setImpsbCd("04");

                   redirectAttributes.addFlashAttribute("message", "불능 - 신규 고객 아님");

                   applicationService.insertApplication(applicationDto);
                   logger.info("최초 신규 고객 (기존 카드 존재) → 불능 처리 : " + applicationDto);

                   return "redirect:/application/index";

                //  4. 최초 신규 고객이 맞는 경우
               } else if (newCustYn < 1) {

                   // 신청 테이블 insert
                   applicationService.insertApplication(applicationDto);

                   // 카드 테이블 insert
                   applicationService.insertCrd(applicationDto);

                   // 고객 테이블 insert
                   applicationService.insertCust(applicationDto);
                   logger.info("최초 신규 고객 등록 : " + applicationDto);

                   return "redirect:/application/index";
               }
            }

            // 최종 저장
            redirectAttributes.addFlashAttribute("message", "신청이 완료되었습니다.");
            applicationService.insertApplication(applicationDto);
        }

        return "redirect:/application/index";
    }
}
