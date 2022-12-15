package com.example.restcontroller.admin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Member;
import com.example.entity.Qna;
import com.example.entity.Teacher;
import com.example.service.admin.AdminService;

import lombok.RequiredArgsConstructor;




@RestController
@RequestMapping(value = "/api/admin")
@RequiredArgsConstructor
public class AdminRestController {

    final AdminService adminService;
    
    // 회원 리스트 (검색)
    // 127.0.0.1:8081/COCO/api/admin/memberlist?page=1&id=
    @GetMapping(value="/memberlist")
    public Map<String, Object> MemberListGET(
        HttpServletRequest request,
        @RequestParam(name = "id", defaultValue = "") String id,
        @RequestParam(name = "page", defaultValue = "1") int page
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            String[] arr = (String[]) request.getAttribute("role");
            if(Arrays.asList(arr).contains("ADMIN")){
                PageRequest pageRequest = PageRequest.of(page-1, 10);
                List<Member> list = adminService.selectMemberList(id, pageRequest);

                map.put("result", list);
                map.put("status", 200);
            }
            else{
                map.put("status", 0);
            }
        } catch (Exception e) {
            map.put("result", e.getMessage());
            map.put("status", -1);
        }
        return map;
    }

    // 회원 블락 처리
    // 127.0.0.1:8081/COCO/api/admin/updateblock.json
    @PutMapping(value="/updateblock.json")
    public Map<String, Object> updateBlockPUT(
        HttpServletRequest request,
        @RequestBody Member member
        ) {
        Map<String, Object> map = new HashMap<>();
        try {
            String[] arr = (String[]) request.getAttribute("role");
            if(Arrays.asList(arr).contains("ADMIN")){
                int ret = adminService.updateMemberBlock(member);
                map.put("status", 200);
                map.put("result", ret);
            }
            else {
                map.put("status", 0);
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    
    // 강사 신청자 목록
    // 127.0.0.1:8081/COCO/api/admin/appteacherlist?page=1
    @GetMapping(value="/appteacherlist")
    public Map<String, Object> apptechlistGET(
        HttpServletRequest request,
        @RequestParam(name = "page", defaultValue = "1") int page
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            String[] arr = (String[]) request.getAttribute("role");
            if(Arrays.asList(arr).contains("ADMIN")){
                // PageRequest pageRequest = PageRequest.of(page-1, 10);
                // List<Teacher> list = adminService.selectAppTechList(pageRequest);
                map.put("status", 200);
                // map.put("result", list);
            }
            else{
                map.put("status", 0);
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
    // 관리자 강사 권한 부여
    // 127.0.0.1:8081/COCO/api/admin/allowtech.json
    @PostMapping(value="/allowtech.json")
    public Map<String, Object> allowTechPOST(
        HttpServletRequest request,
        @RequestBody Teacher teacher
        ) {
            Map<String, Object> map = new HashMap<>();
        try {
            String[] arr = (String[]) request.getAttribute("role");
            if(Arrays.asList(arr).contains("ADMIN")){
                int ret = adminService.allowTech(teacher);

                map.put("result", ret);
                map.put("status", 200);
            }
            else{
                map.put("status", 0);
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 관리자 강사 거절
    // 127.0.0.1:8081/COCO/api/admin/refusetech.json
    @PostMapping(value="/refusetech.json")
    public Map<String, Object> refuseTechPOST(
        HttpServletRequest request,
        @RequestBody Teacher teacher
        ) {
            Map<String, Object> map = new HashMap<>();
        try {
            String[] arr = (String[]) request.getAttribute("role");
            if(Arrays.asList(arr).contains("ADMIN")){
                int ret = adminService.refuseTech(teacher);

                map.put("result", ret);
                map.put("status", 200);
            }
            else{
                map.put("status", 0);
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // QnA 목록
    // 127.0.0.1:8081/COCO/api/admin/QnAlist?page=1
    @GetMapping(value="/QnAlist")
    public Map<String, Object> QnAlistGET(
        HttpServletRequest request,
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "type", defaultValue = "") String type
        ) {
        Map<String, Object> map = new HashMap<>();
        try {
            System.out.println(type);
            String[] arr = (String[]) request.getAttribute("role");
            if(Arrays.asList(arr).contains("ADMIN")){
                PageRequest pageRequest = PageRequest.of(page-1, 10);
                List<Qna> list = adminService.QnaList(type, pageRequest);

                map.put("status", 200);
                map.put("result", list);
            }
            else{
                map.put("status", 0);
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // // QnA 답변
    // @PostMapping(value="/answerQnA.json")
    // public Map<String, Object> answerQnAPOST(
    //     HttpServletRequest request,
    //     @RequestBody Qna qna
    // ) {
    //     Map<String, Object> map = new HashMap<>();
    //     try {
    //         int ret = adminService.answerQna(qna);
    //         map.put("status", 200);
    //         map.put("result", ret);
    //     } catch (Exception e) {
    //         map.put("status", -1);
    //         map.put("result", e.getMessage());
    //     }
    //     return map;
    // }
    
    
    
    
    
}
