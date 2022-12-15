package com.example.dto;

import lombok.Data;

@Data
public class CustomOrderDTO {
    
    Long orderno;

    Long coursno;

    Long status;

    String title;

    String difficult;

    Long price;

    String teachername;

    String instrument;

    Long imageno;
}
