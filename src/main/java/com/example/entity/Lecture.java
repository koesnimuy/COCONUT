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

@Entity
@Data
@Table(name = "LECTURE")
@SequenceGenerator(name = "LET", sequenceName = "SEQ_LECTURE_NO", initialValue = 1, allocationSize = 1)
public class Lecture {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = "LET" )
    Long no;

    @Column(length = 100)
    String title;

    @Column(length = 1000)
    String content;

    int free;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    @Column(name = "REGDATE", updatable = false)
    Date regdate = null;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "chaptercode", referencedColumnName = "no")
    private Chapter chapter; 
    
    @Transient
    Long chapterno;

}
