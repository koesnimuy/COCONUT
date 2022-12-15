package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name = "COURSIMAGE")
@SequenceGenerator(name = "COUIMG", sequenceName = "SEQ_COURSIMAGE_NO", initialValue = 1, allocationSize = 1)
public class CoursImage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COUIMG" )
    Long coursimageno;

    //어디꺼다

    @Column(length = 30)
    String imagename; // 이미지 이름

    Long imagesize; // 이미지 사이즈

    @Column(length = 30)
    String imagetype; // 이미지 타입

    @ToString.Exclude
    @Lob
    byte[] imagedata; // 이미지 데이타

    int repno;  // 0/1대표이미지구분

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    @Column(name = "REGDATE", updatable = false)
    private Date regdate =null; // 작성일

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne // 외래키
    @JoinColumn(name = "COURSCODE", referencedColumnName = "COURSNO")
    Cours cours;
}
