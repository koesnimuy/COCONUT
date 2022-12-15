package com.example.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CustomReviewCours {
    
    Long reviewno;

    Double rating;

    String content;

    Date regdate;

    Long imageno;

    String userid;
}