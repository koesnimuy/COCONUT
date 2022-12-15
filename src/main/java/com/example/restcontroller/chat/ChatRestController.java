package com.example.restcontroller.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Chat;
import com.example.entity.Member;
import com.example.jwt.JwtUtil;
import com.example.repository.MemberRepository;
import com.example.repository.chat.ChatRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/chat")
@RequiredArgsConstructor
public class ChatRestController {

    final JwtUtil jwtUtil;
    final MemberRepository memberRepository;
    final ChatRepository chatRepository;

    // 채팅 개설
    // 127.0.0.1:8081/COCO/api/chat/insert.json
    @PostMapping(value = "/insert.json")
    public Map<String, Object> insertPOST(@RequestBody Chat chat, @RequestHeader(name = "TOKEN") String token
        ,@RequestParam(name = "receiver") String receiver ){
        Map<String, Object> map = new HashMap<>();
        try{
            String username = jwtUtil.extractUsername(token);
            Member member = memberRepository.findById(username).orElse(null);
            chat.setMember(member);
            chat.setSender(username);
            chat.setReceiver(receiver);
            Chat ret = chatRepository.save(chat);
            map.put("status", 200);
            map.put("chat", ret);
        }
        catch(Exception e){
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // // 채팅 조회(보내는 사람 조회 + 토큰)
    // // 127.0.0.1:8081/COCO/api/chat/selectsendertoken.json
    // @GetMapping(value = "/selectsendertoken.json")
    // public Map<String, Object> selectsendertokenGET(@RequestHeader(name = "TOKEN") String token){
    //     Map<String, Object> map = new HashMap<>();
    //     try{
    //         String username = jwtUtil.extractUsername(token);
    //         List<Chat> list = chatRepository.findBySender(username);
    //         map.put("status", 200);
    //         map.put("chat1", list);
    //     }
    //     catch(Exception e){
    //         map.put("status", -1);
    //         map.put("result", e.getMessage());
    //     }
    //     return map;
    // }

    // 채팅 조회(보내는 사람 조회 + 토큰)
    // 127.0.0.1:8081/COCO/api/chat/selectsendertoken.json
    @GetMapping(value = "/selectsendertoken.json")
    public Map<String, Object> selectsendertokenGET(@RequestHeader(name = "TOKEN") String token,
    @RequestParam(name = "page", defaultValue = "1", required = false)int page){
        Map<String, Object> map = new HashMap<>();
        try{
            PageRequest pageRequest = PageRequest.of(page-1, 10);
            String username = jwtUtil.extractUsername(token);
            List<Chat> list = chatRepository.findBySenderOrderByChatnoDesc(username, pageRequest);
            int total = chatRepository.countBySender(username);
            map.put("total", total);
            map.put("status", 200);
            map.put("chat1", list);
        }
        catch(Exception e){
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
    // 채팅 조회(보내는 사람 조회)
    // 127.0.0.1:8081/COCO/api/chat/selectsender.json
    // @GetMapping(value = "/selectsender.json")
    // public Map<String, Object> selectsenderGET(@RequestHeader(name = "TOKEN") String token,
    //     @RequestParam(name = "sender") String sender){
    //     Map<String, Object> map = new HashMap<>();
    //     try{
    //         List<Chat> list = chatRepository.findBySender(sender);
    //         map.put("status", 200);
    //         map.put("chat", list);
    //     }
    //     catch(Exception e){
    //         map.put("status", -1);
    //         map.put("result", e.getMessage());
    //     }
    //     return map;
    // }
    
    // 채팅 조회(받는 사람 조회)
    // 127.0.0.1:8081/COCO/api/chat/selectreceiver.json
    // @GetMapping(value = "/selectreceiver.json")
    // public Map<String, Object> selectreceiverGET(@RequestHeader(name = "TOKEN") String token,
    // @RequestParam(name = "receiver") String receiver){
    //     Map<String, Object> map = new HashMap<>();
    //     try{
    //         List<Chat> list = chatRepository.findByReceiverOrderByChatnoDesc(receiver);
    //         map.put("status", 200);
    //         map.put("chat", list);
    //     }
    //     catch(Exception e){
    //         map.put("status", -1);
    //         map.put("result", e.getMessage());
    //     }
    //     return map;
    // }

    // 채팅 조회(받는 사람 조회)
    // 127.0.0.1:8081/COCO/api/chat/selectreceiver.json
    @GetMapping(value = "/selectreceiver.json")
    public Map<String, Object> selectreceiverGET(@RequestHeader(name = "TOKEN") String token,
    @RequestParam(name = "receiver") String receiver,
    @RequestParam(name = "page", defaultValue = "1", required = false)int page){
        Map<String, Object> map = new HashMap<>();
        try{
            PageRequest pageRequest = PageRequest.of(page-1, 10);
            List<Chat> list = chatRepository.findByReceiverOrderByChatnoDesc(receiver, pageRequest);
            int total = chatRepository.countByReceiver(receiver);
            map.put("total", total);
            map.put("status", 200);
            map.put("chat", list);
        }
        catch(Exception e){
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 채팅 삭제
    // 127.0.0.1:8081/COCO/api/chat/delete.json
    @PostMapping(value = "/delete.json")
    public Map<String, Object> deletePOST(@RequestHeader(name = "TOKEN") String token,
        @RequestParam(name = "chatno") Long chatno){
        Map<String, Object> map = new HashMap<>();
        try{
            chatRepository.deleteById(chatno);
            map.put("status", 200);
            map.put("chat", chatno);
        }
        catch(Exception e){
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
    // 채팅 일괄 삭제
    // 127.0.0.1:8081/COCO/api/chat/deletebatch.json
    @PostMapping(value = "/deletebatch.json")
    public Map<String, Object> deletebatchPOST(@RequestParam(name = "chatno") List<Chat> list){
        Map<String, Object> map = new HashMap<>();
        try{
            for(int i=0; i<list.size(); i++){
                chatRepository.deleteAll(list);
            }
            map.put("status", 200);
        }
        catch(Exception e){
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
}
