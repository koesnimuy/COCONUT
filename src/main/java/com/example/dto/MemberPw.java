package com.example.dto;

import lombok.Data;

@Data
public class MemberPw {
    
    String id;

    // 현재 비밀번호
    String pw;

    // 바꿀 비밀번호
    String updatepw;
}
