package com.example.service;

import org.springframework.stereotype.Service;

import com.example.entity.Token;
import com.example.repository.TokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    final TokenRepository tokenRepository;

    @Override
    public int upsertToken(Token token) {
        
        try {
            tokenRepository.save(token);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }   
    }

    @Override
    public String selectOneToken(String id) {
        try {
            Token token = tokenRepository.findById(id).orElse(null);
            return token.getToken();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Token selectOne(String id) {
        try {
            Token token = tokenRepository.findById(id).orElse(null);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int deleteToken(String id) {
        try {
            System.out.println(id);
            tokenRepository.deleteById(id);
            return 1;
        } catch (Exception e) {
            return 0;    
        }
    }
}
