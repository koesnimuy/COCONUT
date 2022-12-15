package com.example.service;

import org.springframework.stereotype.Service;

import com.example.repository.MemberImageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberImageServiceImpl implements MemberImageService {

    final MemberImageRepository memberImageRepository;
    
    @Override
    public boolean checkmemberImage(String userid) {
        try {
            if(memberImageRepository.countByUseridContaining(userid) > 0){
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;   
        }
    }
    
}
