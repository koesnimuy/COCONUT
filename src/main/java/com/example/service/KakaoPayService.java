package com.example.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.dto.kakaopay.ApproveResponse;
import com.example.dto.kakaopay.ReadyResponse;
import com.example.entity.Order;
import com.example.repository.OrderRepository;

@Service
public class KakaoPayService {

    @Autowired
    OrderRepository orderRepository;

    private static final String HOST = "https://kapi.kakao.com";
    
    private ReadyResponse kakaoPayReadyVO;
    private ApproveResponse kakaoPayApprovalVO;

    
    public String kakaoPayReady(Long no) {
 
        System.out.println(no);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
 
        Order order = orderRepository.findById(no).orElse(null);
        if ( order != null) {
            // 서버로 요청할 Header
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "KakaoAK " + "266d16e86e45cd219e145b677b60b8ea");
            headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
            
            // 서버로 요청할 Body
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("cid", "TC0ONETIME"); //회사코드인데 실사용시필요
            params.add("partner_order_id", String.valueOf(order.getOrderno())); //주문에대한번호
            params.add("partner_user_id", "coconut"); //회사명
            params.add("item_name", order.getCourscode().getTitle()); //물품명 강좌이름
            params.add("item_code", String.valueOf(order.getCourscode().getCoursno()));// 물건코드
            params.add("quantity", "1"); //수량 1개 고정
            params.add("total_amount", String.valueOf(order.getCourscode().getPrice()));
            params.add("tax_free_amount", "0"); //세금면제 쓸모없음
            params.add("approval_url", "http://3.35.91.60:8080/COCO/payok"); //성공시나올페이지http://localhost:9090/paysuccess
            params.add("cancel_url", "http://3.35.91.60:8080/COCO/"); //취소시http://localhost:9090/
            params.add("fail_url", "http://3.35.91.60:8080/COCO/"); //실패시
    
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
    
            try {
                kakaoPayReadyVO = restTemplate.postForObject(new URI(HOST + "/v1/payment/ready"), requestEntity, ReadyResponse.class);
                kakaoPayReadyVO.setNo(no);
                
                return kakaoPayReadyVO.getNext_redirect_pc_url();
    
            } 
            catch (RestClientException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return "/";
        
    }
    
    public ApproveResponse kakaoPayInfo(String pg_token) {
 
        
        RestTemplate restTemplate = new RestTemplate();
 
        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + "266d16e86e45cd219e145b677b60b8ea");
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
 
        System.out.println(kakaoPayReadyVO.getNo());
        
        Order order = orderRepository.findById(kakaoPayReadyVO.getNo()).orElse(null);

        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", kakaoPayReadyVO.getTid());
        params.add("partner_order_id", String.valueOf(order.getOrderno()));
        params.add("partner_user_id", "coconut");
        params.add("pg_token", pg_token);
        params.add("total_amount", String.valueOf(order.getCourscode().getPrice()));
        
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        
        try {
            kakaoPayApprovalVO = restTemplate.postForObject(new URI(HOST + "/v1/payment/approve"), requestEntity, ApproveResponse.class);
          
            return kakaoPayApprovalVO;
        
        } catch (RestClientException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        
        return null;
    }

}
