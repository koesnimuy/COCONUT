package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor

@Entity
@Table(name = "MEMBER")
public class Member {
    
    @Id
    @Column(length = 80)
    String id;

    @Column(length = 150)
    String pw;

    @Column(length = 30)
    String name;

    @Column(length = 50)
    String phone;

    @Column(length = 1)
    int block;

    @Column(length = 80)
    String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    @Column(name = "REGDATE", updatable = false)
    Date regdate = null;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "BIRTHDAY", updatable = true)
    Date birthday = null;

    @Transient
    String role;

    @Transient
    String img;

    @Transient
    Long teacherno;
}
