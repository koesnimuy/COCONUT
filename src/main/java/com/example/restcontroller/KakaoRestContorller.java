package com.example.restcontroller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.KakaoPayService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/kakao")
public class KakaoRestContorller {
    
    final KakaoPayService kakaoPayService;

    @PostMapping(value = "/kakaoPay.json")
    public String kakaoPay(@RequestParam(name = "orderno")Long no) {
        
        return kakaoPayService.kakaoPayReady(no);
    }

    @GetMapping("/Success.json")
    public Map<String,Object> kakaoPaySuccess(@RequestParam("pg_token") String pg_token) {
        Map<String,Object> map = new HashMap<>();
        map.put("info", kakaoPayService.kakaoPayInfo(pg_token));
        return map;
    }
}
