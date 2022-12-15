package com.example.restcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.CustomCommentDTO;
import com.example.dto.CustomCommentMemDTO;
import com.example.dto.CustomReCommentDTO;
import com.example.entity.Comment;
import com.example.entity.Cours;
import com.example.entity.Member;
import com.example.entity.ReComment;
import com.example.jwt.JwtUtil;
import com.example.repository.CommentRepository;
import com.example.repository.CoursRepository;
import com.example.repository.MemberRepository;
import com.example.repository.ReCommentRepository;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(value = "/api/comment")
@RequiredArgsConstructor
public class CommentRestController {
    
    final JwtUtil jwtUtil;
    final CommentRepository commentRepository;
    final ReCommentRepository repository;
    final MemberRepository memberRepository;
    final CoursRepository coursRepository;
    
    @PostMapping(value="/insert.json")
    public Map<String,Object> insertcommentPOST(@RequestBody Comment comment
    , HttpServletRequest request, @RequestParam(name = "coursno")Long coursno){
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            String username = (String) request.getAttribute("username");
            Member member = memberRepository.findById(username).orElse(null);
            Cours cours = coursRepository.findByCoursno(coursno);
            if (cours != null && member != null) {
                comment.setCourscode(cours);
                comment.setUserid(member);
                commentRepository.save(comment);
                map.put("status", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    @PutMapping(value="/update.json")
    public Map<String,Object> updatecomment(@RequestBody Comment comment){
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            Comment comment1 = commentRepository.findById(comment.getCommentno()).orElse(null);
            if (comment1 != null) {
                comment1.setTitle(comment.getTitle());
                comment1.setContent(comment.getContent());
                comment1.setSecret(comment.getSecret());
                commentRepository.save(comment1);
                map.put("status", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }
    @PostMapping(value="/delete.json")
    public Map<String,Object> deletecommentPOST(@RequestParam(name = "commentno")Long commentno){
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            Comment comment1 = commentRepository.findById(commentno).orElse(null);
            if (comment1 != null) {
                commentRepository.delete(comment1);
                map.put("status", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    //로그인안하면 토큰null
    @GetMapping(value="/selectcomment.json")
    public Map<String,Object> zxc(@RequestParam(name = "coursno")Long coursno,
        @RequestHeader(name = "TOKEN") String token, 
        @RequestParam(name = "page", defaultValue = "1", required = false)int page
        ){
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            int teacher = 0;
            String username = null;
            if (!token.equals("null")) {
                username = jwtUtil.extractUsername(token);
                Cours cours = coursRepository.findByCoursno(coursno);
                if (username.equals(cours.getTeachercode().getMember().getId())) {
                    teacher = 1;
                }
            }
            PageRequest pageRequest = PageRequest.of(page-1, 10);
            
            List<Comment> list = commentRepository.findByCourscode_coursnoOrderByCommentnoDesc(coursno, pageRequest);
            List<CustomCommentDTO> list1 = new ArrayList<>();
            if (!list.isEmpty()) {
                for (Comment comment : list) {
                    CustomCommentDTO custom = new CustomCommentDTO();
                    custom.setCommentno(comment.getCommentno());
                    custom.setRegdate(comment.getRegdate());
                    custom.setTitle(comment.getTitle());
                    custom.setUserid(comment.getUserid().getId());
                    custom.setSecret(comment.getSecret());
                    if (comment.getUserid().getId().equals(username) && comment.getSecret() == 1L) {
                        custom.setSecret(0L);
                        custom.setSecrertinfo(1L);
                    }
                    if (teacher == 1 && comment.getSecret() == 1L) {
                        custom.setSecret(0L);
                        custom.setSecrertinfo(1L);
                    }
                    int totalrecomment = repository.countByCommentno_commentno(comment.getCommentno());
                    custom.setTotalrecomment(totalrecomment);


                    list1.add(custom);
                }
                int total = commentRepository.countByCourscode_coursno(coursno);

                map.put("total", total);
                map.put("status", 200);
                map.put("comment", list1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    @GetMapping(value="/selectonecom.json")
    public Map<String,Object> selectonecomGET(@RequestParam(name = "commentno")Long commentno,
        @RequestParam(name = "page", required = false, defaultValue = "1")int page){
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            Comment comment = commentRepository.findById(commentno).orElse(null);
            if (comment != null) {
                List<CustomReCommentDTO> list1 = new ArrayList<>();
                PageRequest pageRequest = PageRequest.of(page-1, 5);
                List<ReComment> list = repository.findByCommentno_commentnoOrderByRecommentnoAsc(commentno, pageRequest);
                map.put("recomment", null);
                map.put("total", 0);
                if (!list.isEmpty()) {
                    int total = repository.countByCommentno_commentno(commentno);
                    for (ReComment reComment : list) {
                        CustomReCommentDTO custom = new CustomReCommentDTO();
                        custom.setRecommentno(reComment.getRecommentno());
                        custom.setContent(reComment.getContent());
                        custom.setUserid(reComment.getUserid().getId());
                        custom.setSecret(reComment.getSecret());
                        custom.setRegdate(reComment.getRegdate());
                        list1.add(custom);
                    }  
                    map.put("recomment", list1);
                    map.put("total", total);
                }
                
                map.put("status", 200);
                map.put("comment", comment);
                map.put("userid", comment.getUserid().getId());
                map.put("coursno", comment.getCourscode().getCoursno());
                map.put("courstitle", comment.getCourscode().getTitle());
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    @GetMapping(value="/selectmember.json")
    public Map<String,Object> selectmember(HttpServletRequest request,
    @RequestParam(name = "page", required = false, defaultValue = "1")int page){
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            String username = (String) request.getAttribute("username");
            PageRequest pageRequest = PageRequest.of(page-1, 10);
            List<Comment> list = commentRepository.findByUserid_idOrderByCommentnoDesc(username, pageRequest);
            int total = commentRepository.countByUserid_id(username);
            if (!list.isEmpty()) {
                List<CustomCommentMemDTO> list1 = new ArrayList<>();
                for (Comment comment : list) {
                    CustomCommentMemDTO custom = new CustomCommentMemDTO();
                    custom.setCommentno(comment.getCommentno());
                    custom.setCoursname(comment.getCourscode().getTitle());
                    custom.setCoursno(comment.getCourscode().getCoursno());
                    custom.setRegdate(comment.getRegdate());
                    custom.setSecret(comment.getSecret());
                    custom.setTitle(comment.getTitle());
                    int totalrecomment = repository.countByCommentno_commentno(comment.getCommentno());
                    custom.setTotalrecomment(totalrecomment);
                    list1.add(custom);
                }
                map.put("status", 200);
                map.put("total", total);
                map.put("list", list1);
            }
        } catch (Exception e) {
            map.put("status", -1);
            e.printStackTrace();
        }
        return map;

    }

    @GetMapping(value="/checkmemcom.json")
    public Map<String,Object> checkmember(HttpServletRequest request, @RequestParam(value = "commentno")Long commentno){
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            String username = (String) request.getAttribute("username");
            Comment comment = commentRepository.findById(commentno).orElse(null);
            if (comment != null) {
                String id = comment.getUserid().getId();
                if (username.equals(id)) {
                   map.put("status", 200); 
                }
            }
        } catch (Exception e) {
            map.put("status", -1);
            e.printStackTrace();
        }
        return map;

    }
    
    
}
