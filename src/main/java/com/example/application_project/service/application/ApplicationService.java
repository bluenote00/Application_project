package com.example.application_project.service.application;

import com.example.application_project.dto.application.ApplicationDto;
import com.example.application_project.entity.application.ApplicationEntity;
import com.example.application_project.repository.application.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    // 입회신청서 조회
    public Optional<ApplicationEntity> searchAppl(String ssn, LocalDate rcvD, String rcvSeqNo) {
        return applicationRepository.findBySsnAndRcvDAndRcvSeqNo(ssn, rcvD, rcvSeqNo);
    }

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
}
