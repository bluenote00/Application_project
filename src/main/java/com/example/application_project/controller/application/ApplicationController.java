package com.example.application_project.controller.application;

import com.example.application_project.dto.application.ApplicationDto;
import com.example.application_project.dto.application.CustBillDto;
import com.example.application_project.dto.card.CrdDto;
import com.example.application_project.dto.cardDetail.CardDetailDto;
import com.example.application_project.entity.application.ApplicationEntity;
import com.example.application_project.entity.card.CrdEntity;
import com.example.application_project.service.application.ApplicationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
                result.put("stlActCheck", "정상");
            }

            logger.info("+ Start " + className + " 조회 결과 " + result);
        }
        return result;
    }


    // 입회신청서 등록
    @PostMapping("/insertAppl")
    public String insertAppl(@RequestParam Map<String, Object> paramMap, @ModelAttribute ApplicationDto applicationDto,
                             HttpSession session, RedirectAttributes redirectAttributes) {

        logger.info("+ Start " + className + " 입회신청서 등록 " + applicationDto);

        // 작업자 사번 저장을 위한 세션 값 저장
        paramMap.put("loginId", session.getAttribute("loginId"));
        paramMap.put("userRole", session.getAttribute("userRole"));

        String loginId = (String) session.getAttribute("loginId");

        // 신청 구분 코드
        String applClas = applicationDto.getApplClas();

        // 당일 중복 체크 : 오늘 날짜 + 주민 번호
        int appDuplicate = applicationService.checkDupApplication(applicationDto);

        // 당일 중복 시 - 불능 처리
        if (appDuplicate > 0) {
            applicationDto.setImpsbClas("불능");
            applicationDto.setImpsbCd("01");

            redirectAttributes.addFlashAttribute("message", "당일 중복 신청입니다.");
            applicationService.insertApplication(applicationDto, loginId);

            // 접수 매핑 테이블 저장
            applicationService.insertNoseq(applicationDto);

            logger.info("중복 신청 → 불능 처리 : " + applicationDto);

            // 당일 중복이 아닐 경우
        } else if (appDuplicate < 1) {

            // 2. 계좌 확인 : 은행 코드 + 계좌 번호
            int acntCheck = applicationService.checkAcnt(applicationDto);

            // 계좌 오류 - 매칭되는 계좌가 없는 경우
            if (acntCheck < 1) {
                applicationDto.setImpsbClas("불능");
                applicationDto.setImpsbCd("02");

                redirectAttributes.addFlashAttribute("message", "불능 - 계좌 오류");
                applicationService.insertApplication(applicationDto, loginId);

                // 접수 매핑 테이블 저장
                applicationService.insertNoseq(applicationDto);

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
                applicationService.insertApplication(applicationDto, loginId);

                // 접수 매핑 테이블 저장
                applicationService.insertNoseq(applicationDto);

                logger.info("비밀번호 오류 → 불능 처리 : " + applicationDto);

                return "redirect:/application/index";
            }

            // 신규 고객인 경우 (신청 구분 - "11")
            if ("11".equals(applClas)) {
                // 신규 고객이 맞는지 확인
                int newCustYn = applicationService.checkNewCust(applicationDto);

                //  4. 최초 신규 고객이 아닌 경우 - 불능 코드 04 (기존 카드 존재)
                if (newCustYn > 0) {
                    applicationDto.setImpsbClas("불능");
                    applicationDto.setImpsbCd("04");

                    redirectAttributes.addFlashAttribute("message", "불능 - 신규 고객 아님");

                    applicationService.insertApplication(applicationDto, loginId);

                    // 접수 매핑 테이블 저장
                    applicationService.insertNoseq(applicationDto);

                    logger.info("최초 신규 고객 (기존 고객) → 불능 처리 : " + applicationDto);

                    return "redirect:/application/index";

                    //  4. 최초 신규 고객이 맞는 경우
                } else if (newCustYn < 1) {
                    // 신청 테이블 insert
                    applicationService.insertApplication(applicationDto, loginId);

                    // 고객 테이블 insert
                    applicationService.insertCust(applicationDto, loginId);

                    // 결제 테이블 insert
                    applicationService.insertBill(applicationDto, loginId);

                    // 카드 테이블 insert
                    applicationService.insertCrd(applicationDto, loginId);

                    // 신청 테이블 - 카드 번호 update
                    applicationService.updateApplication(applicationDto, loginId);

                    // 접수 매핑 테이블 저장
                    applicationService.insertNoseq(applicationDto);

                    // 카드 매핑 테이블 저장
                    applicationService.insertSeqNo(applicationDto);

                    redirectAttributes.addFlashAttribute("message", "최초 신규 고객 신청이 완료되었습니다.");
                    logger.info("최초 신규 고객 등록 : " + applicationDto);

                    return "redirect:/application/index";
                }
            }

            // 추가 신규 고객인 경우 (신청 구분 - "12")
            if ("12".equals(applClas)) {
                // 추가 신규 고객이 맞는지 확인
                int newPlusCustYn = applicationService.checkNewPlusCust(applicationDto);

                //  4. 추가 신규 고객이 아닌 경우 - 불능 코드 04 (기존 브랜드 카드 존재)
                if (newPlusCustYn > 0) {
                    applicationDto.setImpsbClas("불능");
                    applicationDto.setImpsbCd("04");

                    redirectAttributes.addFlashAttribute("message", "불능 - 추가 신규 고객 아님");

                    applicationService.insertApplication(applicationDto, loginId);

                    // 접수 매핑 테이블 저장
                    applicationService.insertNoseq(applicationDto);

                    logger.info("추가 신규 고객 (동일 브랜드 카드 존재) → 불능 처리 : " + applicationDto);

                    return "redirect:/application/index";

                    //  5. 추가 신규 고객이 맞는 경우
                } else if (newPlusCustYn < 1) {
                    // 신청 테이블 insert
                    applicationService.insertApplication(applicationDto, loginId);

                    // 고객 테이블 update
                    applicationService.updateCust(applicationDto, loginId);

                    // 결제 테이블 update
                    applicationService.updateBill(applicationDto, loginId);

                    // 카드 테이블 insert
                    applicationService.insertCrd(applicationDto, loginId);

                    // 신청 테이블 - 카드 번호 update
                    applicationService.updateApplication(applicationDto, loginId);

                    // 접수 매핑 테이블 저장
                    applicationService.insertNoseq(applicationDto);

                    // 카드 매핑 테이블 저장
                    applicationService.insertSeqNo(applicationDto);

                    redirectAttributes.addFlashAttribute("message", "추가 신규 고객 신청이 완료되었습니다.");
                    logger.info("추가 신규 고객 등록 : " + applicationDto);

                    return "redirect:/application/index";
                }
            }

            // 재발급 고객인 경우 (신청 구분 - "21")
            if ("21".equals(applClas)) {
                // 재발급 조건의 고객이 맞는지 확인 (동일 브랜드 카드 소지) - 위에서 이미 선언함
                int newPlusCustYn = applicationService.checkNewPlusCust(applicationDto);

                //  4. 재발급 가능 고객이 아닌 경우 - 불능 코드 05 (기존 브랜드 카드 없음)
                if (newPlusCustYn < 1) {
                    applicationDto.setImpsbClas("불능");
                    applicationDto.setImpsbCd("05");

                    redirectAttributes.addFlashAttribute("message", "불능 - 재발급 고객 아님");

                    applicationService.insertApplication(applicationDto, loginId);

                    // 접수 매핑 테이블 저장
                    applicationService.insertNoseq(applicationDto);

                    logger.info("재발급 고객 (동일 브랜드 카드 미존재) → 불능 처리 : " + applicationDto);

                    return "redirect:/application/index";

                    //  6. 재발급 고객이 맞는 경우
                } else if (newPlusCustYn > 0) {
                    // 신청 테이블 insert
                    applicationService.insertApplication(applicationDto, loginId);

                    // 고객 테이블 update
                    applicationService.updateCust(applicationDto, loginId);

                    // 결제 테이블 update
                    applicationService.updateBill(applicationDto, loginId);

                    // 재발급 고객 카드 테이블 update 후 insert
                    applicationService.reissueCrd(applicationDto, loginId);

                    // 신청 테이블 - 카드 번호 update
                    applicationService.updateApplication(applicationDto, loginId);

                    // 접수 매핑 테이블 저장
                    applicationService.insertNoseq(applicationDto);

                    // 카드 매핑 테이블 저장
                    applicationService.insertSeqNo(applicationDto);

                    redirectAttributes.addFlashAttribute("message", "재발급 고객 신청이 완료되었습니다.");
                    logger.info("재발급 고객 등록 : " + applicationDto);

                    return "redirect:/application/index";
                }
            }

            // 최종 저장
            redirectAttributes.addFlashAttribute("message", "신청이 완료되었습니다.");
            applicationService.insertApplication(applicationDto, loginId);

            // 접수 매핑 테이블 저장
            applicationService.insertNoseq(applicationDto);
        }
        return "redirect:/application/index";
    }


    // 입회신청서 수정
    @PostMapping("/updateAppl")
    public String updateAppl(@RequestParam Map<String, Object> paramMap, @ModelAttribute ApplicationDto applicationDto,
                             HttpSession session, RedirectAttributes redirectAttributes) {

        logger.info("+ Start " + className + " 입회신청서 수정 " + applicationDto);

        // 작업자 사번 저장을 위한 세션 값 저장
        paramMap.put("loginId", session.getAttribute("loginId"));
        paramMap.put("userRole", session.getAttribute("userRole"));

        String loginId = (String) session.getAttribute("loginId");

        // 신청 구분 코드
        String applClas = applicationDto.getApplClas();

        // 당일 중복 체크 : 오늘 날짜 + 주민 번호
        int appDuplicate = applicationService.checkDupApplication(applicationDto);

        // 당일 중복 시 - 불능 처리
        if (appDuplicate > 0) {
            applicationDto.setImpsbClas("불능");
            applicationDto.setImpsbCd("01");

            redirectAttributes.addFlashAttribute("message", "(수정)당일 중복 수정 신청입니다.");

            // 신청 정보 update
            applicationService.updateImpApplication(applicationDto, loginId);

            // 접수 매핑 테이블 시간 update
            applicationService.updateNoseq(applicationDto);

            logger.info("(수정)중복 수정 신청 → 불능 처리 : " + applicationDto);

        // 당일 중복이 아닐 경우
        } else if (appDuplicate < 1) {

            applicationDto.setImpsbClas(null);
            applicationDto.setImpsbCd(null);

            // 2. 계좌 확인 : 은행 코드 + 계좌 번호
            int acntCheck = applicationService.checkAcnt(applicationDto);

            // 계좌 오류 - 매칭되는 계좌가 없는 경우
            if (acntCheck < 1) {
                applicationDto.setImpsbClas("불능");
                applicationDto.setImpsbCd("02");

                redirectAttributes.addFlashAttribute("message", "(수정)불능 - 계좌 오류");

                // 신청 정보 update
                applicationService.updateImpApplication(applicationDto, loginId);

                // 접수 매핑 테이블 시간 update
                applicationService.updateNoseq(applicationDto);

                logger.info("(수정)계좌 오류 → 불능 처리 : " + applicationDto);

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

                // 신청 정보 update
                applicationService.updateImpApplication(applicationDto, loginId);

                // 접수 매핑 테이블 시간 update
                applicationService.updateNoseq(applicationDto);

                logger.info("(수정)비밀번호 오류 → 불능 처리 : " + applicationDto);

                return "redirect:/application/index";

            }

            // 신규 고객인 경우 (신청 구분 - "11")
            if ("11".equals(applClas)) {
                // 신규 고객이 맞는지 확인
                int newCustYn = applicationService.checkNewCust(applicationDto);

                //  4. 최초 신규 고객이 아닌 경우 - 불능 코드 04 (기존 카드 존재)
                if (newCustYn > 0) {
                    applicationDto.setImpsbClas("불능");
                    applicationDto.setImpsbCd("04");

                    redirectAttributes.addFlashAttribute("message", "(수정)불능 - 신규 고객 아님");

                    // 신청 정보 update
                    applicationService.updateImpApplication(applicationDto, loginId);

                    // 접수 매핑 테이블 시간 update
                    applicationService.updateNoseq(applicationDto);

                    logger.info("(수정)최초 신규 고객 (기존 고객) → 불능 처리 : " + applicationDto);

                    return "redirect:/application/index";

                    //  4. 최초 신규 고객이 맞는 경우
                } else if (newCustYn < 1) {
                    // 신청 정보 update
                    applicationService.updateImpApplication(applicationDto, loginId);

                    // 고객 테이블 insert
                    applicationService.insertCust(applicationDto, loginId);

                    // 결제 테이블 insert
                    applicationService.insertBill(applicationDto, loginId);

                    // 카드 테이블 insert
                    applicationService.insertCrd(applicationDto, loginId);

                    // 신청 테이블 - 카드 번호 update
                    applicationService.updateApplication(applicationDto, loginId);

                    // 접수 매핑 테이블 시간 update
                    applicationService.updateNoseq(applicationDto);

                    // 카드 매핑 테이블 저장
                    applicationService.insertSeqNo(applicationDto);

                    redirectAttributes.addFlashAttribute("message", "(수정)최초 신규 고객 신청이 완료되었습니다.");
                    logger.info("(수정)최초 신규 고객 등록 : " + applicationDto);

                    return "redirect:/application/index";
                }
            }

            // 추가 신규 고객인 경우 (신청 구분 - "12")
            if ("12".equals(applClas)) {
                // 추가 신규 고객이 맞는지 확인
                int newPlusCustYn = applicationService.checkNewPlusCust(applicationDto);

                //  4. 추가 신규 고객이 아닌 경우 - 불능 코드 04 (기존 브랜드 카드 존재)
                if (newPlusCustYn > 0) {
                    applicationDto.setImpsbClas("불능");
                    applicationDto.setImpsbCd("04");

                    redirectAttributes.addFlashAttribute("message", "(수정)불능 - 추가 신규 고객 아님");

                    // 신청 정보 update
                    applicationService.updateImpApplication(applicationDto, loginId);

                    // 접수 매핑 테이블 시간 update
                    applicationService.updateNoseq(applicationDto);

                    logger.info("(수정)추가 신규 고객 (동일 브랜드 카드 존재) → 불능 처리 : " + applicationDto);

                    return "redirect:/application/index";

                    //  5. 추가 신규 고객이 맞는 경우
                } else if (newPlusCustYn < 1) {
                    // 신청 정보 update
                    applicationService.updateImpApplication(applicationDto, loginId);

                    // 고객 테이블 update
                    applicationService.updateCust(applicationDto, loginId);

                    // 결제 테이블 update
                    applicationService.updateBill(applicationDto, loginId);

                    // 카드 테이블 insert
                    applicationService.insertCrd(applicationDto, loginId);

                    // 신청 테이블 - 카드 번호 update
                    applicationService.updateApplication(applicationDto, loginId);

                    // 접수 매핑 테이블 시간 update
                    applicationService.updateNoseq(applicationDto);

                    // 카드 매핑 테이블 저장
                    applicationService.insertSeqNo(applicationDto);

                    redirectAttributes.addFlashAttribute("message", "(수정)추가 신규 고객 신청이 완료되었습니다.");
                    logger.info("(수정)추가 신규 고객 등록 : " + applicationDto);

                    return "redirect:/application/index";
                }

            }

            // 재발급 고객인 경우 (신청 구분 - "21")
            if ("21".equals(applClas)) {
                // 재발급 조건의 고객이 맞는지 확인 (동일 브랜드 카드 소지) - 위에서 이미 선언함
                int newPlusCustYn = applicationService.checkNewPlusCust(applicationDto);

                //  4. 재발급 가능 고객이 아닌 경우 - 불능 코드 05 (기존 브랜드 카드 없음)
                if (newPlusCustYn < 1) {
                    applicationDto.setImpsbClas("불능");
                    applicationDto.setImpsbCd("05");

                    redirectAttributes.addFlashAttribute("message", "(수정)불능 - 재발급 고객 아님");

                    // 신청 정보 update
                    applicationService.updateImpApplication(applicationDto, loginId);

                    // 접수 매핑 테이블 시간 update
                    applicationService.updateNoseq(applicationDto);

                    logger.info("(수정)재발급 고객 (동일 브랜드 카드 미존재) → 불능 처리 : " + applicationDto);

                    return "redirect:/application/index";

                    //  6. 재발급 고객이 맞는 경우
                } else if (newPlusCustYn > 0) {
                    // 신청 정보 update
                    applicationService.updateImpApplication(applicationDto, loginId);

                    // 고객 테이블 update
                    applicationService.updateCust(applicationDto, loginId);

                    // 결제 테이블 update
                    applicationService.updateBill(applicationDto, loginId);

                    // 재발급 고객 카드 테이블 update 후 insert
                    applicationService.reissueCrd(applicationDto, loginId);

                    // 신청 테이블 - 카드 번호 update
                    applicationService.updateApplication(applicationDto, loginId);

                    // 접수 매핑 테이블 시간 update
                    applicationService.updateNoseq(applicationDto);

                    // 카드 매핑 테이블 저장
                    applicationService.insertSeqNo(applicationDto);

                    redirectAttributes.addFlashAttribute("message", "(수정)재발급 고객 신청이 완료되었습니다.");
                    logger.info("(수정)재발급 고객 등록 : " + applicationDto);

                    return "redirect:/application/index";
                }
            }

            // 최종 저장
            redirectAttributes.addFlashAttribute("message", "수정 완료되었습니다.");
            applicationService.updateImpApplication(applicationDto, loginId);

            // 접수 매핑 테이블 저장
            applicationService.updateNoseq(applicationDto);
        }
        return "redirect:/application/index";
    }

    // 기간별 입회 신청 내역 조회
    @PostMapping("/searchApplPeriod")
    @ResponseBody
    public Map<String, Object> searchApplPeriod(@RequestBody Map<String, String> params) {
        logger.info("기간별 입회 신청 내역 조회 - Start : " + params);

        String startRcvDStr = params.get("startRcvD");
        String endRcvDStr   = params.get("endRcvD");
        String applClas     = params.get("applClas");
        String ssn          = params.get("ssn");

        LocalDate startRcvD = LocalDate.parse(startRcvDStr, DateTimeFormatter.ISO_DATE);
        LocalDate endRcvD   = LocalDate.parse(endRcvDStr, DateTimeFormatter.ISO_DATE);

        List<ApplicationDto> list = applicationService.searchApplPeriod(ssn, startRcvD, endRcvD, applClas);

        logger.info("기간별 입회 신청 내역 조회 결과 : " + list);

        Map<String, Object> result = new HashMap<>();
        if (list.isEmpty()) {
            result.put("message", "조회 결과가 없습니다.");
        } else {
            result.put("data", list);
        }
        return result;
    }

    // 회원 색인 내역 조회
    @PostMapping("/searchUser")
    @ResponseBody
    public Map<String, Object> searchUser(@RequestBody Map<String, String> params) {
        logger.info("회원 색인 내역 조회 : Start + " + params);

        String ssn = params.get("jumin");       // 주민번호
        String birthD = params.get("birthD");   // 생년월일
        String hdpNo = params.get("hdpNo");     // 핸드폰

        List<CustBillDto> list = applicationService.searchUser(ssn, birthD, hdpNo);

        logger.info("회원 색인 내역 조회 : " + list);

        Map<String, Object> result = new HashMap<>();
        if (list.isEmpty()) {
            result.put("message", "조회 결과가 없습니다.");
        } else {
            result.put("data", list);
        }
        return result;
    }

    // 카드 상세 내역 조회
    @PostMapping("/searchCardDetail")
    @ResponseBody
    public Map<String, Object> searchCardDetail(@RequestBody Map<String, String> params) {
        logger.info("카드 상세 내역 조회 : Start + " + params);

        String ssn = params.get("ssn");   // 주민번호
        String crdNo = params.get("crdNo");   // 카드번호

        Optional<CrdEntity> optionalCrd = applicationService.searchCardDetail(ssn, crdNo);

        Map<String, Object> result = new HashMap<>();
        if (optionalCrd.isPresent()) {
            CrdEntity crd = optionalCrd.get();

            // 화면용 DTO로 변환 (필요시)
            CrdDto dto = CrdDto.builder()
                    .crdNo(crd.getCrdNo())
                    .custNo(crd.getCustNo())
                    .mgtBbrn(crd.getMgtBbrn())
                    .regD(crd.getRegD())
                    .ssn(crd.getSsn())
                    .vldDur(crd.getVldDur())
                    .brd(crd.getBrd())
                    .scrtNo(crd.getScrtNo())
                    .engNm(crd.getEngNm())
                    .bfCrdNo(crd.getBfCrdNo())
                    .lstCrdF(crd.getLstCrdF())
                    .fstRegD(crd.getFstRegD())
                    .crdGrd(crd.getCrdGrd())
                    .lstOprTm(crd.getLstOprTm())
                    .lstOprD(crd.getLstOprD())
                    .lstOprtEmpno(crd.getLstOprtEmpno())
                    .build();

            result.put("data", Collections.singletonList(dto));

            logger.info("카드 상세 내역 조회 : + " + result);
        } else {
            result.put("message", "조회 결과가 없습니다.");
        }

        return result;
    }

    // 카드 리스트 상세 조회
    @PostMapping("/searchCardList")
    @ResponseBody
    public Map<String,Object> searchCardList(@RequestBody Map<String,String> params){
        logger.info("카드 리스트 상세 조회 : Start + " + params);

        String ssn = params.get("ssn");
        String crdNo = params.get("crdNo");

        Map<String,Object> result = new HashMap<>();
        Optional<CardDetailDto> dataOpt = applicationService.searchCardList(ssn,crdNo);

        if(dataOpt.isPresent()){
            result.put("data", Collections.singletonList(dataOpt.get()));
            logger.info("카드 리스트 상세 조회 : " + result);

        } else {
            result.put("message", "조회 결과가 없습니다.");
        }
        return result;
    }


}