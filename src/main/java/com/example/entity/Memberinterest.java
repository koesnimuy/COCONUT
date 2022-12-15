// package com.example.entity;

// import java.util.Date;

// import javax.persistence.Column;
// import javax.persistence.Entity;
// import javax.persistence.Id;
// import javax.persistence.JoinColumn;
// import javax.persistence.ManyToOne;
// import javax.persistence.SequenceGenerator;
// import javax.persistence.Table;

// import org.hibernate.annotations.CreationTimestamp;
// import org.springframework.format.annotation.DateTimeFormat;

// import lombok.Data;
// import lombok.NoArgsConstructor;
// import lombok.ToString;


// @Data
// @NoArgsConstructor

// @Entity
// @Table(name = "MEMBERINTEREST")
// @SequenceGenerator(name = "MemberInterest", sequenceName = "SEQ_MEMINTEREST_NO", initialValue = 1, allocationSize = 1)
// public class Memberinterest {
    
//     @Id
//     Long no;

//     @Column(length = 50)
//     String name;

//     // 등록일
//     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm.ss")
//     @CreationTimestamp
//     @Column(name = "REGDATE", updatable = false)
//     Date regdate = null; 

//     @ToString.Exclude
//     @ManyToOne
//     @JoinColumn(name = "USERID", referencedColumnName = "ID")
//     private String userid; 

// }
