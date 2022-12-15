package com.example.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.jwt.JwtFilter;

// 필터를 통과할 url 설정하기
@Configuration
public class FilterConfig {
    
    @Bean
    public FilterRegistrationBean<JwtFilter> filterRegistrationBean(JwtFilter jwtFilter){
        FilterRegistrationBean<JwtFilter> bean = new FilterRegistrationBean<>();            
        bean.setFilter(jwtFilter); 
        bean.addUrlPatterns(
            "/api/member/update.json", 
            "/api/member/updatepw.json", 
            "/api/member/selectone.json", 
            "/api/member/tokenlogin.json", 
            "/api/member/selectonerole.json",
            "/api/member/logout.json",
            "/api/member/delete.json",
            "/api/memberimg/insert.json",
            "/api/board/insert.json",
            "/api/board/insert50.json",
            "/api/board/update.json",
            "/api/board/delete.json",
            "/api/boardreply/insert.json",
            "/api/boardreply/update.json",
            "/api/boardreply/delete.json",
            "/api/boardlike/insert.json",
            "/api/admin/*",
            "/api/order/select.json",
            "/api/wish/add.json",
            "/api/wish/check.json",
            "/api/wish/selectwish.json",
            "/api/review/write.json",
            "/api/comment/insert.json",
            "/api/recomment/insert.json",
            "/api/order/insert.json",
            "/api/review/selectmem.json",
            "/api/solda/selectorder.json",
            "/api/review/checkreview.json",
            "/api/comment/selectmember.json",
            "/api/comment/checkmemcom.json"
            
        );

        return bean;
    }
}
