package com.example.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "TEACHER")
@SequenceGenerator(name = "SEQ1", sequenceName = "SEQ_TEACHER_NO", initialValue = 1, allocationSize = 1)
public class Teacher {

    @Id   // 강사번호
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ1" )
    private Long teacherno = 0L;

    @Column(length = 50) // 분야
    private String category;
    
    @Column(length = 1000) // 소개
    private String intro;

    @Column(length = 1)
    private int steps = 0; // 최초 등록 단계

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 등록일
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    @Column(name = "REGDATE", updatable = false)
    Date regdate = null;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL) // 외래키
    @JoinColumn(name = "userid", referencedColumnName = "ID")
    private Member member;

}
