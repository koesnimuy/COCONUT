package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "FAQ")
@SequenceGenerator(name = "FAQ", sequenceName = "SEQ_FAQ_NO", initialValue = 1, allocationSize = 1)
public class Faq {

    @Id   // 질문번호
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAQ" )
    private Long faqno = 0L;
    
    @Column(length = 30) // 제목
    private String title;
    
    @Column(length = 500) // 내용
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")// 등록일
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    @Column(name = "REGDATE", updatable = false)
    Date regdate = null;

}
