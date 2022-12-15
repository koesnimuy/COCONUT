package com.example.service;

import org.springframework.stereotype.Service;

@Service
public interface MemberImageService {
    
    public boolean checkmemberImage(String userid);
}
