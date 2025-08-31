package com.example.application_project.service.application;

import com.example.application_project.config.PasswordUtil;
import com.example.application_project.dto.application.ApplicationDto;
import com.example.application_project.dto.application.CustBillDto;
import com.example.application_project.entity.application.ApplicationEntity;
import com.example.application_project.entity.bill.BillEntity;
import com.example.application_project.entity.card.CrdEntity;
import com.example.application_project.entity.cust.CustEntity;
import com.example.application_project.entity.noseq.NoseqEntity;
import com.example.application_project.entity.seqno.SeqnoEntity;
import com.example.application_project.repository.acnt.AcntRepository;
import com.example.application_project.repository.application.ApplicationRepository;
import com.example.application_project.repository.application.CustBillRepository;
import com.example.application_project.repository.bill.BillRepository;
import com.example.application_project.repository.card.CrdRepository;
import com.example.application_project.repository.cust.CustRepository;
import com.example.application_project.repository.noseq.NoseqRepository;
import com.example.application_project.repository.seqno.SeqnoRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final AcntRepository acntRepository;
    private final CrdRepository crdRepository;
    private final CustRepository custRepository;
    private final BillRepository billRepository;
    private final NoseqRepository noseqRepository;
    private final SeqnoRepository seqnoRepository;

    private final CustBillRepository custBillRepository;

    // 입회신청서 조회
    public Optional<ApplicationEntity> searchAppl(String ssn, LocalDate rcvD, String rcvSeqNo) {
        return applicationRepository.findBySsnAndRcvDAndRcvSeqNo(ssn, rcvD, rcvSeqNo);
    }

    // 기간별 입회 신청 내역 조회
    public List<ApplicationDto> searchApplPeriod(String ssn, LocalDate startRcvD, LocalDate endRcvD, String applClas) {
        List<ApplicationEntity> entities = applicationRepository.findByRcvDBetweenAndApplClasAndSsn(startRcvD, endRcvD, applClas, ssn);

        return entities.stream().map(entity -> ApplicationDto.builder()
                .rcvD(entity.getRcvD())
                .rcvSeqNo(entity.getRcvSeqNo())
                .ssn(entity.getSsn())
                .hgNm(entity.getHgNm())
                .engNm(entity.getEngNm())
                .applClas(entity.getApplClas())
                .brd(entity.getBrd())
                .hdpNo(entity.getHdpNo())
                .impsbClas(entity.getImpsbClas())
                .impsbCd(entity.getImpsbCd())
                .impsbMsg(getImpsbMsg(entity.getImpsbCd()))
                .build()
        ).toList();
    }

    // 회원 색인 내역 조회
    public List<CustBillDto> searchUser(String ssn, String birthD, String hdpNo) {
        return custBillRepository.searchUser(ssn, birthD, hdpNo);
    }

    // 카드 내역 조회
    public Optional<CrdEntity> searchCardDetail(String ssn, String crdNo) {
        return crdRepository.findBySsnAndCrdNo(ssn, crdNo);
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

        // 다른곳에서도 위에서 사용한 값들을 사용할 수 있도록 set함
        dto.setScrtNo(encryptedScrtNo);
        dto.setRcvSeqNo(newRcvSeqNo);
        dto.setRcvD(LocalDate.now());
    }

    // 접수 매핑 테이블 등록
    public void insertNoseq(ApplicationDto dto) {
        NoseqEntity entity = NoseqEntity.builder()
                .rcvSeqNo(dto.getRcvSeqNo())
                .rcvD(dto.getRcvD())
                .build();

        noseqRepository.save(entity);
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

    // 5-1. 추가 신규 고객 - 고객 테이블 update
    public void updateCust(ApplicationDto dto, String loginId) {
        CustEntity existing = custRepository.findBySsn(dto.getSsn())
                .orElseThrow(() -> new IllegalStateException("고객 테이블 수정 실패 " + dto.getSsn()));

        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String currentTime = java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        // 기존 엔티티의 값 + 변경할 값 덮어쓰기
        CustEntity updated = CustEntity.builder()
                .custNo(existing.getCustNo())
                .ssn(existing.getSsn())
                .regD(LocalDate.now())
                .hgNM(dto.getHgNm())
                .birthD(dto.getBirthD())
                .hdpNO(dto.getHdpNo())
                .lstOprD(todayDate)
                .lstOprTm(currentTime)
                .lstOprtEmpno(loginId)
                .build();

        custRepository.save(updated);

        dto.setCustNo(updated.getCustNo());
    }

    // 5-2. 추가 신규 고객 - 결제 테이블 update
    public void updateBill(ApplicationDto dto, String loginId) {
        BillEntity existing = billRepository.findByCustNo(dto.getCustNo())
                .orElseThrow(() -> new IllegalStateException("결제 수정 실패 " + dto.getCustNo()));

        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String currentTime = java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        // 청구서 사절("4")일 경우 Y, 아니면 N
        String stmtDeniClas = "4".equals(dto.getStmtSndMtd()) ? "Y" : "N";

        // 기존 엔티티의 값 + 변경할 값 덮어쓰기
        BillEntity updated = BillEntity.builder()
                .custNo(existing.getCustNo())
                .stlAct(dto.getStlAct())
                .bnkCd(dto.getBnkCd())
                .dpsNm(dto.getHgNm())
                .stlMtd(dto.getStlMtd())
                .stlDd(dto.getStlDd())
                .stmtSndMtd(dto.getStmtSndMtd())
                .stmtDeniClas(stmtDeniClas)
                .billZip(dto.getBilladrZip())
                .billAdr1(dto.getBilladrAdr1())
                .billAdr2(dto.getBilladrAdr2())
                .emailAdr(dto.getEmailAdr())
                .lstOprTm(currentTime)
                .lstOprD(todayDate)
                .lstOprtEmpno(loginId)
                .build();

        billRepository.save(updated);
    }

    // 6. 재발급 고객 - 카드 테이블에 이전 카드 UPDATE하고 신규 카드 번호 INSERT
    public void reissueCrd(ApplicationDto dto, String loginId) {
        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String currentTime = java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        // 기존 카드 번호 정보 찾기
        CrdEntity oldCrd = crdRepository.findBySsnAndBrdAndLstCrdF(dto.getSsn(), dto.getBrd(), "1")
                .orElseThrow(() -> new IllegalStateException("재발급 대상 카드 못찾음: " + dto.getSsn()));


        oldCrd = oldCrd.toBuilder()
                .lstCrdF(null)
                .lstOprD(todayDate)
                .lstOprTm(currentTime)
                .lstOprtEmpno(loginId)
                .build();

        crdRepository.save(oldCrd);

        // 위에 메소드 사용해서 이전 고객 번호, 카드 번호 넣고 신규 카드 번호 생성함
        dto.setCustNo(oldCrd.getCustNo());
        dto.setBfCrdNo(oldCrd.getCrdNo());
        insertCrd(dto, loginId);
    }

    // 카드 매핑 테이블 등록
    public void insertSeqNo(ApplicationDto dto) {
        SeqnoEntity entity = SeqnoEntity.builder()
                .custNo(dto.getCustNo())
                .crdNo(dto.getCrdNo())
                .build();

        seqnoRepository.save(entity);
    }

    @Transactional
    public void updateImpApplication(ApplicationDto dto, String loginId) {
        String encryptedScrtNo = PasswordUtil.encryptSHA256(dto.getScrtNo());

        String oprD = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String oprTm = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        int updated = applicationRepository.updateImpApplication(dto, encryptedScrtNo, oprD, oprTm, loginId);
        if (updated == 0) {
            throw new IllegalArgumentException("수정할 신청서가 없습니다.");
        }
    }

    @Transactional
    public void updateNoseq(ApplicationDto dto) {
        int updated = noseqRepository.updateNoseq(dto);
        if (updated == 0) {
            throw new IllegalArgumentException("수정할 접수 매핑 데이터가 없습니다.");
        }
    }
}
