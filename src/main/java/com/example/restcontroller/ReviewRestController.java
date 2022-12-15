package com.example.restcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.CustomReviewCours;
import com.example.dto.CustomReviewMem;
import com.example.entity.Cours;
import com.example.entity.CoursImage;
import com.example.entity.Member;
import com.example.entity.MemberImage;
import com.example.entity.Order;
import com.example.entity.Review;
import com.example.jwt.JwtUtil;
import com.example.repository.CoursImageREpository;
import com.example.repository.CoursRepository;
import com.example.repository.MemberImageRepository;
import com.example.repository.MemberRepository;
import com.example.repository.OrderRepository;
import com.example.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(value = "/api/review")
@RequiredArgsConstructor
public class ReviewRestController {

    final ReviewRepository reviewrepository;
    final JwtUtil jwtUtil;
    final MemberRepository memberRepository;
    final CoursRepository coursRepository;
    final OrderRepository orderRepository;
    final MemberImageRepository memberImageRepository;
    final CoursImageREpository coursImageREpository;

    @PostMapping("write.json")
    public Map<String,Object> writePOST(@RequestBody Review review,
    @RequestParam(name = "courscode") Long courscode,
    HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 0);
        System.out.println(courscode + "@@@@@@@@@@@@@@@@@@@@");
        try {
        String username = (String) request.getAttribute("username");
        Member member = memberRepository.findById(username).orElse(null);
        Cours cours = coursRepository.findById(courscode).orElse(null);
        Order order = orderRepository.findByUserid_idAndCourscode_coursno(member.getId(), cours.getCoursno());
        System.out.println(order.toString());    
        if (order.getStatus() == 1L) {
            review.setUserid(member);
            review.setCourscode(cours);
            reviewrepository.save(review);
            map.put("status", 200);
        }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    @GetMapping(value="/selectmem.json")
    public Map<String,Object> selectreviewmemberGET(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            String username = (String) request.getAttribute("username");
            List<Review> list = reviewrepository.findByUserid_id(username);
            if (list.isEmpty()) {
               return map;
            }
            List<CustomReviewMem> list2 = new ArrayList<>();
            for (Review review : list) {
                CustomReviewMem custom =new CustomReviewMem();
                custom.setReviewno(review.getReviewno());
                custom.setContent(review.getContent());
                custom.setCoursno(review.getCourscode().getCoursno());
                custom.setRating(review.getRating());
                custom.setRegdate(review.getRegdate());
                custom.setTitle(review.getCourscode().getTitle());
                CoursImage image = coursImageREpository.findBycours_coursno(review.getCourscode().getCoursno());
                custom.setImageno(image.getCoursimageno());
                list2.add(custom);
            }
            map.put("status", 200);
            map.put("review", list2);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    @GetMapping(value="/selectcours.json")
    public Map<String,Object> selectreviewcoursnoGET(@RequestParam(name = "courscode") Long courscode,
        @RequestParam(name = "page",defaultValue = "1",required = false)int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            PageRequest pageRequest = PageRequest.of(page-1, 10);

            List<Review> list = reviewrepository.findByCourscode_coursno(courscode, pageRequest);
            System.out.println(list.toString());
            if (list.isEmpty()) {
                return map;
            }
            int total = reviewrepository.countByCourscode_coursno(courscode);
            List<CustomReviewCours> list2 = new ArrayList<>();
            for (Review review : list) {
                CustomReviewCours custom = new CustomReviewCours();
                custom.setReviewno(review.getReviewno());
                custom.setContent(review.getContent());
                custom.setRating(review.getRating());
                custom.setRegdate(review.getRegdate());
                custom.setUserid(review.getUserid().getId());
                custom.setImageno(0L);
                MemberImage image = memberImageRepository.findByUserid(review.getUserid().getId());
                if (image != null) {
                    custom.setImageno(image.getNo());
                }
                list2.add(custom);
            }
            map.put("total", total);
            map.put("status", 200);
            map.put("review", list2);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    @GetMapping(value = "/checkreview.json")
    public Map<String,Object> checkreviewGET(@RequestParam(name = "courscode") Long courscode,
        HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 0);
        String username = (String) request.getAttribute("username");
        try {
        Review review = reviewrepository.findByUserid_idAndCourscode_coursno(username, courscode);
        if (review != null) {
            map.put("status", 200);
        }
        } catch (Exception e) {
           e.printStackTrace();
           map.put("status", -1);
        }
        return map;
    }
    
    

}
