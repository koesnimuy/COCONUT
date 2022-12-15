package com.example.entity;

import java.util.Date;

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
@Table(name = "EDUCATION")
@SequenceGenerator(name = "SEQ3", sequenceName = "SEQ_EDUCATION_NO", initialValue = 1, allocationSize = 1)
public class Education {

    @Id   // 학력번호
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ3" )
    private Long educationno;

    @Column(length = 50) // 기관이름
    private String name;
    
    @Column(length = 30) // 학과
    private String department;

    @Column(length = 20) // 상태
    private String status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 등록일
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    @Column(name = "REGDATE", updatable = false)
    Date regdate = null;
    
    @JsonIgnore
    @ToString.Exclude
    @ManyToOne // 외래키
    @JoinColumn(name = "teachercode", referencedColumnName = "TEACHERNO")
    private Teacher teacher;
    
}
