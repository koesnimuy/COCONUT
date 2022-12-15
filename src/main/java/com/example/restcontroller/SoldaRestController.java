package com.example.restcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.dto.CustomOrderDTO;
import com.example.dto.CustomReviewAll;
import com.example.entity.Chat;
import com.example.entity.Cours;
import com.example.entity.CoursImage;
import com.example.entity.InterestCategori;
import com.example.entity.Member;
import com.example.entity.MemberImage;
import com.example.entity.Order;
import com.example.entity.Review;
import com.example.entity.Teacher;
import com.example.jwt.JwtUtil;
import com.example.repository.CategoriRepository;
import com.example.repository.CoursImageREpository;
import com.example.repository.CoursRepository;
import com.example.repository.MemberImageRepository;
import com.example.repository.MemberRepository;
import com.example.repository.OrderRepository;
import com.example.repository.ReviewRepository;
import com.example.repository.chat.ChatRepository;
import com.example.repository.teacher.TeacherRepository;
import com.example.service.MemberService;
import com.example.service.TeacherService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "api/solda")
@RequiredArgsConstructor
public class SoldaRestController {

    final MemberService memberService;
    final TeacherRepository teacherRepository;
    final MemberRepository memberRepository;
    final JwtUtil jwtUtil;
    final TeacherService teacherService;
    final CoursRepository coursRepository;
    final CoursImageREpository coursimageREpository;
    final CategoriRepository categoriRepository;
    final ReviewRepository reviewRepository;
    final OrderRepository orderRepository;
    final CoursImageREpository coursImageRepository;
    final MemberImageRepository memberImageRepository;
    final ChatRepository chatRepository;

