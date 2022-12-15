package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.service.KakaoPayService;

@Controller
public class KakaoPayController {

    @Autowired
    private KakaoPayService kakaopay;
    
    
    @GetMapping("/kakaoPay")
    public void kakaoPayGet() {

    }
    
    @PostMapping("/kakaoPay")
    public String kakaoPay(@RequestParam(name = "orderno")Long no) {
        
        return "redirect:" + kakaopay.kakaoPayReady(no);
 
    }
    
    @GetMapping("/Success.do")
    public String kakaoPaySuccess(@RequestParam("pg_token") String pg_token, Model model) {
        model.addAttribute("info", kakaopay.kakaoPayInfo(pg_token));
        
        return "kakopay/success";
    }

  
}
