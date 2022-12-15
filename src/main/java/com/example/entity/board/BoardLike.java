package com.example.entity.board;

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

import com.example.entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor

@Entity
@Table(name = "BOARDLIKE")
@SequenceGenerator(name = "BOARDLIKE", sequenceName = "SEQ_BOARDLIKE_NO", initialValue = 1, allocationSize = 1)
public class BoardLike {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = "BOARDLIKE" )
    Long no;

    // 등록일
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    @Column(name = "REGDATE", updatable = false)
    Date regdate = null;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "BOARDNO", referencedColumnName = "NO")
    private Board boardno; 

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "USERID", referencedColumnName = "ID")
    private Member userid; 

}
