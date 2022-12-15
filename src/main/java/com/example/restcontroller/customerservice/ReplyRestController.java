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

import com.example.entity.Qna;
import com.example.entity.Reply;
import com.example.repository.customerservice.QnaRepository;
import com.example.repository.customerservice.ReplyRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/reply")
@RequiredArgsConstructor
public class ReplyRestController {
    
    final ReplyRepository replyRepository;
    final QnaRepository qnaRepository;

    // 127.0.0.1:8081/COCO/api/reply/insert.json
    @PostMapping(value = "/insert.json")
    public Map<String, Object> insertPOST(@RequestBody Reply reply, @RequestParam(name = "qnano") Long qnano,
        @RequestHeader(name = "token") String token){
        Map<String, Object> map = new HashMap<>();
        try{
            Qna qna = qnaRepository.findById(qnano).orElse(null);
            qna.setStatus("완료");
            qnaRepository.save(qna);
            reply.setQna(qna);
            replyRepository.save(reply);
            map.put("status", 200);
            map.put("reply", reply);
        }
        catch(Exception e){
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
    // 127.0.0.1:8081/COCO/api/reply/selectlist.json
    @GetMapping(value = "/selectlist.json")
    public Map<String, Object> selectlistPOST(@RequestParam(name = "qnano") Long qnano){
        Map<String, Object> map = new HashMap<>();
        try{
            Qna qna = qnaRepository.findById(qnano).orElse(null);
            List<Reply> list = replyRepository.findByQna(qna);
            map.put("status", 200);
            map.put("reply", list);
        }
        catch(Exception e){
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

}
