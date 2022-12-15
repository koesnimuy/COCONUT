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
@Table(name = "CHAPTER")
@SequenceGenerator(name = "CHA", sequenceName = "SEQ_CHAPTER_NO", initialValue = 1, allocationSize = 1)
@Entity
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = "CHA" )
    Long no;

    @Column(length = 30)
    String title;

    @Column(length = 50)
    String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    @Column(name = "REGDATE", updatable = false)
    Date regdate = null;
    
    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "COURSCODE", referencedColumnName = "COURSNO")
    private Cours cours;

    @Transient
    Long coursno;
    
}
