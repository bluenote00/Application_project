package com.example.application_project.repository.noseq;

import com.example.application_project.entity.login.LoginEntity;
import com.example.application_project.entity.noseq.NoseqEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoseqRepository extends JpaRepository<NoseqEntity, String> {
}
