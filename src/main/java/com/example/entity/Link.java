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
@Table(name = "LINK")
@SequenceGenerator(name = "SEQ4", sequenceName = "SEQ_LINK_NO", initialValue = 1, allocationSize = 1)
public class Link {
    
    @Id   // 학력번호
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ4" )
    private Long linkno;

    @Column(length = 20)
    private String platform; // 플렛폼

    @Column(length = 30)
    private String url; // url 주소

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
