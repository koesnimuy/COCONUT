package com.example.service;

import org.springframework.stereotype.Service;

import com.example.entity.Token;

@Service
public interface TokenService {
    
    // 토큰 추가,수정
    public int upsertToken(Token token);

    public String selectOneToken(String id);

    public Token selectOne(String id);

    public int deleteToken(String id);
}
