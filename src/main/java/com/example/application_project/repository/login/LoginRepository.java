package com.example.application_project.repository.login;

import com.example.application_project.entity.login.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<LoginEntity, String> {
    // ID로 사용자 조회
    Optional<LoginEntity> findByLoginId(String loginId);
}
