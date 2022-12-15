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
@Table(name = "REPLY")
@SequenceGenerator(name = "REPLY", sequenceName = "SEQ_REPLY_NO", initialValue = 1, allocationSize = 1)
public class Reply {

    @Id // 답글번호
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REPLY" )
    private Long replyno = 0L;
    
    @Column(length = 500) // 내용
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 등록일
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    @Column(name = "REGDATE", updatable = false)
    Date regdate = null;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne // 외래키
    @JoinColumn(name = "qnacode", referencedColumnName = "QNANO")
    private Qna qna;
    
}
