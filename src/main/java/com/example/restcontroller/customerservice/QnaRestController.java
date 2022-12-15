package com.example.restcontroller.customerservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Member;
import com.example.entity.Qna;
import com.example.entity.Reply;
import com.example.jwt.JwtUtil;
import com.example.repository.MemberRepository;
import com.example.repository.customerservice.QnaRepository;
import com.example.repository.customerservice.ReplyRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/qna")
public class QnaRestController {

    final QnaRepository qnaRepository;
    final JwtUtil jwtUtil;
    final MemberRepository memberRepository;
    final ReplyRepository replyRepository;

    // 127.0.0.1:8081/COCO/api/qna/insert.json
    @PostMapping(value = "insert.json") // 입력
    public Map<String, Object> insertPOST(@RequestBody Qna qna, @RequestHeader(name = "token") String token){
        Map<String, Object> map = new HashMap<>();
        try{
            String username = jwtUtil.extractUsername(token);
            Member member = memberRepository.findById(username).orElse(null);
            qna.setMember(member);
            qna.setStatus("대기");
            qnaRepository.save(qna);
            map.put("status", 200);
            map.put("qna", qna);
        }
        catch(Exception e){
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
    // 127.0.0.1:8081/COCO/api/qna/select.json
    @GetMapping(value = "select.json") // 조회
    public Map<String, Object> selectPOST(@RequestHeader(name = "token") String token){
        Map<String, Object> map = new HashMap<>();
        try{
            String username = jwtUtil.extractUsername(token);
            Member member = memberRepository.findById(username).orElse(null);
            List<Qna> list = qnaRepository.findByMember(member);

            if(list == null){
                map.put("status", 0);
                map.put("result", list);
            }
            else{
                map.put("status", 200);
                map.put("qna", list);
            }
        }
        catch(Exception e){
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
    // 127.0.0.1:8081/COCO/api/qna/selectone.json
    @GetMapping(value = "selectone.json") // 한개 조회
    public Map<String, Object> selectonePOST(@RequestParam(name = "qnano") Long qnano){
        Map<String, Object> map = new HashMap<>();
        try{
            Qna qna = qnaRepository.findById(qnano).orElse(null);
            map.put("status", 200);
            map.put("qna", qna);
        }
        catch(Exception e){
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
    //  127.0.0.1:8081/COCO/api/qna/update.json
    @PostMapping(value = "update.json") // 수정
    public Map<String, Object> updatePOST(@RequestBody Qna qna, @RequestHeader(name = "token") String token
        ,@RequestParam(name = "qnano") Long qnano){
        Map<String, Object> map = new HashMap<>();
        try{
            String username = jwtUtil.extractUsername(token);
            Member member = memberRepository.findById(username).orElse(null);
            List<Qna> list = qnaRepository.findByMember(member);
            list.get(0).getMember();
            Qna qna2 = qnaRepository.findById(qnano).orElse(null);
            qna2.setContent(qna.getContent());
            qna2.setStatus("대기");
            qna2.setTitle(qna.getTitle());
            qna2.setType(qna.getType());
            qna2.setMember(member);
            qnaRepository.save(qna2);
            map.put("status", 200);
            map.put("qna", qna2);
        }
        catch(Exception e){
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
    // 127.0.0.1:8081/COCO/api/qna/delete.json
    @PostMapping(value = "/delete.json") // 삭제
    public Map<String, Object> deletePOST(@RequestHeader(name = "TOKEN") String token,
        @RequestParam(name = "qnano") Long qnano){
        Map<String, Object> map = new HashMap<>();
        try{
            Qna qna = qnaRepository.findById(qnano).orElse(null);
            List<Reply> list = replyRepository.findByQna(qna);
            replyRepository.deleteAll(list);
            qnaRepository.deleteById(qnano);
            map.put("status", 200);
        }
        catch(Exception e){
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
}
