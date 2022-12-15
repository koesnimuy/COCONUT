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

@Entity
@Table(name = "COURS")
@Data
@SequenceGenerator(name = "COU1", sequenceName = "SEQ_COURS_NO", initialValue = 1, allocationSize = 1)
public class Cours {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = "COU1" )
    Long coursno;

    @Column(length = 50)
    String title;

    @Column(length = 500)
    String intro;

    @Column(length = 50)
    String difficult;

    @Column(length = 20)
    String period;

    Long price;

    @Column(length = 10)
    String instrument;

    @Column(length = 20)
    String status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    @Column(name = "REGDATE", updatable = false)
    Date regdate;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "teachercode", referencedColumnName = "teacherno")
    private Teacher teachercode;

}
