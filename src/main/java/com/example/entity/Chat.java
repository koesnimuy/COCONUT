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
@Table(name = "CHAT")
@SequenceGenerator(name = "CHAT", sequenceName = "SEQ_CHAT_NO", initialValue = 1, allocationSize = 1)
public class Chat {

    @Id   // 채팅번호
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHAT" )
    private Long chatno = 0L;

    @Column(length = 500) // 내용
    private String content;

    @Column(length = 100) // 보내는 사람
    private String sender;

    @Column(length = 100) // 받는 사람
    private String receiver;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")// 등록일
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    @Column(name = "REGDATE", updatable = false)
    Date regdate = null;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne // 외래키
    @JoinColumn(name = "userid", referencedColumnName = "ID")
    private Member member;

}
