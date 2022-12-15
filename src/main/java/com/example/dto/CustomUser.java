package com.example.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 세션에 포함 내용을 추가하기위한 클래스 상속
@Getter
@Setter
@ToString
public class CustomUser extends User {
    String username;

    @ToString.Exclude
    String password;
    
    Collection<GrantedAuthority> authorities;
    
    public CustomUser(
        String username, 
        String password, 
        Collection<GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }
    
}
