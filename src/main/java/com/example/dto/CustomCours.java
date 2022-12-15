package com.example.dto;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomCours {
    //여기로 리뷰테이블 엮어서?
    Long coursno;

    String title;

    String intro;

    String difficult;

    String period;

    Long price;

    String status;

    Date regdate;

    Long imageno;

    String instrument;

    // 평점평균
    Double ratingavg = 0.0;

    long rown;

    String teachername;

    long reviewtotal; 

    long ordertotal;

    List<String> name;

}
