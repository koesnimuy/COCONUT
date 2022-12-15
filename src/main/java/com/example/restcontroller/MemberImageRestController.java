package com.example.restcontroller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.MemberImage;
import com.example.jwt.JwtUtil;
import com.example.repository.MemberImageRepository;
import com.example.service.MemberImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/memberimg")
@RequiredArgsConstructor
public class MemberImageRestController {

    final MemberImageRepository memberImageRepository;
    final MemberImageService memberImageService;
    final JwtUtil jwtUtil;
    
    // 127.0.0.1:8081/COCO/api/memberimg/insert.json
    @PostMapping(value="/insert.json")
    public Map<String, Object> insertPOST(
        HttpServletRequest request,
        @ModelAttribute MemberImage memberImage, @RequestParam(name = "file") MultipartFile file ) throws IOException  {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = (String) request.getAttribute("username");
            System.out.println(username);
            memberImage.setUserid(username);

            map.put("status", 0);
            if(memberImageService.checkmemberImage(username)){
                memberImage.setImagedata(file.getBytes());
                memberImage.setImagename(file.getOriginalFilename());
                memberImage.setImagesize(file.getSize());
                memberImage.setImagetype(file.getContentType());
            
                System.out.println(memberImage.getImagename().toString());
                memberImageRepository.save(memberImage);
                map.put("status", 200);
            }
            else if(!memberImageService.checkmemberImage(username)){
                memberImage.setNo(memberImageRepository.findByUserid(memberImage.getUserid()).getNo());
                memberImage.setImagedata(file.getBytes());
                memberImage.setImagename(file.getOriginalFilename());
                memberImage.setImagesize(file.getSize());
                memberImage.setImagetype(file.getContentType());

                System.out.println(memberImage.getImagename().toString());
                memberImageRepository.save(memberImage);
                map.put("status", 200);
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 127.0.0.1:8081/COCO/api/memberimg/select.json
    @GetMapping(value = "/select.json")
    public Map<String, Object> selectGET(@RequestHeader(name = "TOKEN") String token){
        Map<String, Object> map = new HashMap<>();
        try{
            String username = jwtUtil.extractUsername(token);
            MemberImage memberImage = memberImageRepository.findByUserid(username);
            map.put("status", 200);
            map.put("image", memberImage);
        }
        catch(Exception e){
            map.put("status", -1);
        }
        return map;
    }
}