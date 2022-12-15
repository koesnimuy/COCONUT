package com.example.restcontroller.customerservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Faq;
import com.example.repository.customerservice.FaqRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/faq")
@RequiredArgsConstructor
public class FaqRestController {

    final FaqRepository faqRepository;

    // 127.0.0.1:8081/COCO/api/faq/insert.json
    @PostMapping(value = "/insert.json") // 입력
    public Map<String, Object> insertPOST(@RequestBody Faq faq){
        Map<String, Object> map = new HashMap<>();
        try{
            faqRepository.save(faq);
            map.put("status", 200);
            map.put("faq", faq);
        }
        catch(Exception e){
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
    // 127.0.0.1:8081/COCO/api/faq/select.json
    @GetMapping(value = "/select.json") // 조회
    public Map<String, Object> selectPOST(){
        Map<String, Object> map = new HashMap<>();
        try{
            List<Faq> list = faqRepository.findAll();
            map.put("status", 200);
            map.put("faq", list);
        }
        catch(Exception e){
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
    // 127.0.0.1:8081/COCO/api/faq/update.json
    @PostMapping(value = "/update.json") // 수정
    public Map<String, Object> updatePOST(@RequestBody Faq faq, @RequestParam(name = "faqno") Long faqno){
        Map<String, Object> map = new HashMap<>();
        try{
            Faq faq2 = faqRepository.findById(faqno).orElse(null);
            faq2.setContent(faq.getContent());
            faq2.setTitle(faq.getTitle());
            faqRepository.save(faq2);
            map.put("status", 200);
            map.put("faq", faq2);
        }
        catch(Exception e){
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
    // 127.0.0.1:8081/COCO/api/faq/delete.json
    @PostMapping(value = "/delete.json") // 삭제
    public Map<String, Object> deletePOST(@RequestParam(name = "faqno") Long faqno){
        Map<String, Object> map = new HashMap<>();
        try{
            faqRepository.deleteById(faqno);
            map.put("status", 200);
            map.put("faq", faqno);
        }
        catch(Exception e){
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
}
