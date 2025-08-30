package com.example.application_project.service.application;

import com.example.application_project.config.PasswordUtil;
import com.example.application_project.dto.application.ApplicationDto;
import com.example.application_project.entity.application.ApplicationEntity;
import com.example.application_project.entity.bill.BillEntity;
import com.example.application_project.entity.card.CrdEntity;
import com.example.application_project.entity.cust.CustEntity;
import com.example.application_project.repository.acnt.AcntRepository;
import com.example.application_project.repository.application.ApplicationRepository;
import com.example.application_project.repository.bill.BillRepository;
import com.example.application_project.repository.card.CrdRepository;
import com.example.application_project.repository.cust.CustRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final AcntRepository acntRepository;
    private final CrdRepository crdRepository;
    private final CustRepository custRepository;
    private final BillRepository billRepository;

    // 입회신청서 조회
    public Optional<ApplicationEntity> searchAppl(String ssn, LocalDate rcvD, String rcvSeqNo) {
        return applicationRepository.findBySsnAndRcvDAndRcvSeqNo(ssn, rcvD, rcvSeqNo);
    }

    // 불능 코드 별 메세지 추가
    public String getImpsbMsg(String impsbCd) {
        if (impsbCd == null) return "";

        switch (impsbCd) {
            case "01":
                return "01 - 당일 신청 내역 존재";
            case "02":
                return "02 - 결제 계좌 오류";
            case "03":
                return "03 - 비밀번호 오류";
            case "04":
                return "04 - 기존 카드 존재";
            case "05":
                return "05 - 기존 카드 미존재";
            default:
                return impsbCd;
        }
    }

    // 입회 신청서 등록 - 신청테이블
    public void insertApplication(ApplicationDto dto, String loginId) {
        // 오늘 날짜
        String todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String currentTime = java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        // DB에서 오늘 날짜의 최대 시퀀스 조회
        String maxSeq = applicationRepository.findMaxSeqNoByDate(todayStr);

        int nextSeq = 1;
        if (maxSeq != null) {
            String seqPart = maxSeq.substring(8);
            nextSeq = Integer.parseInt(seqPart) + 1;
        }

        String newRcvSeqNo = todayStr + nextSeq;

        // 비밀번호 암호화
        String encryptedScrtNo = PasswordUtil.encryptSHA256(dto.getScrtNo());

        ApplicationEntity entity = ApplicationEntity.builder()
                .rcvSeqNo(newRcvSeqNo)
                .rcvD(LocalDate.now())
                .ssn(dto.getSsn())
                .applD(LocalDate.now())
                .birthD(dto.getBirthD())
                .hgNm(dto.getHgNm())
                .engNm(dto.getEngNm())
                .stlMtd(dto.getStlMtd())
                .stlAct(dto.getStlAct())
                .bnkCd(dto.getBnkCd())
                .stlDd(dto.getStlDd())
                .mgtBbrn(dto.getMgtBbrn())
                .applClas(dto.getApplClas())
                .stmtSndMtd(dto.getStmtSndMtd())
                .billadrAdr1(dto.getBilladrAdr1())
                .billadrAdr2(dto.getBilladrAdr2())
                .billadrZip(dto.getBilladrZip())
                .hdpNo(dto.getHdpNo())
                .brd(dto.getBrd())
                .scrtNo(encryptedScrtNo)
                .emailAdr(dto.getEmailAdr())
                .crdNo(dto.getCrdNo())
                .impsbClas(dto.getImpsbClas())
                .impsbCd(dto.getImpsbCd())
                .lstOprD(todayDate)
                .lstOprTm(currentTime)
                .lstOprtEmpno(loginId)
                .build();


        applicationRepository.save(entity);

        // 다른곳에서도 암호화한 비밀번호 값이 insert되도록 set함
        dto.setScrtNo(encryptedScrtNo);
    }


    // 1. 당일 중복 신청 체크
    public int checkDupApplication(ApplicationDto dto) {
        return applicationRepository.countBySsnAndRcvD(dto.getSsn(), LocalDate.now());
    }

    // 2. 계좌 정상 체크
    public int checkAcnt(ApplicationDto dto) {
        return acntRepository.countByAcntCodeAndAcntNum(dto.getBnkCd(), dto.getStlAct());
    }

    // 3. 비밀번호 체크
    public String validatePassword(String scrtNo, String hdpNo, String birthD) {
        // 동일 숫자 반복 체크
        if (scrtNo.chars().distinct().count() == 1) {
            return "비밀번호 - 동일 숫자 사용";
        }

        // 연속된 숫자 체크
        boolean sequentialAsc = true;
        boolean sequentialDesc = true;
        for (int i = 0; i < scrtNo.length() - 1; i++) {
            int cur = scrtNo.charAt(i) - '0';
            int next = scrtNo.charAt(i + 1) - '0';

            if (next != cur + 1) sequentialAsc = false;
            if (next != cur - 1) sequentialDesc = false;
        }
        if (sequentialAsc || sequentialDesc) {
            return "비밀번호 - 연속된 숫자 사용";
        }

        // 핸드폰 번호 끝 4자리 체크
        if (hdpNo != null && hdpNo.length() >= 4) {
            String last4 = hdpNo.substring(hdpNo.length() - 4);
            if (scrtNo.equals(last4)) {
                return "비밀번호 - 핸드폰 번호 중복";
            }
        }

        // 생년월일 체크
        if (birthD != null && birthD.length() >= 8) {
            String birth4 = birthD.substring(4);
            if (scrtNo.equals(birth4)) {
                return "비밀번호 - 생년월일 중복";
            }
        }

        return null;
    }

    // 4. 최초 신규 고객 확인
    public int checkNewCust(ApplicationDto dto) {
        return crdRepository.countBySsn(dto.getSsn());
    }

    // 최초 신규 고객 - 고객 정보 등록
    public void insertCust(ApplicationDto dto, String loginId) {

        // 고객번호 조회 후 최신 번호로 시퀀스
        String lastCustNo = custRepository.findMaxCustNo();
        long nextCustNo = 1L;

        if (lastCustNo != null) {
            nextCustNo = Long.parseLong(lastCustNo) + 1;
        }

        String newCustNo = String.format("%09d", nextCustNo);

        // 최종 작업자 날짜와 시간 분리
        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String currentTime = java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        
        CustEntity entity = CustEntity.builder()
                .custNo(newCustNo)
                .ssn(dto.getSsn())
                .regD(LocalDate.now())
                .hgNM(dto.getHgNm())
                .birthD(dto.getBirthD())
                .hdpNO(dto.getHdpNo())
                .lstOprD(todayDate)
                .lstOprTm(currentTime)
                .lstOprtEmpno(loginId)
                .build();

        custRepository.save(entity);

        // 아래 결제 정보/카드 정보 등록에서 사용 할 수 있도록 set함
        dto.setCustNo(newCustNo);
    }

    // 최초 신규 고객 - 결제 정보 등록
    public void insertBill(ApplicationDto dto, String loginId) {
        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String currentTime = java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        // 청구서 사절("4")일 경우 Y 아니면 N
        String stmtDeniClas = "4".equals(dto.getStmtSndMtd()) ? "Y" : "N";

        BillEntity entity = BillEntity.builder()
                .custNo(dto.getCustNo())
                .stlAct(dto.getStlAct())
                .bnkCd(dto.getBnkCd())
                .dpsNm(dto.getHgNm())
                .stlMtd(dto.getStlMtd())
                .stlDd(dto.getStlDd())
                .prcsClas("Y")
                .stmtSndMtd(dto.getStmtSndMtd())
                .stmtDeniClas(stmtDeniClas)
                .billZip(dto.getBilladrZip())
                .billAdr1(dto.getBilladrAdr1())
                .billAdr2(dto.getBilladrAdr2())
                .emailAdr(dto.getEmailAdr())
                .lstOprD(todayDate)
                .lstOprTm(currentTime)
                .lstOprtEmpno(loginId)
                .build();

        billRepository.save(entity);
    }

    // 카드 번호 체크 디지트
    private int calculateLuhnCheckDigit(String numberWithoutCheckDigit) {
        int sum = 0;
        boolean alternate = true;

        for (int i = numberWithoutCheckDigit.length() - 1; i >= 0; i--) {
            int n = numberWithoutCheckDigit.charAt(i) - '0';
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (10 - (sum % 10)) % 10;
    }


    // 최초 신규 고객 - 카드 정보 등록
    public void insertCrd(ApplicationDto dto, String loginId) {
        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String currentTime = java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        // 유효기간 = 오늘 + 5년 (yyMMdd)
        String vldDur = LocalDate.now().plusYears(5).format(DateTimeFormatter.ofPattern("yyMMdd"));

        // 브랜드 별 4자리 숫자 세팅
        String prefix;
        switch (dto.getBrd()) {
            case "1": prefix = "5310"; break;
            case "2": prefix = "4906"; break;
            case "3": prefix = "3560"; break;
            default: throw new IllegalArgumentException("잘못된 브랜드 코드: " + dto.getBrd());
        }

        // 카드 번호 시퀀스 추가
        String lastCrdNo = crdRepository.findMaxCrdNo();

        long nextSeq = 1;
        if (lastCrdNo != null && lastCrdNo.length() == 16) {
            String lastSeq = lastCrdNo.substring(6, 15);
            nextSeq = Long.parseLong(lastSeq) + 1;
        }
        String seq9 = String.format("%09d", nextSeq);

        // 4. 체크디지트 계산
        String numberWithoutCheck = prefix + "11" + seq9;
        int checkDigit = calculateLuhnCheckDigit(numberWithoutCheck);

        String newCrdNo = numberWithoutCheck + checkDigit;

        CrdEntity entity = CrdEntity.builder()
                .crdNo(newCrdNo)
                .custNo(dto.getCustNo())
                .mgtBbrn(dto.getMgtBbrn())
                .regD(LocalDate.now())
                .ssn(dto.getSsn())
                .vldDur(vldDur)
                .brd(dto.getBrd())
                .scrtNo(dto.getScrtNo())
                .engNm(dto.getEngNm())
                .bfCrdNo(dto.getBfCrdNo())
                .lstCrdF("1")
                .fstRegD(LocalDate.now())
                .crdGrd("11")
                .lstOprD(todayDate)
                .lstOprTm(currentTime)
                .lstOprtEmpno(loginId)
                .build();

        crdRepository.save(entity);

        dto.setCrdNo(newCrdNo);
    }

    // 입회 신청서에 카드 번호 업데이트
    public void updateApplication(ApplicationDto dto, String loginId) {
        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String currentTime = java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        // SSN으로 최신 접수건 조회 (최신 = RCV_D + RCV_SEQ_NO 기준)
        Optional<ApplicationEntity> latestApplOpt = applicationRepository
                .findLatestBySsn(dto.getSsn());

        if (latestApplOpt.isPresent()) {
            ApplicationEntity appl = latestApplOpt.get();
            appl.updateCrdNo(dto.getCrdNo(), todayDate, currentTime, loginId);
            applicationRepository.save(appl);

        } else {
            throw new IllegalStateException("카드 번호 update 실패 " + dto.getSsn());
        }
    }

    // 5. 추가 신규 고객 확인
    public int checkNewPlusCust(ApplicationDto dto) {
        return crdRepository.countBySsnAndBrd(dto.getSsn(), dto.getBrd());
    }

}