    @GetMapping(value = "/selectonechat.json")
    public Map<String, Object> selectOneChatGET(
        @RequestParam(name = "chatno") Long chatno) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            Chat chat = chatRepository.findByChatno(chatno);
            map.put("status", 200);
            map.put("chat", chat);
        }
        catch(Exception e) {
            map.put("status", -1);
            e.printStackTrace();
        }
        return map;
    }

    @GetMapping(value = "/selectreview.json")
    public Map<String, Object> selectReviewGET() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            List<Review> list = reviewRepository.findAll();
            if(list.isEmpty()) {
                return map;
            }
            List<CustomReviewAll> list2 = new ArrayList<>();
            for (Review review : list) {
                CustomReviewAll custom = new CustomReviewAll();
                custom.setReviewno(review.getReviewno());
                custom.setContent(review.getContent());
                custom.setRating(review.getRating());
                custom.setRegdate(review.getRegdate());
                custom.setCoursno(review.getCourscode().getCoursno());
                custom.setTitle(review.getCourscode().getTitle());
                custom.setUserid(review.getUserid().getId());
                custom.setImageno(0L);
                MemberImage image = memberImageRepository.findByUserid(review.getUserid().getId());
                CoursImage coursimg = coursImageRepository.findBycours_coursno(review.getCourscode().getCoursno());
                custom.setCoursimageno(coursimg.getCoursimageno());
                if(image != null) {
                    custom.setImageno(image.getNo());
                }
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

    // 회원 아이디로 바로 이미지 조회
    // 127.0.0.1:8081/COCO/api/solda/userimage.json
    @GetMapping(value = "userimage.json")
    public Map<String, Object> userImageGET(
        @RequestParam(name = "userid") String id) {
            Map<String, Object> map = new HashMap<>();
            map.put("status", 0);
            try {
                Member member = memberRepository.findById(id).orElse(null);
                MemberImage image = memberImageRepository.findByUserid(member.getId());
                if(image != null) {
                    Long no = image.getNo();
                    map.put("status", 200);
                    map.put("imageno", no);
                }
            }
            catch(Exception e) {
                e.printStackTrace();
                map.put("status", -1);
            }
            return map;
        }

    // 127.0.0.1:8081/COCO/api/solda/selectcours.json   
    @GetMapping(value = "selectcours.json")
    public Map<String, Object> selectCoursGET(
        @RequestHeader(name = "TOKEN") String token
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            Member member = memberRepository.findById(username).orElse(null);
            Long teacherno = teacherRepository.findByMember(member).getTeacherno();
            Teacher teacher = teacherRepository.findById(teacherno).orElse(null);
            List<Cours> cours = coursRepository.findByTeachercode(teacher);
            // if(member.getId() != teacher.getMember().getId()) {
            //     map.put("status", 0);
            // }
            // else {
                
                map.put("status", 200);
                map.put("cours", cours);
            // }
        }
        catch(Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    //강좌만들기
    @PostMapping(value="/insertCours.json")
    public Map<String,Object> CoursMakingPOST(@ModelAttribute Cours cours,
    @RequestParam(name = "name") String[] name,
    @RequestHeader(name = "TOKEN") String token,
    @RequestParam(name = "file") MultipartFile file) {
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
        String username = jwtUtil.extractUsername(token);
        Member member = memberRepository.findById(username).orElse(null);
        if (member != null) {
            Teacher teacher = teacherRepository.findByMember(member);
            cours.setTeachercode(teacher);
            Cours cours1 = coursRepository.save(cours);
                    
            //카테고리여러개받기
                    List<InterestCategori> list1 = new ArrayList<>();
                    for (int j = 0; j < name.length; j++) {
                        InterestCategori category = new InterestCategori();
                        category.setName(name[j]);
                        category.setCourscode(cours1);
                        list1.add(category);
                    }
                    categoriRepository.saveAll(list1);
                    // 수정 전
                    //이미지
                        CoursImage image = new CoursImage();
                        image.setImagetype(file.getContentType());
                        image.setImagename(file.getOriginalFilename());
                        image.setImagesize(file.getSize());
                        image.setImagedata(file.getBytes());
                        image.setCours(cours1);
                        image.setRegdate(null);
                    coursimageREpository.save(image);
                    
                    map.put("status", 200);
            }
            
        
        } catch (Exception e) {
        e.printStackTrace();
        map.put("status", -1);
        }
        return map;
    }


    // 강사 권한부여 하지말고 teacher table만 생성
    // 127.0.0.1:8081/COCO/api/solda/jointest.json
    @PostMapping(value = "/jointest.json")
    public Map<String, Object> jointestPOST(
            @RequestBody Teacher teacher, 
            @RequestHeader(name = "TOKEN") String token) {
        Map<String, Object> map = new HashMap<>();
            try {
                String username = jwtUtil.extractUsername(token);
                Member member = memberRepository.findById(username).orElse(null);
                member.setId(username);

                teacher.setMember(member);
                Teacher teacher1 = teacherRepository.save(teacher);
                map.put("status", 200);
                map.put("teacher", teacher1);
            }
            catch(Exception e) {
                map.put("status", -1);
                map.put("result", e.getMessage());
            }
        return map;
    }

    // 별점 평균
    // 127.0.0.1:8081/COCO/api/solda/ratingavg.json
    @GetMapping(value = "ratingavg.json")
    public Map<String, Object> ratingAvgGET(
        @RequestParam(name = "courscode") Long courscode) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            List<Review> list = reviewRepository.findByCourscode_coursno(courscode);
            if(list.isEmpty()) {
                return map;
            }
            ArrayList<Double> list2 = new ArrayList<>();
            list2.addAll(list.stream().map(Review::getRating).collect(Collectors.toList()));
            Double sum = 0d;
            Double avg = 0d;
            int count = 0;
            count = list2.size();
            for(int i = 0; i < list2.size(); i ++) {
                    sum += list2.get(i);
                }
            avg = sum/list2.size();
  
            map.put("status", 200);
            map.put("average", avg);
            map.put("count", count);
        } catch(Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    // selectOrderByMember
    // 127.0.0.1:8081/COCO/api/solda/selectorder.json
    @GetMapping(value = "/selectorder.json")
    public Map<String, Object> selectOrderGET(
        HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            String username = (String) request.getAttribute("username");
            List<Order> list = orderRepository.findByUserid_id(username);
            List<CustomOrderDTO> list1 = new ArrayList<>();
            if(!list.isEmpty()) {
                for (Order order : list) {
                    CustomOrderDTO custom = new CustomOrderDTO();
                    Cours cours = order.getCourscode();
                    custom.setOrderno(order.getOrderno());
                    custom.setStatus(order.getStatus());
                    custom.setCoursno(cours.getCoursno());
                    custom.setDifficult(cours.getDifficult());
                    custom.setInstrument(cours.getInstrument());
                    custom.setPrice(cours.getPrice());
                    custom.setTitle(cours.getTitle());
                    CoursImage image = coursImageRepository.findBycours_coursno(cours.getCoursno());
                    if(image != null) {
                        custom.setImageno(image.getCoursimageno());
                    }
                    Teacher teacher = teacherRepository.findById(cours.getTeachercode().getTeacherno()).orElse(null);
                    if(teacher != null) {
                        custom.setTeachername(teacher.getMember().getName());
                    }
                    list1.add(custom);
                }
                map.put("order", list1);
                map.put("status", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

}
