package com.example.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CustomReviewAll {
    
    Long reviewno;

    String title;
    
    String content;
    
    Double rating;

    Date regdate;

    Long coursno;

    String userid;

    Long imageno;

    Long coursimageno;
}
