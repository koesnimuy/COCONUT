package com.example.restcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Cours;
import com.example.entity.CoursImage;
import com.example.entity.Member;
import com.example.entity.Order;
import com.example.jwt.JwtUtil;
import com.example.repository.CoursImageREpository;
import com.example.repository.CoursRepository;
import com.example.repository.MemberRepository;
import com.example.repository.OrderRepository;

import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/order")
public class OrderRestController {
    
    final CoursImageREpository imageREpository;
    final OrderRepository orderRepository;
    final MemberRepository memberRepository;
    final CoursRepository coursRepository;
    final JwtUtil jwtUtil;


    //{"status" : 1} 스테이터스, 1 결완| 0 아님 , 아이디, 강의번호  
    @PostMapping(value="/insert.json")
    public Map<String,Object> insertORDERPOST(@RequestBody Order order,
        @RequestParam(name = "coursno")Long coursno, HttpServletRequest request) {
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            String username = (String) request.getAttribute("username");
            Member member = memberRepository.findById(username).orElse(null);
            Cours cours = coursRepository.findByCoursno(coursno);
            if (member != null && cours != null) {
            order.setCourscode(cours);
            order.setUserid(member);
            Order order1 = orderRepository.save(order);
            map.put("status", 200);
            map.put("orderno", order1.getOrderno());
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        
        return map;
    }

    //토큰으로 아이디받기 selectone의 강좌번호도주기
    @GetMapping(value="/search.json")
    public Map<String,Object> orderSearchGET(@RequestHeader(name = "token")String token,
        @RequestParam(name = "coursno")Long coursno) {
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            String username = jwtUtil.extractUsername(token);
            Order order = orderRepository.findByUserid_idAndCourscode_coursno(username, coursno);
                if (order != null) {
                    map.put("status", 200);
                    map.put("orderstatus", order.getStatus());
                }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    @GetMapping(value="/select.json")
    public Map<String,Object> ordermemberselectGET(
        HttpServletRequest request) {
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            String username = (String) request.getAttribute("username");
            List<Order> list = orderRepository.findByUserid_id(username);
            List<Long> coursno = new ArrayList<>();
            List<Long> imageno = new ArrayList<>();
            if (!list.isEmpty()) {
                for (Order order : list) {
                    coursno.add(order.getCourscode().getCoursno());
                    CoursImage image = 
                    imageREpository.findBycours_coursno(order.getCourscode().getCoursno());
                    imageno.add(image.getCoursimageno());
                }
                List<Cours> list1 = coursRepository.findAllByCoursnoIn(coursno);
               
                System.out.println(list1.toString());
                map.put("status", 200);
                map.put("courslist", list1);
                map.put("imageno", imageno);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }
    
    
    

}
