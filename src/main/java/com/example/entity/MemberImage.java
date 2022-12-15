package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor

@Entity
@Table(name = "MEMBERIMAGE")
@SequenceGenerator(name = "MemberImage", sequenceName = "SEQ_MEMIMAGE_NO", initialValue = 1, allocationSize = 1)
public class MemberImage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = "MemberImage" )
    Long no;

    @Column(length = 80)
    String imagename;

    Long imagesize;

    @Column(length = 30)
    String imagetype;

    @ToString.Exclude
    @Lob
    byte[] imagedata;

    // 등록일
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    @Column(name = "REGDATE", updatable = false)
    Date regdate = null; 

    // @ToString.Exclude
    // @ManyToOne
    // @JoinColumn(name = "USERID")
    String userid;

}
