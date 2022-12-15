package com.example.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CustomReCommentDTO {
    

    String content;

    Date regdate;

    Long secret;

    Long recommentno;

    String userid;
}
