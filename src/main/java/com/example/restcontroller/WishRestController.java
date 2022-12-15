package com.example.restcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.CustomWish;
import com.example.entity.Cours;
import com.example.entity.CoursImage;
import com.example.entity.Member;
import com.example.entity.Teacher;
import com.example.entity.Wish;
import com.example.repository.CoursImageREpository;
import com.example.repository.CoursRepository;
import com.example.repository.MemberRepository;
import com.example.repository.WishRepository;
import com.example.repository.teacher.TeacherRepository;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(value = "/api/wish")
@RequiredArgsConstructor
public class WishRestController {
    
    final WishRepository wishRepository;
    final CoursRepository coursRepository;
    final MemberRepository memberRepository;
    final CoursImageREpository imageREpository;
    final TeacherRepository teacherRepository;


    // 장바구니추가 
    @PostMapping(value="/add.json")
    public Map<String,Object> addwishPOST(HttpServletRequest request, 
        @RequestParam(name = "coursno") Long coursno) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            Wish wish = new Wish();
            String username = (String) request.getAttribute("username");
            Cours cours = coursRepository.findByCoursno(coursno);
            Member member = memberRepository.findById(username).orElse(null);
            if (cours != null && member != null) {
            wish.setCourscode(cours);
            wish.setUserid(member);
            wishRepository.save(wish);
            map.put("status", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    @PostMapping(value="/delete.json")
    public Map<String,Object> deletewishPOST(@RequestParam(name = "wishno")Long wishno) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            Wish wish = wishRepository.findById(wishno).orElse(null);
            if (wish != null) {
                wishRepository.delete(wish);
                map.put("status", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    @GetMapping(value="/check.json")
    public Map<String,Object> checkwishGET(
        @RequestParam(name = "coursno")Long coursno, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            String username = (String) request.getAttribute("username");
            List<Wish> list = wishRepository.findByUserid_id(username);
            for (Wish wish : list) {
                if ( wish.getCourscode().getCoursno() == coursno) {
                    map.put("wishcheck", wish.getWishno());
                    map.put("status", 200);
                    break;
                }
            }
           
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    @GetMapping(value = "/selectwish.json")
    public Map<String,Object> selectwishGET(
        HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            String username = (String) request.getAttribute("username");
            List<Wish> list = wishRepository.findByUserid_id(username);
            List<CustomWish> list1 = new ArrayList<>();
            if (!list.isEmpty()) {
                for (Wish wish : list) {
                    CustomWish custom = new CustomWish();
                    Cours cours = wish.getCourscode();
                    custom.setWishno(wish.getWishno());
                    custom.setCoursno(cours.getCoursno());
                    custom.setDifficult(cours.getDifficult());
                    custom.setInstrument(cours.getInstrument());
                    custom.setPrice(cours.getPrice());
                    custom.setTitle(cours.getTitle());
                    CoursImage image = imageREpository.findBycours_coursno(cours.getCoursno());
                    if (image != null) {
                        custom.setIagmeno(image.getCoursimageno());
                    }
                    Teacher teacher = teacherRepository.findById(cours.getTeachercode().getTeacherno()).orElse(null);
                    if (teacher != null) {
                        custom.setTeachername(teacher.getMember().getName());
                    }
                    list1.add(custom);
                }
                map.put("wish", list1);
                map.put("status", 200);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }
    
    
    

}
