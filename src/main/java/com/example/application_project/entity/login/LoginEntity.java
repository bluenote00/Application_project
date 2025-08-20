package com.example.application_project.entity.login;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "login")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginEntity {

    @Id
    @Column(name = "LOGIN_ID", nullable =false)
    private String loginId;

    @Column(name = "LOGIN_PW")
    private String loginPw;

    @Column(name = "NAME")
    private String name;

    @Column(name = "USER_ROLE")
    private String userRole;

}
