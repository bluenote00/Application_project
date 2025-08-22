package com.example.application_project.service.application;

import com.example.application_project.entity.application.ApplicationEntity;
import com.example.application_project.repository.application.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    // 입회신청서 조회
    public Optional<ApplicationEntity> searchAppl(String ssn, LocalDate rcvD, int rcvSeqNo) {
        return applicationRepository.findBySsnAndRcvDAndRcvSeqNo(ssn, rcvD, rcvSeqNo);
    }
}
