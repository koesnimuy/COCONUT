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
@Data
@Table(name = "Wish")
@SequenceGenerator(name = "WISH1", sequenceName = "SEQ_WISH_NO", initialValue = 1, allocationSize = 1)
public class Wish {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = "WISH1" )
    Long wishno;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    @Column(name = "REGDATE", updatable = false)
    Date regdate;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "USERID", referencedColumnName = "ID")
    private Member userid; 

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "COURSCODE", referencedColumnName = "COURSNO")
    private Cours courscode;  

}