package com.example.restcontroller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Comment;
import com.example.entity.Member;
import com.example.entity.ReComment;
import com.example.repository.CommentRepository;
import com.example.repository.CoursRepository;
import com.example.repository.MemberRepository;
import com.example.repository.ReCommentRepository;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(value = "/api/recomment")
@RequiredArgsConstructor
public class ReCommentRestController {

    final ReCommentRepository reCommentRepository;
    final CommentRepository commentRepository;
    final ReCommentRepository repository;
    final MemberRepository memberRepository;
    final CoursRepository coursRepository;

    @PostMapping(value="/insert.json")
    public Map<String,Object> insertcommentPOST(@RequestBody ReComment recomment
    , HttpServletRequest request, @RequestParam(name = "commentno")Long commentno){
        Map<String,Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            String username = (String) request.getAttribute("username");
            Member member = memberRepository.findById(username).orElse(null);
            Comment comment = commentRepository.findById(commentno).orElse(null);
            if (member != null && comment != null) {
                recomment.setCommentno(comment);
                recomment.setUserid(member);
                reCommentRepository.save(recomment);
                map.put("status", 200);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    
    
}
