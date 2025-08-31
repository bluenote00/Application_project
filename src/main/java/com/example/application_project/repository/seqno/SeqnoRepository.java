package com.example.application_project.repository.seqno;

import com.example.application_project.entity.login.LoginEntity;
import com.example.application_project.entity.seqno.SeqnoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeqnoRepository extends JpaRepository<SeqnoEntity, String> {
}
