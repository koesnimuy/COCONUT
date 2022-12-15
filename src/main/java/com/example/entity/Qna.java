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
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "QNA")
@SequenceGenerator(name = "QNA", sequenceName = "SEQ_QNA_NO", initialValue = 1, allocationSize = 1)
public class Qna {

    @Id   // 문의번호
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QNA" )
    private Long qnano = 0L;
    
    @Column(length = 30) // 제목
    private String title;
    
    @Column(length = 20) // 상태
    private String status;
    
    @Column(length = 20) // 문의유형
    private String type;
    
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
    @JoinColumn(name = "userid", referencedColumnName = "ID")
    private Member member;

    @Transient
    String userid;
}
