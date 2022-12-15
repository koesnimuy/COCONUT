package com.example.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CustomReviewMem {
    
    Long reviewno;

    Double rating;

    String content;

    Date regdate;

    Long coursno;

    String title;

    Long imageno;
}
