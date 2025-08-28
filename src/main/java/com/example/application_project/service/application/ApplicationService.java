package com.example.application_project.service.application;

import com.example.application_project.dto.application.ApplicationDto;
import com.example.application_project.entity.application.ApplicationEntity;
import com.example.application_project.repository.acnt.AcntRepository;
import com.example.application_project.repository.application.ApplicationRepository;
import com.example.application_project.repository.card.CrdRepository;
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
    public void insertApplication(ApplicationDto dto) {
            // 오늘 날짜
            String todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            // DB에서 오늘 날짜의 최대 시퀀스 조회
            String maxSeq = applicationRepository.findMaxSeqNoByDate(todayStr);

            int nextSeq = 1;
            if (maxSeq != null) {
                String seqPart = maxSeq.substring(8);
                nextSeq = Integer.parseInt(seqPart) + 1;
            }

            String newRcvSeqNo = todayStr + nextSeq;

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
                .scrtNo(dto.getScrtNo())
                .emailAdr(dto.getEmailAdr())
                .crdNo(dto.getCrdNo())
                .impsbClas(dto.getImpsbClas())
                .impsbCd(dto.getImpsbCd())
                .lstOprTm(dto.getLstOprTm())
                .lstOprD(dto.getLstOprD())
                .lstOprtEmpno(dto.getLstOprtEmpno())
                .build();

        applicationRepository.save(entity);
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
        return crdRepository.countBySsnOrBrd(dto.getSsn(), dto.getBrd());
    }

    // 신규 고객 카드 정보 등록
    public void insertCrd(ApplicationDto dto) {
        ApplicationEntity entity = ApplicationEntity.builder()
                .crdNo(dto.getRcvSeqNo())
                .custNo(LocalDate.now())
                .mgtBbrn(dto.getSsn())
                .regD(LocalDate.now())
                .ssn(LocalDate.now())
                .vldDur(LocalDate.now())
                .brd(LocalDate.now())
                .scrtNo(LocalDate.now())
                .engNm(LocalDate.now())
                .bfCrdNo(LocalDate.now())
                .lstCrdF(LocalDate.now())
                .fstRegD(LocalDate.now())
                .crdGrd(LocalDate.now())
                .lstOprTm(LocalDate.now())
                .lstOprD(LocalDate.now())
                .lstOprtEmpno(LocalDate.now())
                .build();

        CrdRepository.save(entity);
    }
}
