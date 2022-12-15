package com.example.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;

// 환경설정 없이 필터가 동작됨.
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter{

    final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, 
            HttpServletResponse response, 
            FilterChain filterChain) throws ServletException, IOException {
        try{                
            System.out.println("--------JwtFilter---------");
            System.out.println(request.getRequestURI());

            // 1. headers에 있는 token
            String token = request.getHeader("TOKEN");
            System.out.println(token);
            // 토큰 키가 없다면
            if(token == null){ 
                throw new Exception(); //강제로 오류 발생
            }

            // 토큰 내용이 없다면
            if(token.length() <= 0 ) {
                throw new Exception(); //강제로 오류 발생
            }

            // 토큰 검증
            String username = jwtUtil.extractUsername(token);
            System.out.println(username);
            // if(jwtUtil.validateToken(token, username) == false){
            //     throw new Exception(); //강제로 오류 발생
            // }

            // 권한이 맞지않으면
            String[] role = jwtUtil.extractRole(token);
            // 아래가 수행되어야 restcontroller가 동작됨.
            request.setAttribute("username", username);
            request.setAttribute("role", role);
            filterChain.doFilter(request, response);
        }
        catch(Exception e){
            e.printStackTrace();
            response.sendError(-1, "token error");
        }
    }
}